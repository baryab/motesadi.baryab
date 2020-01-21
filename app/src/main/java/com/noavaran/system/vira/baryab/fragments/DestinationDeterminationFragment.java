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
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.popupwindows.ChosenLocationsPopup;

public class DestinationDeterminationFragment extends BaseFragment {
    private View rootView;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    private CustomTextView btnAddNewPlace;
    private CustomTextView btnConfirm;
    private CustomTextView btnBack;
    private CustomTextView btnShowChosenPlace;
    private RelativeLayout rlContainer;

    private ChosenLocationsPopup chosenLocationsPopup;

    private MapController mapController;

    public DestinationDeterminationFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_destination_determination, container, false);
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
        this.rlContainer = (RelativeLayout) this.rootView.findViewById(R.id.frDestinationDetermination_rlContainer);
        this.btnConfirm = (CustomTextView) this.rootView.findViewById(R.id.frDestinationDetermination_btnConfirm);
        this.btnBack = (CustomTextView) this.rootView.findViewById(R.id.frDestinationDetermination_btnBack);
        this.btnAddNewPlace = (CustomTextView) this.rootView.findViewById(R.id.frDestinationDetermination_btnAddNewPlace);
        this.btnShowChosenPlace = (CustomTextView) this.rootView.findViewById(R.id.frDestinationDetermination_btnShowChosenPlace);
    }

    private void initComponents() {
        chosenLocationsPopup = new ChosenLocationsPopup(getActivity(), rlContainer, this.mapController, mapController.KEY_PLACE_TYPE_DESTINATION);
    }

    private void setViewsListeners() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenLocationsPopup.isShowning())
                    chosenLocationsPopup.dismissPopup();

                mapController.gotoPreviousFragment();
            }
        });

        this.btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
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
                if (mapController.getDestinationItemCount() == 0)
                    ((MapsActivity) getActivity()).showToastInfo("هیچ محل تخلیه بار جهت نمایش یافت نشد");
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
        if (mapController.getDestinationPlaces().size() == 0 && !chosenLocationsPopup.isShowning()) {
            mapController.gotoPreviousFragment();
        } else if (chosenLocationsPopup.isShowning()) {
            chosenLocationsPopup.dismissPopup();
        } else {
            int pos = mapController.getOriginItemCount() + mapController.getDestinationPlaces().size();
            LatLngInfo latLngInfo = mapController.getPlaces().get(pos - 1).getLatLngInfo();
            mapController.getPlaces().remove(pos - 1);
            mapController.onMapMarkersChanged(latLngInfo);
        }
    }
}
