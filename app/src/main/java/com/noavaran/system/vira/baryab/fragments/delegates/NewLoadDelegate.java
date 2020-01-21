package com.noavaran.system.vira.baryab.fragments.delegates;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.noavaran.system.vira.baryab.customviews.CustomEditText;
import com.noavaran.system.vira.baryab.customviews.CustomSwitchButton;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.customviews.ExpandableLayout;

public interface NewLoadDelegate {
    interface View {
        ScrollView getSvContainer();

        void setLocationHint(String location);

        void setCarChooserHint(String carTypeFullname);

        CustomTextView getLocationHint();

        CustomTextView getCarChooseHint();

        ExpandableLayout getCarLengthView();

        ExpandableLayout getCarWidthView();

        ExpandableLayout getCarHeightView();

        CustomEditText getEditTextCarLengthFrom();

        CustomEditText getEditTextCarLengthTo();

        CustomEditText getEditTextCarWidthFrom();

        CustomEditText getEditTextCarWidthTo();

        CustomEditText getEditTextCarHeightFrom();

        CustomEditText getEditTextCarHeightTo();

        CustomEditText getEditTextLoadingType();

        CustomEditText getEditTextLoadingWeight();

        CustomEditText getEditTextLoadingFare();

        CustomTextView getEditTextLoadingTimeHint();

        CustomEditText getEditTextDescription();

        ImageView getImagViewLoadingPhotoPreview();

        CustomEditText getEditTextSpendingWarehouse();

        CustomTextView getTextViewDetermineDischargeTimeHint();

        CustomTextView getTextViewDetermineExpirationTimeHint();

        CustomSwitchButton getSwithchButtonPaymentIsPaidAfterLoadingArriveToDestination();

        CustomSwitchButton getSwithchButtonLoadingIsGoingRound();

        CustomSwitchButton getSwithchButtonHavingIntactTent();

        void setCarLengthHeaderLabel(String text);

        void setCarWidthHeaderLabel(String text);

        void setCarHeightHeaderLabel(String text);

        void hideLoadingFareView();

        void showLoadingFareView();

        RecyclerView getLoadingFareTypeRecyclerView();

        CustomTextView getTextViewLoadingFareTypeHint();

        void collapseAllExpandableLayout();

        void removeCroppedImage();
    }

    interface Controller {
        void save();
    }
}