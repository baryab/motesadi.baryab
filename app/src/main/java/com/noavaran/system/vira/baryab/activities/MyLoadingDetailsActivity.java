package com.noavaran.system.vira.baryab.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.controllers.MyLoadingDetailsController;
import com.noavaran.system.vira.baryab.activities.delegates.MyLoadingDetailsDelegate;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.ConfirmDialog;
import com.noavaran.system.vira.baryab.enums.ShipmentEditTypeEnum;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.helpers.GoogleMapHelper;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

public class MyLoadingDetailsActivity extends BaseActivity implements OnMapReadyCallback, MyLoadingDetailsDelegate.View {
    private ScrollView scroll;
    private CustomTextView tvCarType;
    private CustomTextView btnBack;
    private ImageView ivLoadingPicture;
    private CustomTextView btnEdit;
    private CustomTextView btnDelete;
    private CustomTextView etRegisterTime;
    private CustomTextView etShipmentType;
    private CustomTextView etWeight;
    private CustomTextView etTruckType;
    private CustomTextView etTruckLength;
    private CustomTextView etTruckWidth;
    private CustomTextView tvTruckHeight;
    private CustomTextView etTruckHeight;
    private CustomTextView etLoadingFareType;
    private CustomTextView tvLoadingFare;
    private CustomTextView etLoadingFare;
    private CustomTextView etLoadingTime;
    private MapView mapView;
    private RelativeLayout rlAddress;
    private RelativeLayout rlAddress1;
    private RelativeLayout rlAddress2;
    private CustomTextView tvOriginMarker;
    private CustomTextView tvDestinationMarker;
    private CustomTextView tvOriginMarker1;
    private CustomTextView tvDestinationMarker1;
    private CustomTextView tvOriginMarker2;
    private CustomTextView tvDestinationMarker2;
    private CustomTextView etLoadingDescription;
    private CustomTextView tvWareHouseSpending;
    private CustomTextView etWareHouseSpending;
    private CustomTextView etWareDischargeTime;
    private CustomTextView etWareExpirationTime;
    private CustomTextView tvSettlementAfterArrived;
    private CustomTextView tvShipmentIsTransit;
    private CustomTextView tvHavingWellTent;
    private CustomTextView btnDrivers;

    private String shipmentId;

    private MyLoadingDetailsController controller;
    private GoogleMapHelper googleMapHelper;

    private boolean doGettingDataAgain = false;

    private boolean isBroadcastMyLoadingDetailsAgainRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loading_details);

        findViews();
        initComponents();
        setViewsListeners();
        getIntents();
        initMapView(savedInstanceState);
    }

    private void findViews() {
        scroll = findViewById(R.id.acMyLoadingDetails_scroll);
        tvCarType = findViewById(R.id.acMyLoadingDetails_tvCarType);
        btnBack = findViewById(R.id.acMyLoadingDetails_btnBack);
        ivLoadingPicture = findViewById(R.id.acMyLoadingDetails_ivLoadingPicture);
        btnEdit = findViewById(R.id.acMyLoadingDetails_btnEdit);
        btnDelete = findViewById(R.id.acMyLoadingDetails_btnDelete);
        etRegisterTime = findViewById(R.id.acMyLoadingDetails_etRegisterTime);
        etShipmentType = findViewById(R.id.acMyLoadingDetails_etShipmentType);
        etWeight = findViewById(R.id.acMyLoadingDetails_etWeight);
        etTruckType = findViewById(R.id.acMyLoadingDetails_etTruckType);
        etTruckLength = findViewById(R.id.acMyLoadingDetails_etTruckLength);
        etTruckWidth = findViewById(R.id.acMyLoadingDetails_etTruckWidth);
        tvTruckHeight = findViewById(R.id.acMyLoadingDetails_tvTruckHeight);
        etTruckHeight = findViewById(R.id.acMyLoadingDetails_etTruckHeight);
        etLoadingFareType = findViewById(R.id.acMyLoadingDetails_etLoadingFareType);
        tvLoadingFare = findViewById(R.id.acMyLoadingDetails_tvLoadingFare);
        etLoadingFare = findViewById(R.id.acMyLoadingDetails_etLoadingFare);
        etLoadingTime = findViewById(R.id.acMyLoadingDetails_etLoadingTime);
        mapView = findViewById(R.id.acMyLoadingDetails_mapView);

        rlAddress = findViewById(R.id.acMyLoadingDetails_rlAddress);
        rlAddress1 = findViewById(R.id.acMyLoadingDetails_rlAddress1);
        rlAddress2 = findViewById(R.id.acMyLoadingDetails_rlAddress2);
        tvOriginMarker = findViewById(R.id.acMyLoadingDetails_tvOriginMarker);
        tvDestinationMarker = findViewById(R.id.acMyLoadingDetails_tvDestinationMarker);
        tvOriginMarker1 = findViewById(R.id.acMyLoadingDetails_tvOriginMarker1);
        tvDestinationMarker1 = findViewById(R.id.acMyLoadingDetails_tvDestinationMarker1);
        tvOriginMarker2 = findViewById(R.id.acMyLoadingDetails_tvOriginMarker2);
        tvDestinationMarker2 = findViewById(R.id.acMyLoadingDetails_tvDestinationMarker2);

        etLoadingDescription = findViewById(R.id.acMyLoadingDetails_etLoadingDescription);
        tvWareHouseSpending = findViewById(R.id.acMyLoadingDetails_tvWareHouseSpending);
        etWareHouseSpending = findViewById(R.id.acMyLoadingDetails_etWareHouseSpending);
        etWareDischargeTime = findViewById(R.id.acMyLoadingDetails_etWareDischargeTime);
        etWareExpirationTime = findViewById(R.id.acMyLoadingDetails_etWareExpirationTime);
        tvSettlementAfterArrived = findViewById(R.id.acMyLoadingDetails_tvSettlementAfterArrived);
        tvShipmentIsTransit = findViewById(R.id.acMyLoadingDetails_tvShipmentIsTransit);
        tvHavingWellTent = findViewById(R.id.acMyLoadingDetails_tvHavingWellTent);
        btnDrivers = findViewById(R.id.acMyLoadingDetails_btnDrivers);
    }


    private void initComponents() {
        this.mapView.getMapAsync(this);
    }

    private void setViewsListeners() {
        btnBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (doGettingDataAgain) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                }

                finish();
            }
        });

        btnEdit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(MyLoadingDetailsActivity.this).gotoActivityEditShipment(controller.getShipmentData(), ShipmentEditTypeEnum.edit.getValue());
            }
        });

        btnDelete.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ConfirmDialog confirmDialog = new ConfirmDialog(MyLoadingDetailsActivity.this, getString(R.string.app_name), "آیا مایل به حذف این آیتم می باشید؟");
                confirmDialog.setOnClickListener(new ConfirmDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        controller.deleteLoadingFromServer(shipmentId);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                confirmDialog.show();
            }
        });

        btnDrivers.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(MyLoadingDetailsActivity.this).gotoActivityApplicantDrivers(shipmentId);
            }
        });
    }

    private void getIntents() {
        if (getIntent().getExtras() != null) {
            shipmentId = getIntent().getExtras().getString("shipmentId");
            controller = new MyLoadingDetailsController(MyLoadingDetailsActivity.this, this, shipmentId);
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

    @Override
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

    @Override
    public void initGoogleMapHelper(GoogleMap googleMap) {
        this.googleMapHelper = new GoogleMapHelper(MyLoadingDetailsActivity.this, googleMap);
        this.googleMapHelper.setZoomLevel(12f);
        this.googleMapHelper.showIranMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initGoogleMap(googleMap);
        initGoogleMapHelper(googleMap);

        mapView.post(new Runnable() {
            @Override
            public void run() {
                controller.getMyLoadingDetailsFomServer(shipmentId, mapView.getMeasuredWidth() / 2, mapView.getMeasuredHeight() / 2);
            }
        });
    }

    @Override
    public GoogleMapHelper getGoogleMapHelper() {
        return googleMapHelper;
    }

    @Override
    public CustomTextView getTvCarType() {
        return tvCarType;
    }

    public CustomTextView getBtnBack() {
        return btnBack;
    }

    @Override
    public ImageView getIvLoadingPicture() {
        return ivLoadingPicture;
    }

    @Override
    public CustomTextView getBtnEdit() {
        return btnEdit;
    }

    @Override
    public CustomTextView getBtnDelete() {
        return btnDelete;
    }

    @Override
    public CustomTextView getEtRegisterTime() {
        return etRegisterTime;
    }

    @Override
    public CustomTextView getEtShipmentType() {
        return etShipmentType;
    }

    @Override
    public CustomTextView getEtWeight() {
        return etWeight;
    }

    @Override
    public CustomTextView getEtTruckType() {
        return etTruckType;
    }

    @Override
    public CustomTextView getEtTruckLength() {
        return etTruckLength;
    }

    @Override
    public CustomTextView getEtTruckWidth() {
        return etTruckWidth;
    }

    @Override
    public CustomTextView getTvTruckHeight() {
        return tvTruckHeight;
    }

    @Override
    public CustomTextView getEtTruckHeight() {
        return etTruckHeight;
    }

    @Override
    public CustomTextView getEtLoadingFareType() {
        return etLoadingFareType;
    }

    @Override
    public CustomTextView getEtLoadingFare() {
        return etLoadingFare;
    }

    @Override
    public CustomTextView getTvLoadingFare() {
        return tvLoadingFare;
    }

    @Override
    public CustomTextView getEtLoadingTime() {
        return etLoadingTime;
    }

    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public RelativeLayout getRlAddress() {
        return rlAddress;
    }

    @Override
    public RelativeLayout getRlAddress1() {
        return rlAddress1;
    }

    @Override
    public RelativeLayout getRlAddress2() {
        return rlAddress2;
    }

    @Override
    public CustomTextView getTvOriginMarker() {
        return tvOriginMarker;
    }

    @Override
    public CustomTextView getTvDestinationMarker() {
        return tvDestinationMarker;
    }

    @Override
    public CustomTextView getTvOriginMarker1() {
        return tvOriginMarker1;
    }

    @Override
    public CustomTextView getTvDestinationMarker1() {
        return tvDestinationMarker1;
    }

    @Override
    public CustomTextView getTvOriginMarker2() {
        return tvOriginMarker2;
    }

    @Override
    public CustomTextView getTvDestinationMarker2() {
        return tvDestinationMarker2;
    }

    @Override
    public CustomTextView getEtLoadingDescription() {
        return etLoadingDescription;
    }

    @Override
    public CustomTextView getTvWareHouseSpending() {
        return tvWareHouseSpending;
    }

    @Override
    public CustomTextView getEtWareHouseSpending() {
        return etWareHouseSpending;
    }

    @Override
    public CustomTextView getEtWareDischargeTime() {
        return etWareDischargeTime;
    }

    @Override
    public CustomTextView getEtWareExpirationTime() {
        return etWareExpirationTime;
    }

    @Override
    public CustomTextView getTvSettlementAfterArrived() {
        return tvSettlementAfterArrived;
    }

    @Override
    public CustomTextView getTvShipmentIsTransit() {
        return tvShipmentIsTransit;
    }

    @Override
    public CustomTextView getTvHavingWellTent() {
        return tvHavingWellTent;
    }

    @Override
    public CustomTextView getBtnDrivers() {
        return btnDrivers;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        isBroadcastMyLoadingDetailsAgainRegistered = true;
        registerReceiver(rgMyLoadingDetailsAgain, new IntentFilter("com.noavaran.system.vira.baryab.REQUEST_LOADINGDETAIL"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (isBroadcastMyLoadingDetailsAgainRegistered) {
            isBroadcastMyLoadingDetailsAgainRegistered = false;
            unregisterReceiver(rgMyLoadingDetailsAgain);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        if (isBroadcastMyLoadingDetailsAgainRegistered) {
            isBroadcastMyLoadingDetailsAgainRegistered = false;
            unregisterReceiver(rgMyLoadingDetailsAgain);
        }
    }

    @Override
    public void onBackPressed() {
        if (doGettingDataAgain) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
        }

        super.onBackPressed();
    }

    BroadcastReceiver rgMyLoadingDetailsAgain =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                doGettingDataAgain = intent.getBooleanExtra("doGettingDataAgain", false);

                if (doGettingDataAgain)
                    controller.getMyLoadingDetailsFomServer(shipmentId, mapView.getMeasuredWidth() / 2, mapView.getMeasuredHeight() / 2);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
}