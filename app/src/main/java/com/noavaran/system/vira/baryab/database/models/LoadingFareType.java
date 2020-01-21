package com.noavaran.system.vira.baryab.database.models;

import com.orm.SugarRecord;

public class LoadingFareType extends SugarRecord {
    private int loadingFareTypeId;
    private String name;

    public LoadingFareType() {
    }

    public LoadingFareType(int id, String name) {
        super();

        this.loadingFareTypeId = id;
        this.name = name;
    }

    public int getLoadingFareTypeId() {
        return loadingFareTypeId;
    }

    public void setLoadingFareTypeId(int loadingFareTypeId) {
        this.loadingFareTypeId = loadingFareTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}