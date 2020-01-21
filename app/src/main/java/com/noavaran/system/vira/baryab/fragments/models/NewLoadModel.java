package com.noavaran.system.vira.baryab.fragments.models;

import android.util.Base64;

import com.noavaran.system.vira.baryab.info.PlacesInfo;

import java.util.List;

public class NewLoadModel {
    private List<PlacesInfo> origins;
    private List<PlacesInfo> destinations;
    private int     carType;
    private float   lengthFrom;
    private float   lengthTo;
    private float   widthFrom;
    private float   widthTo;
    private float   heightFrom;
    private float   heightTo;
    private String  loadingType;
    private int     loadingWeight;
    private int     loadingFareType;
    private int     loadingFare;
    private String  description;
    private Base64  loadingPhoto;
    private String  warehouseSpending;
    private String  expireTime;
    private boolean isShipmentGoingRound;
    private boolean refundAfterArrival;
    private boolean havingWellTent;

    public NewLoadModel() {}

    public NewLoadModel(List<PlacesInfo> listOriginPlacesInfo, List<PlacesInfo> listDestinationPlacesInfo, int carType, float lengthFrom, float lengthTo, float widthFrom, float widthTo, float heightFrom, float heightTo, String loadingType, int loadingWeight, int loadingFareType, int loadingFare, String description, Base64 loadingPhoto, String warehouseSpending, String expireTime, boolean isShipmentGoingRound, boolean refundAfterArrival, boolean havingWellTent) {
        this.origins                  = listOriginPlacesInfo;
        this.destinations             = listDestinationPlacesInfo;
        this.carType                  = carType;
        this.lengthFrom               = lengthFrom;
        this.lengthTo                 = lengthTo;
        this.widthFrom                = widthFrom;
        this.widthTo                  = widthTo;
        this.heightFrom               = heightFrom;
        this.heightTo                 = heightTo;
        this.loadingType              = loadingType;
        this.loadingWeight            = loadingWeight;
        this.loadingFareType          = loadingFareType;
        this.loadingFare              = loadingFare;
        this.description              = description;
        this.loadingPhoto             = loadingPhoto;
        this.warehouseSpending        = warehouseSpending;
        this.expireTime               = expireTime;
        this.isShipmentGoingRound     = isShipmentGoingRound;
        this.refundAfterArrival       = refundAfterArrival;
        this.havingWellTent           = havingWellTent;
    }

    public List<PlacesInfo> getListOriginPlacesInfo() {
        return origins;
    }

    public void setListOriginPlacesInfo(List<PlacesInfo> listOriginPlacesInfo) {
        this.origins = listOriginPlacesInfo;
    }

    public List<PlacesInfo> getListDestinationPlacesInfo() {
        return destinations;
    }

    public void setListDestinationPlacesInfo(List<PlacesInfo> listDestinationPlacesInfo) {
        this.destinations = listDestinationPlacesInfo;
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
    }

    public float getLengthFrom() {
        return lengthFrom;
    }

    public void setLengthFrom(float lengthFrom) {
        this.lengthFrom = lengthFrom;
    }

    public float getLengthTo() {
        return lengthTo;
    }

    public void setLengthTo(float lengthTo) {
        this.lengthTo = lengthTo;
    }

    public float getWidthFrom() {
        return widthFrom;
    }

    public void setWidthFrom(float widthFrom) {
        this.widthFrom = widthFrom;
    }

    public float getWidthTo() {
        return widthTo;
    }

    public void setWidthTo(float widthTo) {
        this.widthTo = widthTo;
    }

    public float getHeightFrom() {
        return heightFrom;
    }

    public void setHeightFrom(float heightFrom) {
        this.heightFrom = heightFrom;
    }

    public float getHeightTo() {
        return heightTo;
    }

    public void setHeightTo(float heightTo) {
        this.heightTo = heightTo;
    }

    public String getLoadingType() {
        return loadingType;
    }

    public void setLoadingType(String loadingType) {
        this.loadingType = loadingType;
    }

    public int getLoadingWeight() {
        return loadingWeight;
    }

    public void setLoadingWeight(int loadingWeight) {
        this.loadingWeight = loadingWeight;
    }

    public int getLoadingFareType() {
        return loadingFareType;
    }

    public void setLoadingFareType(int loadingFareType) {
        this.loadingFareType = loadingFareType;
    }

    public int getLoadingFare() {
        return loadingFare;
    }

    public void setLoadingFare(int loadingFare) {
        this.loadingFare = loadingFare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Base64 getLoadingPhoto() {
        return loadingPhoto;
    }

    public void setLoadingPhoto(Base64 loadingPhoto) {
        this.loadingPhoto = loadingPhoto;
    }

    public String getWarehouseSpending() {
        return warehouseSpending;
    }

    public void setWarehouseSpending(String warehouseSpending) {
        this.warehouseSpending = warehouseSpending;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isShipmentGoingRound() {
        return isShipmentGoingRound;
    }

    public void setShipmentGoingRound(boolean shipmentGoingRound) {
        isShipmentGoingRound = shipmentGoingRound;
    }

    public boolean isRefundAfterArrival() {
        return refundAfterArrival;
    }

    public void setRefundAfterArrival(boolean refundAfterArrival) {
        this.refundAfterArrival = refundAfterArrival;
    }

    public boolean isHavingWellTent() {
        return havingWellTent;
    }

    public void setHavingWellTent(boolean havingWellTent) {
        this.havingWellTent = havingWellTent;
    }
}