package com.noavaran.system.vira.baryab.info;

public class ApplicantDriversInfo {
    private String driverId;
    private String shipmentId;
    private String driverName;
    private String driverPhoto;
    private int rate;
    private String phoneNumber;
    private String truckType;
    private float length;
    private float width;
    private float height;
    private String licensePlate;
    private String requestDate;

    public ApplicantDriversInfo() {}

    public ApplicantDriversInfo(String driverId, String shipmentId, String driverName, String driverPhoto, int rate, String phoneNumber, String truckType, float length, float width, float height, String licensePlate, String requestDate) {
        this.driverId = driverId;
        this.shipmentId = shipmentId;
        this.driverName = driverName;
        this.driverPhoto = driverPhoto;
        this.rate = rate;
        this.phoneNumber = phoneNumber;
        this.truckType = truckType;
        this.length = length;
        this.width = width;
        this.height = height;
        this.licensePlate = licensePlate;
        this.requestDate = requestDate;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}