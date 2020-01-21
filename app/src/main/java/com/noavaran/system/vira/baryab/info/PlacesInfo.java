package com.noavaran.system.vira.baryab.info;

import androidx.annotation.NonNull;

public class PlacesInfo implements Comparable {
    private int placeType;
    private LatLngInfo latLngInfo;
    private GeocoderInfo geocoderInfo;
    private AddressInfo addressInfo;

    public PlacesInfo() {
    }

    public PlacesInfo(int placeType, LatLngInfo latLngInfo, GeocoderInfo geocoderInfo, AddressInfo addressInfo) {
        this.placeType = placeType;
        this.latLngInfo = latLngInfo;
        this.geocoderInfo = geocoderInfo;
        this.addressInfo = addressInfo;
    }

    public int getPlaceType() {
        return placeType;
    }

    public void setPlaceType(int placeType) {
        this.placeType = placeType;
    }

    public LatLngInfo getLatLngInfo() {
        return latLngInfo;
    }

    public void setLatLngInfo(LatLngInfo latLngInfo) {
        this.latLngInfo = latLngInfo;
    }

    public GeocoderInfo getGeocoderInfo() {
        return geocoderInfo;
    }

    public void setGeocoderInfo(GeocoderInfo geocoderInfo) {
        this.geocoderInfo = geocoderInfo;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compareType = ((PlacesInfo) o).getPlaceType();

        return this.placeType - compareType;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }
}
