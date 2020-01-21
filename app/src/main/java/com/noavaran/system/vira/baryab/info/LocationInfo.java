package com.noavaran.system.vira.baryab.info;

public class LocationInfo {
    private LatLngInfo latLngInfo;
    private GeocoderInfo geocoderInfo;

    public LocationInfo() {}

    public LocationInfo(LatLngInfo latLngInfo, GeocoderInfo geocoderInfo) {
        this.latLngInfo = latLngInfo;
        this.geocoderInfo = geocoderInfo;
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
}
