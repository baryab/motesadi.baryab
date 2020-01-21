package com.noavaran.system.vira.baryab.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.controllers.MapController;
import com.noavaran.system.vira.baryab.activities.delegates.MapDelegate;
import com.noavaran.system.vira.baryab.customviews.CustomAutoCompleteTextView;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.ConfirmDialog;
import com.noavaran.system.vira.baryab.fragments.DestinationDeterminationFragment;
import com.noavaran.system.vira.baryab.fragments.OriginDeterminationFragment;
import com.noavaran.system.vira.baryab.googlemap.GPSSettingRequest;
import com.noavaran.system.vira.baryab.helpers.GPSHelper;
import com.noavaran.system.vira.baryab.helpers.GoogleMapHelper;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.info.PlacesInfo;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.ImageUtil;

import org.json.JSONArray;

import java.util.Collections;
import java.util.List;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, MapDelegate.View {
    private final static String KEY_ORIGIN = "origin";
    private final static String KEY_DESTINATION = "destination";

    private MapController                     controller;
    private GPSSettingRequest                 gpsRequest;
    private GoogleMapHelper                   googleMapHelper;
    private MapView                           mapView;
    private RelativeLayout                    rlContainer;
    private CustomAutoCompleteTextView        etSearchPlaces;
    private ImageView                         ivPlaceChooserMarker;
    private CustomTextView                    btnMyLocation;
    private OriginDeterminationFragment       originDeterminationFragment;
    private DestinationDeterminationFragment  destinationDeterminationFragment;
    private boolean                           isMyLocationEnabled = false;
    private String                            strPlacesInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        findViews();
        initComponents();
        initMapView(savedInstanceState);
        setViewListeners();
    }

    private void findViews() {
        this.rlContainer          =  findViewById(R.id.acMaps_rlContainer);
        this.etSearchPlaces       =  findViewById(R.id.acMaps_etSearchPlaces);
        this.mapView              =  findViewById(R.id.acMaps_mapView);
        this.ivPlaceChooserMarker =  findViewById(R.id.acMaps_ivPlaceChooserMarker);
        this.btnMyLocation        =  findViewById(R.id.acMaps_btnMyLocation);
    }

    private void initComponents() {


        controller = new MapController(MapsActivity.this, this);
        gpsRequest = new GPSSettingRequest(MapsActivity.this);

        originDeterminationFragment = new OriginDeterminationFragment();
        originDeterminationFragment.setMapController(controller);

        destinationDeterminationFragment = new DestinationDeterminationFragment();
        destinationDeterminationFragment.setMapController(controller);

        this.mapView.getMapAsync(this);

        replaceFragment(originDeterminationFragment);
    }

    private void getIntents() {

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("places")) {
            Bundle extras = intent.getBundleExtra("places");
            strPlacesInfo = extras.getString("place_info");

            if (!GlobalUtils.IsNullOrEmpty(strPlacesInfo)) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<PlacesInfo> list = mapper.readValue(strPlacesInfo, TypeFactory.defaultInstance().constructCollectionType(List.class, PlacesInfo.class));
                    controller.getPlaces().addAll(list);

                    if (controller.getOriginItemCount() >= 3) {
                        hideAddNewLocationPin();
                    } else {
                        showAddNewLocationPin();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void initMapView(Bundle savedInstanceState) {
        try {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            MapsInitializer.initialize(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initGoogleMap(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(false);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void initGoogleMapHelper(GoogleMap googleMap) {
        this.googleMapHelper = new GoogleMapHelper(MapsActivity.this, googleMap);
        this.googleMapHelper.setZoomLevel(6);
        this.googleMapHelper.showIranMap();
    }

    private void setViewListeners() {
        this.btnMyLocation.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!GPSHelper.isGPSProviderEnabled(MapsActivity.this)) {
                    gpsRequest.request();
                } else {
                    findMyLocation();
                }
            }
        });

        this.etSearchPlaces.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                controller.executeSearchPlace(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        this.etSearchPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalUtils.hideSoftKeyboard(MapsActivity.this);
                LatLngInfo latLngInfo = googleMapHelper.getLatLngFromGivenAddress(etSearchPlaces.getText().toString());
                googleMapHelper.gotoThisPositionOnTheMap(latLngInfo, true);
                etSearchPlaces.setText("");
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        /**
         * Add new fragment to container if it already has not been added, otherwise it will be shown
         */
        if (fragment.isAdded()) {
            if (controller.getCurrentFragment() != null)
                fragmentTransaction.hide(controller.getCurrentFragment());

            fragmentTransaction.show(fragment);
        } else {
            if (controller.getCurrentFragment() != null)
                fragmentTransaction.hide(controller.getCurrentFragment());

            fragmentTransaction.add(R.id.acMaps_flContainers, fragment);
        }

        fragmentTransaction.commit();
        controller.setCurrentFragment(fragment);

        /**
         * change place chooser marker's color and load markers according to current fragment
         */
        if (controller.getCurrentFragment() == originDeterminationFragment) {
            ivPlaceChooserMarker.setImageBitmap(ImageUtil.getInstance(MapsActivity.this).getBitmapFromDrawable(R.drawable.ic_origin, ""));

            if (googleMapHelper != null && googleMapHelper.getGoogleMap() != null) {
                googleMapHelper.getGoogleMap().clear();
                for (PlacesInfo placesInfo : controller.getOriginPlaces()) {
                    LatLngInfo latLngInfo = placesInfo.getLatLngInfo();
                    googleMapHelper.addCustomMarkerToMap(latLngInfo.getLatitude(), latLngInfo.getLongitude(), R.drawable.ic_origin, false);
                }

                if (controller.getOriginItemCount() >= 3)
                    hideAddNewLocationPin();
                else
                    showAddNewLocationPin();
            }
        } else if (googleMapHelper != null && controller.getCurrentFragment() == destinationDeterminationFragment) {
            ivPlaceChooserMarker.setImageBitmap(ImageUtil.getInstance(MapsActivity.this).getBitmapFromDrawable(R.drawable.ic_destination, ""));

            if (googleMapHelper.getGoogleMap() != null) {
                googleMapHelper.getGoogleMap().clear();
                for (PlacesInfo placesInfo : controller.getDestinationPlaces()) {
                    LatLngInfo latLngInfo = placesInfo.getLatLngInfo();
                    googleMapHelper.addCustomMarkerToMap(latLngInfo.getLatitude(), latLngInfo.getLongitude(), R.drawable.ic_destination, false);
                }
            }

            if (controller.getDestinationItemCount() >= 3)
                hideAddNewLocationPin();
            else
                showAddNewLocationPin();
        }
    }

    private void findMyLocation() {
        if (btnMyLocation.getTag().toString().equals("off")) {
            btnMyLocation.setTag("on");
            btnMyLocation.setTextColor(Color.parseColor("#3f51b5"));
        } else if (btnMyLocation.getTag().toString().equals("on")) {
            btnMyLocation.setTag("off");
            btnMyLocation.setTextColor(Color.parseColor("#8a000000"));
        }

        if (btnMyLocation.getTag().toString().equals("off")) {
            isMyLocationEnabled = false;
        } else if (btnMyLocation.getTag().toString().equals("on")) {
            isMyLocationEnabled = true;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        initGoogleMap(googleMap);
        initGoogleMapHelper(googleMap);
        getIntents();
        setViewListeners();

        controller.setGoogleMapHelper(googleMapHelper);

        if (!GlobalUtils.IsNullOrEmpty(strPlacesInfo)) {
            if (!strPlacesInfo.equals("[]"))
                onMapMarkersChanged(controller.getOriginPlaces().get(controller.getOriginItemCount() - 1).getLatLngInfo());
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (isMyLocationEnabled)
                    googleMapHelper.gotoThisPositionOnTheMap(location.getLatitude(), location.getLongitude(), true);
            }
        });

        /**
         *  Save zoom's level status on google map helper's class when
         *  map's camera is changed by user
         */
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = googleMap.getCameraPosition();
                googleMapHelper.setZoomLevel(cameraPosition.zoom);
            }
        });

        /**
         *  When user drag map, button my location will be disable
         *  and map's camera is not focused on the current location
         */
        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason == REASON_GESTURE) {
                    btnMyLocation.setTextColor(Color.parseColor("#8a000000"));
                    btnMyLocation.setTag("off");
                    isMyLocationEnabled = false;
                }
            }
        });
    }

    @Override
    public void setSearchPlacesAdapter(SimpleAdapter adapter) {
        this.etSearchPlaces.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddNewMarker() {
        final LatLng latLng = googleMapHelper.getGoogleMap().getProjection().getVisibleRegion().latLngBounds.getCenter();
        LatLngInfo latLngInfo = new LatLngInfo(latLng.latitude, latLng.longitude);

        if (controller.getCurrentFragment() == originDeterminationFragment) {
            if (controller.getOriginItemCount() >= 2) {
                showMessageDialog("حداکثر محل بارگیری توسط شما تعیین شد. جهت تعیین محل های تخیله دکمه تایید در صفحه نقشه را لمس نمایید");
            } else {
                controller.addNewPlace(googleMapHelper.getGoogleMap(), MapController.KEY_PLACE_TYPE_ORIGIN, latLngInfo, new MapController.OnGeoListener() {
                    @Override
                    public void onFinish() {
                        googleMapHelper.addCustomMarkerToMap(latLng.latitude, latLng.longitude, R.drawable.ic_origin, false);
                        googleMapHelper.gotoThisPositionOnTheMap(latLng.latitude + 0.004000, latLng.longitude + 0.004000, true);
                        showAddNewLocationPin();

                        if (controller.getOriginItemCount() == 3) {
                            showMessageDialog("حداکثر محل بارگیری توسط شما تعیین شد. جهت تعیین محل های تخیله دکمه تایید در صفحه نقشه را لمس نمایید");
                        }
                    }

                    @Override
                    public void onFailure() {
                        showToastError("اتصال با سرور امکانپذیر نمی باشد.");
                    }
                });

            }
        } else if (controller.getCurrentFragment() == destinationDeterminationFragment) {
            if (controller.getDestinationItemCount() >= 2) {
                showMessageDialog("حداکثر محل تخیله توسط شما تعیین شد. در صورت صحت محل های مشخص شده لطفا دکمه تایید نهایی در صفحه نقشه را لمس نمایید");
            } else {
                controller.addNewPlace(googleMapHelper.getGoogleMap(), MapController.KEY_PLACE_TYPE_DESTINATION, latLngInfo, new MapController.OnGeoListener() {
                    @Override
                    public void onFinish() {
                        googleMapHelper.addCustomMarkerToMap(latLng.latitude, latLng.longitude, R.drawable.ic_destination, false);
                        googleMapHelper.gotoThisPositionOnTheMap(latLng.latitude + 0.004000, latLng.longitude + 0.004000, true);
                        showAddNewLocationPin();

                        if (controller.getDestinationItemCount() == 3) {
                            hideAddNewLocationPin();
                            showMessageDialog("حداکثر محل تخیله توسط شما تعیین شد. در صورت صحت محل های مشخص شده لطفا دکمه تایید نهایی در صفحه نقشه را لمس نمایید");
                        }
                    }

                    @Override
                    public void onFailure() {
                        showToastError("اتصال با سرور امکانپذیر نمی باشد.");
                    }
                });

            }
        }
    }

    @Override
    public void onMapMarkersChanged(LatLngInfo lastLatLngInfo) {
        googleMapHelper.getGoogleMap().clear();

        if (controller.getCurrentFragment() == originDeterminationFragment) {
            for (PlacesInfo placesInfo : controller.getOriginPlaces()) {
                LatLngInfo latLngInfo = placesInfo.getLatLngInfo();
                googleMapHelper.addCustomMarkerToMap(latLngInfo.getLatitude(), latLngInfo.getLongitude(), R.drawable.ic_origin, false);
            }

            googleMapHelper.gotoThisPositionOnTheMap(lastLatLngInfo.getLatitude(), lastLatLngInfo.getLongitude(), true);

            if (controller.getOriginItemCount() < 3) showAddNewLocationPin();
        } else if (controller.getCurrentFragment() == destinationDeterminationFragment) {
            for (PlacesInfo placesInfo : controller.getDestinationPlaces()) {
                LatLngInfo latLngInfo = placesInfo.getLatLngInfo();
                googleMapHelper.addCustomMarkerToMap(latLngInfo.getLatitude(), latLngInfo.getLongitude(), R.drawable.ic_destination, false);
            }

            googleMapHelper.gotoThisPositionOnTheMap(lastLatLngInfo.getLatitude(), lastLatLngInfo.getLongitude(), true);

            if (controller.getDestinationItemCount() < 3) showAddNewLocationPin();
        }
    }

    @Override
    public void onConfirm() {
        final LatLng latLng = googleMapHelper.getGoogleMap().getProjection().getVisibleRegion().latLngBounds.getCenter();
        LatLngInfo latLngInfo = new LatLngInfo(latLng.latitude, latLng.longitude);

        if (controller.getCurrentFragment() == originDeterminationFragment) {
            if (controller.getOriginItemCount() >= 3) {
                replaceFragment(destinationDeterminationFragment);
            } else {
                controller.addNewPlace(googleMapHelper.getGoogleMap(), MapController.KEY_PLACE_TYPE_ORIGIN, latLngInfo, new MapController.OnGeoListener() {
                    @Override
                    public void onFinish() {
                        googleMapHelper.addCustomMarkerToMap(latLng.latitude, latLng.longitude, R.drawable.ic_origin, false);
                        googleMapHelper.gotoThisPositionOnTheMap(latLng.latitude + 0.004000, latLng.longitude + 0.004000, true);

                        showDialogProgress("در حال ذخیره سازی محل های بارگیری ...");
                        googleMapHelper.getGoogleMap().clear();
                        Collections.sort(controller.getPlaces());
                        replaceFragment(destinationDeterminationFragment);
                        dismissDialogProgress();
                    }

                    @Override
                    public void onFailure() {
                        showToastError("اتصال با سرور امکانپذیر نمی باشد.");
                    }
                });

            }
        } else if (controller.getCurrentFragment() == destinationDeterminationFragment) {
            if (controller.getDestinationItemCount() >= 3) {
                doFinish();
            } else {
                controller.addNewPlace(googleMapHelper.getGoogleMap(), MapController.KEY_PLACE_TYPE_DESTINATION, latLngInfo, new MapController.OnGeoListener() {
                    @Override
                    public void onFinish() {
                        googleMapHelper.addCustomMarkerToMap(latLng.latitude, latLng.longitude, R.drawable.ic_destination, false);
                        googleMapHelper.gotoThisPositionOnTheMap(latLng.latitude + 0.004000, latLng.longitude + 0.004000, true);

                        doFinish();
                    }

                    @Override
                    public void onFailure() {
                        showToastError("اتصال با سرور امکانپذیر نمی باشد.");
                    }
                });
            }
        }
    }

    @Override
    public void gotoPreviousFragment() {
        replaceFragment(originDeterminationFragment);
    }

    @Override
    public ImageView getImagePlaceChooserMarker() {
        return ivPlaceChooserMarker;
    }

    @Override
    public void showAddNewLocationPin() {
        ivPlaceChooserMarker.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddNewLocationPin() {
        ivPlaceChooserMarker.setVisibility(View.GONE);
    }

    private void doFinish() {
        googleMapHelper.getGoogleMap().clear();
        final String placeInfo = getPlacesInfoAsJsonString();
        setDataResult(placeInfo);
        showConfirmDialog(placeInfo);
    }

    private String getPlacesInfoAsJsonString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(controller.getPlaces());
            return jsonString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returning places info to fragment's new loading
     */
    private void setDataResult(String placeInfo) {
        Intent intent = new Intent();
        intent.putExtra("place_info", placeInfo);
        setResult(RESULT_OK, intent);
    }

    private void showConfirmDialog(String placeInfo) {
        try {
            JSONArray jsonArray = new JSONArray(placeInfo);
            String orgProvinceName = jsonArray.optJSONObject(0).optJSONObject("addressInfo").optString("province");
            String orgCityName = jsonArray.optJSONObject(0).optJSONObject("addressInfo").optString("city");
            String desProvinceName = jsonArray.optJSONObject(jsonArray.length() - 1).optJSONObject("addressInfo").optString("province");
            String desCityName = jsonArray.optJSONObject(jsonArray.length() - 1).optJSONObject("addressInfo").optString("city");

            ConfirmDialog confirmDialog = new ConfirmDialog(MapsActivity.this, getString(R.string.app_name), "آیا مسیری که برای بار خود تعریف کردید از استان " + orgProvinceName + " شهر " + orgCityName + " به استان " + desProvinceName + " شهر " + desCityName + " می باشد؟");
            confirmDialog.setOnClickListener(new ConfirmDialog.OnClickListener() {
                @Override
                public void onConfirm() {
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
            confirmDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPSSettingRequest.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d("Maps Activity", "GPS is turned on by user");
                        findMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
//                        GPSSettingRequest.getInstance(MapsActivity.this).request();
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (controller.getCurrentFragment() == originDeterminationFragment) {
            originDeterminationFragment.onBackPressed();
        } else if (controller.getCurrentFragment() == destinationDeterminationFragment) {
            destinationDeterminationFragment.onBackPressed();
        }

        final String placeInfo = getPlacesInfoAsJsonString();
        setDataResult(placeInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}