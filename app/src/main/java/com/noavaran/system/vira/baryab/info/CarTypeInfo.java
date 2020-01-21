package com.noavaran.system.vira.baryab.info;

public class CarTypeInfo {
    private String model;
    private boolean hasMoreDetails;

    public CarTypeInfo(String model, boolean hasMoreDetails) {
        this.model = model;
        this.hasMoreDetails = hasMoreDetails;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean hasMoreDetails() {
        return hasMoreDetails;
    }

    public void setHasMoreDetails(boolean hasMoreDetails) {
        this.hasMoreDetails = hasMoreDetails;
    }
}