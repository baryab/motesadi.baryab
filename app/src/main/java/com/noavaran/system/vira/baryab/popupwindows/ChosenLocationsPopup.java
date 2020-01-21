package com.noavaran.system.vira.baryab.popupwindows;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.delegates.MapDelegate;
import com.noavaran.system.vira.baryab.adapters.LocationsAdapter;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.info.PlacesInfo;

import java.util.ArrayList;
import java.util.List;

public class ChosenLocationsPopup implements MapDelegate.ChosenLocationsPopup {
    private Activity activity;
    private View container;
    private MapDelegate.Controller controller;
    private int placeType;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View views;

    private CustomTextView btnDismiss;
    private RecyclerView rvChosenLocations;
    private LocationsAdapter locationsAdapter;

    private List<PlacesInfo> listPlacesInfo;

    public ChosenLocationsPopup(Activity activity, View container, MapDelegate.Controller controller, int placeType) {
        this.activity = activity;
        this.container = container;
        this.controller = controller;
        this.placeType = placeType;

        this.init();
        this.findViews();
        this.initComponents();
        this.initRecycleViewChosenLocations();
        this.setViewsListeners();
    }

    private void init() {
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = inflater.inflate(R.layout.popup_chosen_locations, null);
        popupWindow = new PopupWindow(views, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.Animation);

        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
    }

    private void findViews() {
        rvChosenLocations = (RecyclerView) views.findViewById(R.id.puChosenLocation_rvChosenLocations);
        btnDismiss = (CustomTextView) views.findViewById(R.id.puChosenLocations_btnDismiss);
    }

    private void initComponents() {
        listPlacesInfo = new ArrayList<>();
    }

    private void initRecycleViewChosenLocations() {
        this.rvChosenLocations.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rvChosenLocations.setLayoutManager(mLayoutManager);
        this.rvChosenLocations.setItemAnimator(new DefaultItemAnimator());

        this.locationsAdapter = new LocationsAdapter(activity, controller, this, listPlacesInfo, placeType);
        this.rvChosenLocations.setAdapter(this.locationsAdapter);
    }

    private void setViewsListeners() {
        btnDismiss.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dismissPopup();
            }
        });
    }

    public void showPopup() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showPlaces();
                popupWindow.showAtLocation(container, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    public void dismissPopup() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChosenLocationsPopup.this.popupWindow.dismiss();
            }
        });

    }

    public boolean isShowning() {
        return popupWindow.isShowing();
    }

    /**
     * First, system clear list and notify adapter and then it start to fill up list of place
     * according to list of controller
     */
    private void showPlaces() {
        listPlacesInfo.clear();
        locationsAdapter.notifyDataSetChanged();

        for (PlacesInfo placesInfo : controller.getPlaces()) {
            if (placesInfo.getPlaceType() == this.placeType)
            listPlacesInfo.add(placesInfo);
        }
        locationsAdapter.notifyDataSetChanged();
    }

    @Override
    public void hidePopup() {
        dismissPopup();
    }

    @Override
    public void disableButtonDismiss() {
        btnDismiss.setEnabled(false);
    }

    @Override
    public void enableButtonDismiss() {
        btnDismiss.setEnabled(true);
    }

    @Override
    public void notifyDataSetChanged() {
        locationsAdapter.refresh();
    }
}