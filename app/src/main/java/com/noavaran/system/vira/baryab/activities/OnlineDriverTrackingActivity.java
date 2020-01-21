package com.noavaran.system.vira.baryab.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;
import com.noavaran.system.vira.baryab.helpers.GoogleMapHelper;
import com.noavaran.system.vira.baryab.info.OnlineDriverTrackingInfo;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.DialerUtil;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class OnlineDriverTrackingActivity extends BaseActivity implements OnMapReadyCallback {
    private MapView mapView;
    private RelativeLayout rlDriverDetails;
    private CustomTextView btnContact;
    private CustomTextView tvLoadingType;
    private ImageView ivDriverPhoto;
    private CustomTextView tvDriverName;
    private CustomTextView tvTruckType;

    private CustomTextView tvTruckDetails;
    private GoogleMapHelper googleMapHelper;
    private String draftId;
    private String info;
    private OnlineDriverTrackingInfo trackingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_driver_tracking);

        getIntents();
        findViews();
        initComponents();
        initMapView(savedInstanceState);
    }

    private void getIntents() {
        if (getIntent().getExtras() != null) {
            draftId = getIntent().getExtras().getString("draftId");
            info = getIntent().getExtras().getString("info");

            try {
                ObjectMapper mapper = new ObjectMapper();
                trackingInfo = mapper.readValue(info, OnlineDriverTrackingInfo.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void findViews() {
        mapView = (MapView) findViewById(R.id.acOnlineDriverTracking_mapView);
        rlDriverDetails = (RelativeLayout) findViewById(R.id.acOnlineDriverTracking_rlDriverDetails);
        btnContact = (CustomTextView) findViewById(R.id.acOnlineDriverTracking_btnContact);
        tvLoadingType = (CustomTextView) findViewById(R.id.acOnlineDriverTracking_tvLoadingType);
        ivDriverPhoto = (ImageView) findViewById(R.id.acOnlineDriverTracking_ivDriverPhoto);
        tvDriverName = (CustomTextView) findViewById(R.id.acOnlineDriverTracking_tvDriverName);
        tvTruckType = (CustomTextView) findViewById(R.id.acOnlineDriverTracking_tvTruckType);
        tvTruckDetails = (CustomTextView) findViewById(R.id.acOnlineDriverTracking_tvTruckDetails);
    }

    private void initComponents() {
        tvLoadingType.setText(trackingInfo.getShipmentType());
        Picasso.with(OnlineDriverTrackingActivity.this).load(Configuration.BASE_IMAGE_URL + trackingInfo.getDriverPhoto()).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(ivDriverPhoto);
        tvDriverName.setText(trackingInfo.getDriverName());
        tvTruckType.setText(trackingInfo.getTruckType());
        tvTruckDetails.setText(trackingInfo.getTruckDetails());
    }

    private void setViewsListeners() {
        btnContact.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DialerUtil.getInstance(OnlineDriverTrackingActivity.this).openTheDialerAppAndDoTheCallAutomatically(trackingInfo.getContactNumber());
            }
        });
    }

    private void initMapView(Bundle savedInstanceState) {
        try {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            MapsInitializer.initialize(this);

            mapView.getMapAsync(this);
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
        this.googleMapHelper = new GoogleMapHelper(OnlineDriverTrackingActivity.this, googleMap);
        this.googleMapHelper.setZoomLevel(6);
        this.googleMapHelper.showIranMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initGoogleMap(googleMap);
        initGoogleMapHelper(googleMap);
        setViewsListeners();

        getLastDriverLocationFromServer();
    }

    private void getLastDriverLocationFromServer() {
        OkHttpHelper okHttpHelper = new OkHttpHelper(OnlineDriverTrackingActivity.this, Configuration.API_LAST_DRIVER_LOCATION, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getLastDriverLocation(draftId, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                loadLastDriverLocation(result);
            }

            @Override
            public void onRequestReject(String message) {
                dismissDialogProgress();
                showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                dismissDialogProgress();
                showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                dismissDialogProgress();
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void loadLastDriverLocation(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        JSONObject data = result.optJSONObject("data");
                        JSONArray mabda = data.optJSONArray("Mabda");
                        JSONArray maghsad = data.optJSONArray("Maghsad");

                        for (int i = 0; i < mabda.length(); i++) {
                            float lat = BigDecimal.valueOf(mabda.optJSONObject(i).optJSONObject("lanLatvm").optDouble("latitude", 0)).floatValue();
                            float lon = BigDecimal.valueOf(mabda.optJSONObject(i).optJSONObject("lanLatvm").optDouble("longitude", 0)).floatValue();
                            googleMapHelper.addCustomMarkerToMap(lat, lon, R.drawable.ic_origin, false);
                        }

                        for (int i = 0; i < maghsad.length(); i++) {
                            float lat = BigDecimal.valueOf(maghsad.optJSONObject(i).optJSONObject("lanLatvm").optDouble("latitude", 0)).floatValue();
                            float lon = BigDecimal.valueOf(maghsad.optJSONObject(i).optJSONObject("lanLatvm").optDouble("longitude", 0)).floatValue();
                            googleMapHelper.addCustomMarkerToMap(lat, lon, R.drawable.ic_destination, false);
                        }

                        if (!data.isNull("cellid") && !data.isNull("celllac") && !data.isNull("cellsds") && !data.isNull("cellstatus")) {
                            int cellId = data.optInt("cellid", 0);
                            int cellLac = data.optInt("celllac", 0);
                            int cellSds = data.optInt("cellsds", 0);
                            String cellStatus = data.optString("cellstatus", "");

                            if (cellId > 0 && cellLac > 0 && cellSds > 0 && !GlobalUtils.IsNullOrEmpty(cellStatus))
                                getGeoLocationFromServer(cellId, cellLac, cellSds, cellStatus);
                            else
                                dismissDialogProgress();
                        } else {
                            MessageDialog messageDialog = new MessageDialog(OnlineDriverTrackingActivity.this, getString(R.string.app_name), "هیچ موقعیتی از راننده در حال حاضر یافت نشد. می توانید با راننده جهت اینکه موقعیت خود را ارسال کنند تماس بگیرید.");
                            messageDialog.show();
                        }
                    }
                } catch (Exception ex) {
                    dismissDialogProgress();
                    ex.printStackTrace();
                }
            }
        });
    }

    private void getGeoLocationFromServer(int cellId, int cellLac, int cellSds, String cellStatus) {
        new OkHttpHelper(OnlineDriverTrackingActivity.this, Configuration.API_GET_LAST_DRIVER_LOCATION, OkHttpHelper.MEDIA_TYPE_JSON).getLastDriverLocation(cellId, cellLac, cellSds, cellStatus, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onResponse(JSONObject result) {
                loadGeoLocation(result);
            }

            @Override
            public void onRequestReject(String message) {
                dismissDialogProgress();
                showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                showToastError(errorMessage);
                dismissDialogProgress();
            }

            @Override
            public void onNoInternetConnection() {
                dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                dismissDialogProgress();
            }
        });
    }

    private void loadGeoLocation(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject data = result.optJSONObject("data");

                    float driverCurrentLocationLat = BigDecimal.valueOf(data.optDouble("lat", 0)).floatValue();
                    float driverCurrentLocationLon = BigDecimal.valueOf(data.optDouble("lon", 0)).floatValue();

                    showDriverLocation(driverCurrentLocationLat, driverCurrentLocationLon);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                dismissDialogProgress();
            }
        });
    }

    private void showDriverLocation(float driverCurrentLocationLat, float driverCurrentLocationLon) {
        if (driverCurrentLocationLat > 0 && driverCurrentLocationLon > 0) {
            googleMapHelper.setZoomLevel(9);
            googleMapHelper.addCustomMarkerToMap(driverCurrentLocationLat, driverCurrentLocationLon, R.drawable.ic_driver_current_location, false);
            googleMapHelper.gotoThisPositionOnTheMap(driverCurrentLocationLat, driverCurrentLocationLon, true);
        } else {
            MessageDialog messageDialog = new MessageDialog(OnlineDriverTrackingActivity.this, getString(R.string.app_name), "هیچ اطلاعات در مورد آخرین موقعیت راننده در حال حاضر در دسترس نمی باشد.");
            messageDialog.show();
        }
    }
}