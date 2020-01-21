package com.noavaran.system.vira.baryab.activities.delegates;

import androidx.fragment.app.Fragment;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.noavaran.system.vira.baryab.activities.controllers.MapController;
import com.noavaran.system.vira.baryab.helpers.GoogleMapHelper;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.info.PlacesInfo;

import java.util.List;

public interface MapDelegate {
    interface View {
        void setSearchPlacesAdapter(SimpleAdapter adapter);

        void onAddNewMarker();

        void onMapMarkersChanged(LatLngInfo lastLatLngInfo);

        void onConfirm();

        void gotoPreviousFragment();

        ImageView getImagePlaceChooserMarker();

        void showAddNewLocationPin();

        void hideAddNewLocationPin();
    }

    interface ChosenLocationsPopup {
        void hidePopup();

        void disableButtonDismiss();

        void enableButtonDismiss();

        void notifyDataSetChanged();
    }

    interface Controller {
        void setCurrentFragment(Fragment currentFragment);

        Fragment getCurrentFragment();

        void gotoPreviousFragment();

        void setGoogleMapHelper(GoogleMapHelper googleMapHelper);

        void addNewPlace(GoogleMap googleMap, int placeType, LatLngInfo latLngInfo, MapController.OnGeoListener geoListener);

        void onMapMarkersChanged(LatLngInfo lastLatLngInfo);

        List<PlacesInfo> getPlaces();

        List<PlacesInfo> getOriginPlaces();

        List<PlacesInfo> getDestinationPlaces();

        int getOriginItemCount();

        int getDestinationItemCount();

        void changeMarkerPosition(ChosenLocationsPopup chosenLocationsPopup, int placeType, int position);

        void removePlace(int placeType, int position);

        void onAddNewMarker();

        void onConfirm();

        void executeSearchPlace(CharSequence s);
    }
}
