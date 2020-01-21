package com.noavaran.system.vira.baryab.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.controllers.MapController;
import com.noavaran.system.vira.baryab.activities.delegates.MapDelegate;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.info.PlacesInfo;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationHolder> {
    private Context context;
    private MapDelegate.Controller controller;
    private MapDelegate.ChosenLocationsPopup chosenLocationsPopup;
    private List<PlacesInfo> listPlacesInfo;
    private int placeType;
    private PlacesInfo placesInfo;
    private View itemView;

    public LocationsAdapter(Context context, MapDelegate.Controller controller, MapDelegate.ChosenLocationsPopup chosenLocationsPopup, List<PlacesInfo> listPlacesInfo, int placeType) {
        this.context = context;
        this.controller = controller;
        this.chosenLocationsPopup = chosenLocationsPopup;
        this.listPlacesInfo = listPlacesInfo;
        this.placeType = placeType;
    }

    @Override
    public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_locations, parent, false);

        return new LocationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationHolder holder, int position) {
        this.placesInfo = this.listPlacesInfo.get(position);

        holder.tvPlaceName.setText(placesInfo.getAddressInfo().getProvince() + ", " + placesInfo.getAddressInfo().getCity());

        if (placesInfo.getPlaceType() == MapController.KEY_PLACE_TYPE_ORIGIN)
            holder.tvPlaceType.setTextColor(Color.parseColor("#1b5e20"));
        else {
            holder.tvPlaceType.setTextColor(Color.parseColor("#b71c1c"));
        }
    }

    @Override
    public int getItemCount() {
        return listPlacesInfo.size();
    }

    public void refresh() {
        this.listPlacesInfo.clear();
        notifyDataSetChanged();

        for (int i = 0; i < controller.getPlaces().size(); i++) {
            if (controller.getPlaces().get(i).getPlaceType() == placeType)
                this.listPlacesInfo.add(controller.getPlaces().get(i));
        }

        notifyDataSetChanged();
    }

    public class LocationHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvPlaceType;
        public CustomTextView tvPlaceName;
        public CustomTextView btnEdit;
        public CustomTextView btnDelete;

        public LocationHolder(View view) {
            super(view);

            tvPlaceType = (CustomTextView) view.findViewById(R.id.rlLocations_tvPlaceType);
            tvPlaceName = (CustomTextView) view.findViewById(R.id.rlLocations_tvPlaceName);
            btnEdit = (CustomTextView) view.findViewById(R.id.rlLocations_btnEdit);
            btnDelete = (CustomTextView) view.findViewById(R.id.rlLocations_btnDelete);

            btnEdit.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    controller.changeMarkerPosition(chosenLocationsPopup, listPlacesInfo.get(getAdapterPosition()).getPlaceType(), getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (placeType == MapController.KEY_PLACE_TYPE_ORIGIN) {
                        controller.removePlace(placeType, getAdapterPosition());

                        if (getItemCount() <= 0)
                            chosenLocationsPopup.hidePopup();
                    } else if (placeType == MapController.KEY_PLACE_TYPE_DESTINATION) {
                        controller.removePlace(placeType, controller.getOriginItemCount() + getAdapterPosition());

                        if (getItemCount() <= 0)
                            chosenLocationsPopup.hidePopup();
                    }

                    refresh();
                }
            });
        }
    }
}