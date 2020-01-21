package com.noavaran.system.vira.baryab.activities.controllers;

import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.MapsActivity;
import com.noavaran.system.vira.baryab.activities.delegates.MapDelegate;
import com.noavaran.system.vira.baryab.activities.models.MapModel;
import com.noavaran.system.vira.baryab.googlemap.PlaceJSONParser;
import com.noavaran.system.vira.baryab.helpers.GoogleMapHelper;
import com.noavaran.system.vira.baryab.info.AddressInfo;
import com.noavaran.system.vira.baryab.info.GeocoderInfo;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.info.PlacesInfo;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapController implements MapDelegate.Controller {
    public final static int KEY_PLACE_TYPE_ORIGIN = 0;
    public final static int KEY_PLACE_TYPE_DESTINATION = 1;

    private       MapsActivity       activity;
    private       MapModel           mapModel;
    private final MapDelegate.View   view;
    private       Fragment           currentFragment;

    private       GoogleMapHelper    googleMapHelper;

    private       PlacesTask         placesTask;
    private       ParserTask         parserTask;

    public interface OnGeoListener {
        public abstract void onFinish();
        public abstract void onFailure();
    }

    public MapController(MapsActivity activity, MapDelegate.View view) {
        this.activity = activity;
        this.view = view;

        this.mapModel = new MapModel();
    }

    @Override
    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void gotoPreviousFragment() {
        view.gotoPreviousFragment();
    }

    @Override
    public void setGoogleMapHelper(GoogleMapHelper googleMapHelper) {
        this.googleMapHelper = googleMapHelper;
    }

    @Override
    public void addNewPlace(GoogleMap googleMap, int placeType, LatLngInfo latLngInfo, OnGeoListener geoListener) {
        getAddressBox(googleMap, placeType, latLngInfo, geoListener);
    }

    @Override
    public void onMapMarkersChanged(LatLngInfo lastLatLngInfo) {
        view.onMapMarkersChanged(lastLatLngInfo);
    }

    @Override
    public List<PlacesInfo> getPlaces() {
        return mapModel.getPlaces();
    }

    @Override
    public List<PlacesInfo> getOriginPlaces() {
        List<PlacesInfo> listOrigins = new ArrayList<>();

        for (int i = 0; i < getPlaces().size(); i++) {
            if (getPlaces().get(i).getPlaceType() == MapController.KEY_PLACE_TYPE_ORIGIN) {
                listOrigins.add(getPlaces().get(i));
            }
        }

        return listOrigins;
    }

    @Override
    public List<PlacesInfo> getDestinationPlaces() {
        List<PlacesInfo> listDestination = new ArrayList<>();

        for (int i = 0; i < getPlaces().size(); i++) {
            if (getPlaces().get(i).getPlaceType() == MapController.KEY_PLACE_TYPE_DESTINATION) {
                listDestination.add(getPlaces().get(i));
            }
        }

        return listDestination;
    }

    @Override
    public int getOriginItemCount() {
        int count = 0;

        for (int i = 0; i < getPlaces().size(); i++) {
            if (getPlaces().get(i).getPlaceType() == MapController.KEY_PLACE_TYPE_ORIGIN)
                count++;
        }

        return count;
    }

    @Override
    public int getDestinationItemCount() {
        int count = 0;

        for (int i = 0; i < getPlaces().size(); i++) {
            if (getPlaces().get(i).getPlaceType() == MapController.KEY_PLACE_TYPE_DESTINATION)
                count++;
        }

        return count;
    }

    @Override
    public void changeMarkerPosition(final MapDelegate.ChosenLocationsPopup chosenLocationsPopup, final int placeType, final int position) {
        googleMapHelper.getGoogleMap().clear();

        int pos = 0;
        if (placeType == MapController.KEY_PLACE_TYPE_ORIGIN)
            pos = position;
        else if (placeType == MapController.KEY_PLACE_TYPE_DESTINATION)
            pos = position + getOriginItemCount();

        for (int i = 0; i < getPlaces().size(); i++) {
            if (getPlaces().get(i).getPlaceType() == placeType && i != pos)
                addMarkerToMap(getPlaces().get(i).getLatLngInfo(), getPlaces().get(i).getPlaceType());
        }

        final PlacesInfo placesInfo = getPlaces().get(pos);
        googleMapHelper.gotoThisPositionOnTheMap(placesInfo.getLatLngInfo(), false);
        chosenLocationsPopup.disableButtonDismiss();

        final int finalPos = pos;
        view.getImagePlaceChooserMarker().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentLatLng = googleMapHelper.getGoogleMap().getProjection().getVisibleRegion().latLngBounds.getCenter();
                LatLngInfo currentLatLngInfo = new LatLngInfo(currentLatLng.latitude, currentLatLng.longitude);
                googleMapHelper.addCustomMarkerToMap(currentLatLng.latitude, currentLatLng.longitude, placeType == MapController.KEY_PLACE_TYPE_ORIGIN ? R.drawable.ic_origin : R.drawable.ic_destination, false);

                LatLngInfo lastLatLngInfo = getPlaces().get(getPlaces().size() - 1).getLatLngInfo();
                googleMapHelper.gotoThisPositionOnTheMap(lastLatLngInfo.getLatitude() + 0.004000, lastLatLngInfo.getLongitude() + 0.004000, true);

                GeocoderInfo geocoderInfo = googleMapHelper.getLocationAddress(currentLatLngInfo);

                int placeType = placesInfo.getPlaceType();

                getPlaces().remove(finalPos);
                chosenLocationsPopup.notifyDataSetChanged();

                getAddressBox(chosenLocationsPopup, placeType, geocoderInfo, currentLatLngInfo, finalPos);
            }
        });
    }

    @Override
    public void removePlace(int placeType, int position) {
        getPlaces().remove(position);
        googleMapHelper.getGoogleMap().clear();

        for (PlacesInfo placesInfo : getPlaces()) {
            if (placesInfo.getPlaceType() == placeType)
                addMarkerToMap(placesInfo.getLatLngInfo(), placesInfo.getPlaceType());
        }

        if (placeType == MapController.KEY_PLACE_TYPE_ORIGIN && getOriginItemCount() < 3)
            view.showAddNewLocationPin();
        else if (placeType == MapController.KEY_PLACE_TYPE_DESTINATION && getDestinationItemCount() < 3)
            view.showAddNewLocationPin();
    }

    @Override
    public void onAddNewMarker() {
        view.onAddNewMarker();
    }

    @Override
    public void onConfirm() {
        view.onConfirm();
    }

    @Override
    public void executeSearchPlace(CharSequence s) {
        this.placesTask = new PlacesTask();
        this.placesTask.execute(s.toString());
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public class PlacesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... place) {
            String data = "";
            String key = "key=" + Configuration.GOOGLE_PLACE_SEARCH_API_KEY;
            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String types = "types=geocode";
            String sensor = "sensor=false";
            String parameters = input + "&" + types + "&" + sensor + "&" + key;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            //https://maps.googleapis.com/maps/api/place/autocomplete/json?input=Vict&types=geocode&language=fr&key=YOUR_API_KEY

            try {
                data = downloadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            SimpleAdapter adapter = new SimpleAdapter(activity.getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            view.setSearchPlacesAdapter(adapter);
        }
    }

    // Getting id, province, and city from geo server while update a marker
    private void getAddressBox(MapDelegate.ChosenLocationsPopup chosenLocationsPopup, int placeType, GeocoderInfo geocoderInfo, LatLngInfo latLngInfo, int finalPos) {
        String lat1 = Double.toString(latLngInfo.getLatitude() - 0.0001);
        String lot1 = Double.toString(latLngInfo.getLongitude() - 0.0001);

        String lat2 = Double.toString(latLngInfo.getLatitude() + 0.0001);
        String lot2 = Double.toString(latLngInfo.getLongitude() + 0.0001);

        String box = lot1 + "," + lat1 + "," + lot2 + "," + lat2;

        getAddressFromLatLng(chosenLocationsPopup, placeType, geocoderInfo, latLngInfo, finalPos, box);
    }

    public void getAddressFromLatLng(final MapDelegate.ChosenLocationsPopup chosenLocationsPopup, final int placeType, final GeocoderInfo geocoderInfo, final LatLngInfo latLngInfo, final int finalPos, String box) {
        new OkHttpHelper(activity, Configuration.GEO_SERVER_URL, OkHttpHelper.MEDIA_TYPE_JSON).getAddressFromLatLng(box, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                activity.showDialogProgress("در حال دریافت اطلاعات از سرور");
            }

            @Override
            public void onResponse(JSONObject result) {
                loadAddressFromLatLng(chosenLocationsPopup, placeType, geocoderInfo, latLngInfo, finalPos, result);
            }

            @Override
            public void onRequestReject(String message) {
                activity.showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                activity.dismissDialogProgress();
                activity.showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                activity.dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                activity.dismissDialogProgress();
            }
        });
    }

    private void loadAddressFromLatLng(final MapDelegate.ChosenLocationsPopup chosenLocationsPopup, final int placeType, final GeocoderInfo geocoderInfo, final LatLngInfo latLngInfo, final int finalPos, final JSONObject result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int id = result.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getInt("OBJECTID");
                    String province = result.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getString("ostan");
                    String city = result.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getString("CENTER");

                    getPlaces().add(finalPos, new PlacesInfo(placeType, latLngInfo, geocoderInfo, new AddressInfo(id, province, city)));
                    chosenLocationsPopup.notifyDataSetChanged();

                    chosenLocationsPopup.enableButtonDismiss();
                    view.getImagePlaceChooserMarker().setOnClickListener(null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Getting id, province, and city from geo server while add a new marker
    private void getAddressBox(GoogleMap googleMap, int placeType, LatLngInfo latLngInfo, OnGeoListener geoListener) {
        String lat1 = Double.toString(latLngInfo.getLatitude() - 0.01);
        String lot1 = Double.toString(latLngInfo.getLongitude() - 0.01);

        String lat2 = Double.toString(latLngInfo.getLatitude() + 0.01);
        String lot2 = Double.toString(latLngInfo.getLongitude() + 0.01);

        String box = lot1 + "," + lat1 + "," + lot2 + "," + lat2;

        getAddressFromLatLng(googleMap, placeType, latLngInfo, box, geoListener);
    }

    public void getAddressFromLatLng(final GoogleMap googleMap, final int placeType, final LatLngInfo latLngInfo, String box, final OnGeoListener geoListener) {
        new OkHttpHelper(activity, Configuration.GEO_SERVER_URL, OkHttpHelper.MEDIA_TYPE_JSON).getAddressFromLatLng(box, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                activity.showDialogProgress("در حال دریافت اطلاعات از سرور");
            }

            @Override
            public void onResponse(JSONObject result) {
                activity.dismissDialogProgress();
                loadAddressFromLatLng(googleMap, placeType, latLngInfo, result, geoListener);
            }

            @Override
            public void onRequestReject(String message) {
                activity.showToastWarning(message);
                geoListener.onFailure();
            }

            @Override
            public void onFailure(String errorMessage) {
                activity.dismissDialogProgress();
                activity.showToastError(errorMessage);
                geoListener.onFailure();
            }

            @Override
            public void onNoInternetConnection() {
                activity.dismissDialogProgress();
                geoListener.onFailure();
            }

            @Override
            public void onFinish() {
                activity.dismissDialogProgress();
            }
        });
    }

    private void loadAddressFromLatLng(final GoogleMap googleMap, final int placeType, final LatLngInfo latLngInfo, final JSONObject result, final OnGeoListener geoListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int id = result.optJSONArray("features").optJSONObject(0).optJSONObject("properties").optInt("OBJECTID", 0);
                    String province = result.optJSONArray("features").optJSONObject(0).optJSONObject("properties").optString("ostan");
                    String city = result.optJSONArray("features").optJSONObject(0).optJSONObject("properties").optString("CENTER");

                    googleMapHelper = new GoogleMapHelper(activity, googleMap);
                    GeocoderInfo geocoderInfo = googleMapHelper.getLocationAddress(latLngInfo);
                    mapModel.addNewPlace(new PlacesInfo(placeType, latLngInfo, geocoderInfo, new AddressInfo(id, province, city)));
                    geoListener.onFinish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void addMarkerToMap(LatLngInfo latLngInfo, int placeType) {
        googleMapHelper.addCustomMarkerToMap(latLngInfo.getLatitude(), latLngInfo.getLongitude(), placeType == MapController.KEY_PLACE_TYPE_ORIGIN ? R.drawable.ic_origin : R.drawable.ic_destination, false);
        googleMapHelper.gotoThisPositionOnTheMap(latLngInfo.getLatitude() + 0.004000, latLngInfo.getLongitude() + 0.004000, true);
    }
}