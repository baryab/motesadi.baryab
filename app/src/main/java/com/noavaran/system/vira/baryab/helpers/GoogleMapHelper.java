package com.noavaran.system.vira.baryab.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.info.GeocoderInfo;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.utils.ImageUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class GoogleMapHelper {
    private static GoogleMapHelper googleMapHelper;
    private Context context;

    private GoogleMap googleMap;
    private float zoomLevel;

    public GoogleMapHelper(Context context, GoogleMap googleMap) {
        this.context = context;
        this.googleMap = googleMap;
    }

    public static GoogleMapHelper getInstance(Context context, GoogleMap googleMap) {
        if (googleMapHelper == null)
            googleMapHelper = new GoogleMapHelper(context, googleMap);

        return googleMapHelper;
    }

    public void addMarkerToMap(double latitude, double longitude, boolean playAnimation) {
        if (googleMap != null) {
            if (latitude > 0 && longitude > 0) {
                LatLng latLng = new LatLng(latitude, longitude);

                googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)));
                gotoThisPositionOnTheMap(latitude, longitude, playAnimation);
            }
        }
    }

    public void addMarkerToMap(double latitude, double longitude, String title, boolean playAnimation) {
        if (googleMap != null) {
            if (latitude > 0 && longitude > 0) {
                LatLng latLng = new LatLng(latitude, longitude);

                googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(title));
                gotoThisPositionOnTheMap(latitude, longitude, playAnimation);
            }
        }
    }

    public void addMarkerToMap(double latitude, double longitude, String title, String snippet, boolean playAnimation) {
        if (googleMap != null) {
            if (latitude > 0 && longitude > 0) {
                LatLng latLng = new LatLng(latitude, longitude);

                googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(title).snippet(snippet));
                gotoThisPositionOnTheMap(latitude, longitude, playAnimation);
            }
        }
    }

    public void addCustomMarkerToMap(double latitude, double longitude, int markerDrawableId, boolean playAnimation) {
        if (googleMap != null) {
            if (latitude > 0 && longitude > 0) {
                LatLng latLng = new LatLng(latitude, longitude);

                googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.getInstance(context).getBitmapFromDrawable(markerDrawableId, ""))));
                gotoThisPositionOnTheMap(latitude, longitude, playAnimation);
            }
        }
    }

    public void addCustomMarkerToMap(double latitude, double longitude, int markerDrawableId, String markerLabel, boolean playAnimation) {
        if (googleMap != null) {
            if (latitude > 0 && longitude > 0) {
                LatLng latLng = new LatLng(latitude, longitude);

                googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.getInstance(context).getBitmapFromDrawable(markerDrawableId, markerLabel))));
                gotoThisPositionOnTheMap(latitude, longitude, playAnimation);
            }
        }
    }

    public void gotoThisPositionOnTheMap(LatLngInfo latLngInfo, boolean playAnimation) {
        gotoThisPositionOnTheMap(latLngInfo.getLatitude(), latLngInfo.getLongitude(), playAnimation);
    }

    public void gotoThisPositionOnTheMap(double latitude, double longitude, boolean playAnimation) {
        if (googleMap != null) {
            if (latitude > 0 && longitude > 0) {
                LatLng latLng = new LatLng(latitude, longitude);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(getZoomLevel()).build();
                if (playAnimation)
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                else
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    public GeocoderInfo getLocationAddress(LatLngInfo latLngInfo) {
        return getLocationAddress(latLngInfo.getLatitude(), latLngInfo.getLongitude());
    }

    public GeocoderInfo getLocationAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, new Locale("fa"));

        String address = null;
        String city = null;
        String state = null;
        String country = null;
        String postalCode = null;
        String knownName = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new GeocoderInfo(address, city, state, country, postalCode, knownName);
    }

    public LatLngInfo getLatLngFromGivenAddress(String yourAddress) {
        double lat = 0;
        double lng = 0;

        ((BaseActivity) context).showDialogProgress();
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" + Uri.encode(yourAddress) + "&sensor=false";

        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((BaseActivity) context).dismissDialogProgress();

        return new LatLngInfo(lat, lng);
    }

    public String getLocationAddressFromLatLngByUrl(double latitude, double longitude) {
        return getLocationAddressFromLatLngByUrl(new LatLng(latitude, longitude));
    }

    public String getLocationAddressFromLatLngByUrl(LatLng latLng) {
        String locationName = "";

        HttpClient httpclient = new DefaultHttpClient();
        String postURL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&sensor=true";
        HttpGet httppost = new HttpGet(postURL);
        try {

            HttpResponse response = httpclient.execute(httppost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            StringBuilder sb = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                sb.append(line + "\n");
            }

            String result = sb.toString();

            JSONObject jobj = new JSONObject(result);
            JSONArray array = jobj.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
            int size = array.length();
            for (int i = 0; i < size; i++) {
                JSONArray typearray = array.getJSONObject(i).getJSONArray("types");
                if (typearray.getString(0).equals("locality")) {
                    locationName = array.getJSONObject(i).getString("long_name");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return locationName;
    }

    public void showIranMap() {
//        CameraPosition newCamPos = new CameraPosition(new LatLng(32.427908, 53.688045999999986), getZoomLevel(), this.googleMap.getCameraPosition().tilt, this.googleMap.getCameraPosition().bearing);
//        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 5000, null);
        gotoThisPositionOnTheMap(new LatLngInfo(32.427908, 53.688045999999986), true);
    }

    public void setZoomLevel(float zoomLevel) {
        SPreferences.getInstance(context).setZoomLevel(zoomLevel);
        this.zoomLevel = zoomLevel;
    }

    public float getZoomLevel() {
        zoomLevel = SPreferences.getInstance(context).getZoomLevel();
        return zoomLevel;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }
}
