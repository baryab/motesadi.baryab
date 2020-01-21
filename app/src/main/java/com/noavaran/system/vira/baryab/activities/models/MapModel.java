package com.noavaran.system.vira.baryab.activities.models;

import com.noavaran.system.vira.baryab.info.PlacesInfo;

import java.util.ArrayList;
import java.util.List;

public class MapModel {
    private List<PlacesInfo> listPlacesInfo;

    public MapModel() {
        this.listPlacesInfo = new ArrayList<>();
    }

    public void addNewPlace(PlacesInfo placesInfo) {
        listPlacesInfo.add(placesInfo);
    }

    public List<PlacesInfo> getPlaces() {
        return listPlacesInfo;
    }
}
