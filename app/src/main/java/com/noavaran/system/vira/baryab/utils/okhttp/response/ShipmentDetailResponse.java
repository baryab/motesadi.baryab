package com.noavaran.system.vira.baryab.utils.okhttp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.noavaran.system.vira.baryab.info.PlacesInfo;

import java.util.List;

public class ShipmentDetailResponse {
    private String DraftId;
    private String CreateDate;
    private float TruckMinLength;
    private float TruckMaxLength;
    private float TruckMinWidth;
    private float TruckMaxWidth;
    private float TruckMinHeight;
    private float TruckMaxHeight;
    private float Weight;
    private int Money;
    private String ExpireDate;
    private String loadingDate;
    private String DischargeDate;
    private long loadDate;
    private long expDate;
    private long disChDate;
    private boolean HasTent;
    private boolean Goback;
    private boolean AfterRecive;
    private int StoreCost;
    private String PicPath;
    private String PIcthumb;
    private String Desc;
    private String ShipmentType;
    private int MoneyTypeId;
    private int TruckTypeId;
    private List<PlacesInfo> listDestinationPlacesInfo;
    private List<PlacesInfo> listOriginPlacesInfo;

    @JsonProperty("DraftId")
    public String getDraftId() {
        return DraftId;
    }

    public void setDraftId(String draftId) {
        DraftId = draftId;
    }

    @JsonProperty("CreateDate")
    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    @JsonProperty("TruckMinLength")
    public float getTruckMinLength() {
        return TruckMinLength;
    }

    public void setTruckMinLength(float truckMinLength) {
        TruckMinLength = truckMinLength;
    }

    @JsonProperty("TruckMaxLength")
    public float getTruckMaxLength() {
        return TruckMaxLength;
    }

    public void setTruckMaxLength(float truckMaxLength) {
        TruckMaxLength = truckMaxLength;
    }

    @JsonProperty("TruckMinWidth")
    public float getTruckMinWidth() {
        return TruckMinWidth;
    }

    public void setTruckMinWidth(float truckMinWidth) {
        TruckMinWidth = truckMinWidth;
    }

    @JsonProperty("TruckMaxWidth")
    public float getTruckMaxWidth() {
        return TruckMaxWidth;
    }

    public void setTruckMaxWidth(float truckMaxWidth) {
        TruckMaxWidth = truckMaxWidth;
    }

    @JsonProperty("TruckMinHeight")
    public float getTruckMinHeight() {
        return TruckMinHeight;
    }

    public void setTruckMinHeight(float truckMinHeight) {
        TruckMinHeight = truckMinHeight;
    }

    @JsonProperty("TruckMaxHeight")
    public float getTruckMaxHeight() {
        return TruckMaxHeight;
    }

    public void setTruckMaxHeight(float truckMaxHeight) {
        TruckMaxHeight = truckMaxHeight;
    }

    @JsonProperty("Weight")
    public float getWeight() {
        return Weight;
    }

    public void setWeight(float weight) {
        Weight = weight;
    }

    @JsonProperty("Money")
    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
    }

    @JsonProperty("ExpireDate")
    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    @JsonProperty("loadingDate")
    public String getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }

    @JsonProperty("DischargeDate")
    public String getDischargeDate() {
        return DischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        DischargeDate = dischargeDate;
    }

    @JsonProperty("loadDate")
    public long getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(long loadDate) {
        this.loadDate = loadDate;
    }

    @JsonProperty("expDate")
    public long getExpDate() {
        return expDate;
    }

    public void setExpDate(long expDate) {
        this.expDate = expDate;
    }

    @JsonProperty("disChDate")
    public long getDisChDate() {
        return disChDate;
    }

    public void setDisChDate(long disChDate) {
        this.disChDate = disChDate;
    }

    @JsonProperty("HasTent")
    public boolean isHasTent() {
        return HasTent;
    }

    public void setHasTent(boolean hasTent) {
        HasTent = hasTent;
    }

    @JsonProperty("Goback")
    public boolean isGoback() {
        return Goback;
    }

    public void setGoback(boolean goback) {
        Goback = goback;
    }

    @JsonProperty("AfterRecive")
    public boolean isAfterRecive() {
        return AfterRecive;
    }

    public void setAfterRecive(boolean afterRecive) {
        AfterRecive = afterRecive;
    }

    @JsonProperty("StoreCost")
    public int getStoreCost() {
        return StoreCost;
    }

    public void setStoreCost(int storeCost) {
        StoreCost = storeCost;
    }

    @JsonProperty("PicPath")
    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    @JsonProperty("PIcthumb")
    public String getPIcthumb() {
        return PIcthumb;
    }

    public void setPIcthumb(String PIcthumb) {
        this.PIcthumb = PIcthumb;
    }

    @JsonProperty("Desc")
    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    @JsonProperty("ShipmentType")
    public String getShipmentType() {
        return ShipmentType;
    }

    public void setShipmentType(String shipmentType) {
        ShipmentType = shipmentType;
    }

    @JsonProperty("MoneyTypeId")
    public int getMoneyTypeId() {
        return MoneyTypeId;
    }

    public void setMoneyTypeId(int moneyTypeId) {
        MoneyTypeId = moneyTypeId;
    }

    @JsonProperty("TruckTypeId")
    public int getTruckTypeId() {
        return TruckTypeId;
    }

    public void setTruckTypeId(int truckTypeId) {
        TruckTypeId = truckTypeId;
    }

    @JsonProperty("listDestinationPlacesInfo")
    public List<PlacesInfo> getListDestinationPlacesInfo() {
        return listDestinationPlacesInfo;
    }

    public void setListDestinationPlacesInfo(List<PlacesInfo> listDestinationPlacesInfo) {
        this.listDestinationPlacesInfo = listDestinationPlacesInfo;
    }

    @JsonProperty("listOriginPlacesInfo")
    public List<PlacesInfo> getListOriginPlacesInfo() {
        return listOriginPlacesInfo;
    }

    public void setListOriginPlacesInfo(List<PlacesInfo> listOriginPlacesInfo) {
        this.listOriginPlacesInfo = listOriginPlacesInfo;
    }
}