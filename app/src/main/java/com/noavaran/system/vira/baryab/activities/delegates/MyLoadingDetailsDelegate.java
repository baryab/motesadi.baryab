package com.noavaran.system.vira.baryab.activities.delegates;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.helpers.GoogleMapHelper;

public interface MyLoadingDetailsDelegate {
    interface View {
        void initGoogleMap(GoogleMap googleMap);
        void initGoogleMapHelper(GoogleMap googleMap);
        GoogleMapHelper getGoogleMapHelper();
        CustomTextView getTvCarType();
        ImageView getIvLoadingPicture();
        CustomTextView getEtRegisterTime();
        CustomTextView getEtShipmentType();
        CustomTextView getEtWeight();
        CustomTextView getEtTruckType();
        CustomTextView getEtTruckLength();
        CustomTextView getEtTruckWidth();
        CustomTextView getEtTruckHeight();
        CustomTextView getTvTruckHeight();
        CustomTextView getEtLoadingFareType();
        CustomTextView getEtLoadingFare();
        CustomTextView getTvLoadingFare();
        CustomTextView getEtLoadingTime();
        MapView getMapView();
        RelativeLayout getRlAddress();
        RelativeLayout getRlAddress1();
        RelativeLayout getRlAddress2();
        CustomTextView getTvOriginMarker();
        CustomTextView getTvDestinationMarker();
        CustomTextView getTvOriginMarker1();
        CustomTextView getTvDestinationMarker1();
        CustomTextView getTvOriginMarker2();
        CustomTextView getTvDestinationMarker2();
        CustomTextView getEtLoadingDescription();
        CustomTextView getTvWareHouseSpending();
        CustomTextView getEtWareHouseSpending();
        CustomTextView getEtWareDischargeTime();
        CustomTextView getEtWareExpirationTime();
        CustomTextView getTvSettlementAfterArrived();
        CustomTextView getTvShipmentIsTransit();
        CustomTextView getTvHavingWellTent();
        CustomTextView getBtnEdit();
        CustomTextView getBtnDelete();
        CustomTextView getBtnDrivers();
    }

    interface Controller {
        void deleteLoadingFromServer(String shipmentId);
        void getMyLoadingDetailsFomServer(String shipmentId, final int mapViewWidth, final int mapViewHeight);
        String getShipmentData();
    }
}