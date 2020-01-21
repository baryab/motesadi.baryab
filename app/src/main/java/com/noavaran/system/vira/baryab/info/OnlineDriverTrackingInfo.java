package com.noavaran.system.vira.baryab.info;

public class OnlineDriverTrackingInfo {
    private String contactNumber;
    private String shipmentType;
    private String driverPhoto;
    private String driverName;
    private String truckType;
    private String truckDetails;

    public OnlineDriverTrackingInfo() {}

    public OnlineDriverTrackingInfo(String contactNumber, String shipmentType, String driverPhoto, String driverName, String truckType, String truckDetails) {
        this.contactNumber = contactNumber;
        this.shipmentType = shipmentType;
        this.driverPhoto = driverPhoto;
        this.driverName = driverName;
        this.truckType = truckType;
        this.truckDetails = truckDetails;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public String getTruckDetails() {
        return truckDetails;
    }

    public void setTruckDetails(String truckDetails) {
        this.truckDetails = truckDetails;
    }
}
