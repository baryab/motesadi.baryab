package com.noavaran.system.vira.baryab.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.noavaran.system.vira.baryab.BaseFragment;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.MapsActivity;
import com.noavaran.system.vira.baryab.activities.controllers.MapController;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.popupwindows.ChosenLocationsPopup;

public class OriginDeterminationFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private RelativeLayout rlContainer;
    private CustomTextView btnAddNewPlace;
    private CustomTextView btnConfirm;
    private CustomTextView btnShowChosenPlace;

    private ChosenLocationsPopup chosenLocationsPopup;

    private MapController mapController;

    public OriginDeterminationFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_origin_determination, container, false);
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;

        return this.rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        initComponents();
        setViewsListeners();
    }

    private void findViews() {
        this.rlContainer = (RelativeLayout) this.rootView.findViewById(R.id.frOriginDetermination_rlContainer);
        this.btnConfirm = (CustomTextView) this.rootView.findViewById(R.id.frOriginDetermination_btnConfirm);
        this.btnAddNewPlace = (CustomTextView) this.rootView.findViewById(R.id.frOriginDetermination_btnAddNewPlace);
        this.btnShowChosenPlace = (CustomTextView) this.rootView.findViewById(R.id.frOriginDetermination_btnShowChosenPlace);
    }

    private void initComponents() {
        chosenLocationsPopup = new ChosenLocationsPopup(getActivity(), rlContainer, this.mapController, mapController.KEY_PLACE_TYPE_ORIGIN);
    }

    private void setViewsListeners() {
        this.btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (chosenLocationsPopup.isShowning())
                    chosenLocationsPopup.dismissPopup();

                mapController.onConfirm();
            }
        });

        this.btnAddNewPlace.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mapController.onAddNewMarker();
            }
        });

        this.btnShowChosenPlace.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (mapController.getOriginItemCount() == 0)
                    ((MapsActivity) getActivity()).showToastInfo("هیچ محل بارگیری جهت نمایش یافت نشد");
                else
                    chosenLocationsPopup.showPopup();
            }
        });
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        if ((mapController.getOriginPlaces().size() == 0 && mapController.getDestinationPlaces().size() == 0) && !chosenLocationsPopup.isShowning()) {
            getActivity().finish();
        } else if (chosenLocationsPopup.isShowning()) {
            chosenLocationsPopup.dismissPopup();
        } else if (mapController.getOriginPlaces().size() > 0) {
            int pos = mapController.getOriginPlaces().size();
            LatLngInfo latLngInfo = mapController.getPlaces().get(pos - 1).getLatLngInfo();
            mapController.getPlaces().remove(pos - 1);
            mapController.onMapMarkersChanged(latLngInfo);
        } else if (mapController.getDestinationPlaces().size() > 0) {
            MessageDialog messageDialog = new MessageDialog(getActivity(), getResources().getString(R.string.app_name), "شما در حال حاضر چند محل تخلیه بار مشخص نموده اید، امکان خروج از صفحه تعیین محل بارگیری و تخلیه در حال حاضر نمی باشد");
            messageDialog.show();
        }
    }
}