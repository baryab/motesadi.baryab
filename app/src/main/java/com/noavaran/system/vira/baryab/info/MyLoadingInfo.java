package com.noavaran.system.vira.baryab.info;

public class MyLoadingInfo {
    private String id;
    private String moneyType;
    private String truckType;
    private String mabdaName;
    private String maghsadName;
    private String shipmentType;
    private String pic;
    private int driverCount;
    private int money;
    private String loadDate;

    public MyLoadingInfo() {}

    public MyLoadingInfo(String id, String moneyType, String truckType, String mabdaName, String maghsadName, String shipmentType, String pic, int driverCount, int money, String loadDate) {
        this.id = id;
        this.moneyType = moneyType;
        this.truckType = truckType;
        this.mabdaName = mabdaName;
        this.maghsadName = maghsadName;
        this.shipmentType = shipmentType;
        this.pic = pic;
        this.driverCount = driverCount;
        this.money = money;
        this.loadDate = loadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public String getMabdaName() {
        return mabdaName;
    }

    public void setMabdaName(String mabdaName) {
        this.mabdaName = mabdaName;
    }

    public String getMaghsadName() {
        return maghsadName;
    }

    public void setMaghsadName(String maghsadName) {
        this.maghsadName = maghsadName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getDriverCount() {
        return driverCount;
    }

    public void setDriverCount(int driverCount) {
        this.driverCount = driverCount;
    }

    public String getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }
}
