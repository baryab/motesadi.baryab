package com.noavaran.system.vira.baryab.activities.delegates;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.noavaran.system.vira.baryab.customviews.CustomEditText;
import com.noavaran.system.vira.baryab.customviews.CustomSwitchButton;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.customviews.ExpandableLayout;
import com.noavaran.system.vira.baryab.info.TruckTypeInfo;
import com.noavaran.system.vira.baryab.utils.okhttp.response.ShipmentDetailResponse;

public interface EditShipmentDelegate {
    interface View {
        CustomTextView getTvLocationHint();
        CustomTextView getTvCarChooseHint();
        CustomTextView getTvCarLengthHeaderLabel();
        CustomEditText getEtCarLengthFrom();
        CustomEditText getEtCarLengthTo();
        CustomTextView getTvCarWidthHeaderLabel();
        CustomEditText getEtCarWidthFrom();
        CustomEditText getEtCarWidthTo();
        ExpandableLayout getElCarHeight();
        CustomTextView getTvCarHeightHeaderLabel();
        CustomEditText getEtCarHeightFrom();
        CustomEditText getEtCarHeightTo();
        CustomEditText getEtLoadingType();
        CustomEditText getEtLoadingWeight();
        RecyclerView getLoadingFareTypeRecyclerView();
        CustomTextView getTvLoadingFareTypeHint();
        CustomEditText getEtLoadingFare();
        public void setLoadingFareViewVisiablity(int visiablity);
        CustomTextView getTvLoadingTimeHint();
        CustomEditText getEtّDescription();
        ImageView getIvLoadingPhotoPreview();
        CustomEditText getEtSpendingWarehouse();
        CustomTextView getTvDetermineDischargeTimeHint();
        CustomTextView getTvDetermineExpirationTimeHint();
        CustomSwitchButton getBtnPaymentIsPaidAfterLoadingArriveToDestination();
        CustomSwitchButton getBtnLoadingIsGoingRound();
        CustomSwitchButton getBtnHavingIntactTent();
        void setCarChooserHint(String carTypeFullname);
        ExpandableLayout getCarLengthView();
        void setCarLengthHeaderLabel(String text);
        ExpandableLayout getCarWidthView();
        void setCarWidthHeaderLabel(String text);
        ExpandableLayout getCarHeightView();
        void setCarHeightHeaderLabel(String text);
        void collapseAllExpandableLayout();
    }

    interface Controller {
        void setShipmentData(ShipmentDetailResponse shipmentData);
        ShipmentDetailResponse getShipmentData();
        void setType(int type);
        int getType();
        void setTruckTypeInfo(TruckTypeInfo truckTypeInfo);
        void save();
    }
}
