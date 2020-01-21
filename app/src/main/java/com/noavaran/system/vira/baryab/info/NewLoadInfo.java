package com.noavaran.system.vira.baryab.info;

import java.util.List;

public class NewLoadInfo {
    private String DraftId;
    private List<PlacesInfo> listOriginPlacesInfo;
    private List<PlacesInfo> listDestinationPlacesInfo;
    private int TruckTypeId;
    private float TruckMinLength;
    private float TruckMaxLength;
    private float TruckMinWidth;
    private float TruckMaxWidth;
    private float TruckMinHeight;
    private float TruckMaxHeight;
    private String ShipmentType;
    private float Weight;
    private int MoneyTypeId;
    private int Money;
    private long LoadDate;
    private String Desc;
    private String Pic;
    private int StoreCost;
    private long disChDate;
    private long expDate;
    private boolean IsGoback;
    private boolean IsAfterRecive;
    private boolean HasTent;

    public NewLoadInfo() {}

    public NewLoadInfo(String DraftId, List<PlacesInfo> listOriginPlacesInfo, List<PlacesInfo> listDestinationPlacesInfo, int TruckTypeId, float TruckMinLength, float TruckMaxLength, float TruckMinWidth, float TruckMaxWidth, float TruckMinHeight, float TruckMaxHeight, String ShipmentType, float Weight, int MoneyTypeId, int Money, long LoadDate, String Desc, String Pic, int StoreCost, long disChDate, long expDate, boolean IsGoback, boolean IsAfterRecive, boolean HasTent) {
        this.DraftId = DraftId;
        this.listOriginPlacesInfo = listOriginPlacesInfo;
        this.listDestinationPlacesInfo = listDestinationPlacesInfo;
        this.TruckTypeId = TruckTypeId;
        this.TruckMinLength = TruckMinLength;
        this.TruckMaxLength = TruckMaxLength;
        this.TruckMinWidth = TruckMinWidth;
        this.TruckMaxWidth = TruckMaxWidth;
        this.TruckMinHeight = TruckMinHeight;
        this.TruckMaxHeight = TruckMaxHeight;
        this.ShipmentType = ShipmentType;
        this.Weight = Weight;
        this.MoneyTypeId = MoneyTypeId;
        this.Money = Money;
        this.LoadDate = LoadDate;
        this.Desc = Desc;
        this.Pic = Pic;
        this.StoreCost = StoreCost;
        this.disChDate = disChDate;
        this.expDate = expDate;
        this.IsGoback = IsGoback;
        this.IsAfterRecive = IsAfterRecive;
        this.HasTent = HasTent;
    }

    public String getDraftId() {
        return DraftId;
    }

    public void setDraftId(String draftId) {
        DraftId = draftId;
    }

    public List<PlacesInfo> getListOriginPlacesInfo() {
        return listOriginPlacesInfo;
    }

    public void setListOriginPlacesInfo(List<PlacesInfo> listOriginPlacesInfo) {
        this.listOriginPlacesInfo = listOriginPlacesInfo;
    }

    public List<PlacesInfo> getListDestinationPlacesInfo() {
        return listDestinationPlacesInfo;
    }

    public void setListDestinationPlacesInfo(List<PlacesInfo> listDestinationPlacesInfo) {
        this.listDestinationPlacesInfo = listDestinationPlacesInfo;
    }

    public int getTruckTypeId() {
        return TruckTypeId;
    }

    public void setTruckTypeId(int truckTypeId) {
        TruckTypeId = truckTypeId;
    }

    public float getTruckMinLength() {
        return TruckMinLength;
    }

    public void setTruckMinLength(float truckMinLength) {
        TruckMinLength = truckMinLength;
    }

    public float getTruckMaxLength() {
        return TruckMaxLength;
    }

    public void setTruckMaxLength(float truckMaxLength) {
        TruckMaxLength = truckMaxLength;
    }

    public float getTruckMinWidth() {
        return TruckMinWidth;
    }

    public void setTruckMinWidth(float truckMinWidth) {
        TruckMinWidth = truckMinWidth;
    }

    public float getTruckMaxWidth() {
        return TruckMaxWidth;
    }

    public void setTruckMaxWidth(float truckMaxWidth) {
        TruckMaxWidth = truckMaxWidth;
    }

    public float getTruckMinHeight() {
        return TruckMinHeight;
    }

    public void setTruckMinHeight(float truckMinHeight) {
        TruckMinHeight = truckMinHeight;
    }

    public float getTruckMaxHeight() {
        return TruckMaxHeight;
    }

    public void setTruckMaxHeight(float truckMaxHeight) {
        TruckMaxHeight = truckMaxHeight;
    }

    public String getShipmentType() {
        return ShipmentType;
    }

    public void setShipmentType(String shipmentType) {
        ShipmentType = shipmentType;
    }

    public float getWeight() {
        return Weight;
    }

    public void setWeight(float weight) {
        Weight = weight;
    }

    public int getMoneyTypeId() {
        return MoneyTypeId;
    }

    public void setMoneyTypeId(int moneyTypeId) {
        MoneyTypeId = moneyTypeId;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int money) {
        Money = money;
    }

    public long getLoadDate() {
        return LoadDate;
    }

    public void setLoadDate(long loadingDate) {
        LoadDate = loadingDate;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public int getStoreCost() {
        return StoreCost;
    }

    public void setStoreCost(int storeCost) {
        StoreCost = storeCost;
    }

    public boolean isGoback() {
        return IsGoback;
    }

    public void setIsGoback(boolean IsGoBack) {
        IsGoback = IsGoBack;
    }

    public boolean isAfterRecive() {
        return IsAfterRecive;
    }

    public void setIsAfterRecive(boolean IsAfterRecive) {
        IsAfterRecive = IsAfterRecive;
    }

    public boolean isHasTent() {
        return HasTent;
    }

    public void setHasTent(boolean hasTent) {
        HasTent = hasTent;
    }

    public long getDisChDate() {
        return disChDate;
    }

    public void setDisChDate(long disChDate) {
        this.disChDate = disChDate;
    }

    public long getExpDate() {
        return expDate;
    }

    public void setExpDate(long expDate) {
        this.expDate = expDate;
    }
}
