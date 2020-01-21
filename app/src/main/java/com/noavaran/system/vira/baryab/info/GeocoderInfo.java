package com.noavaran.system.vira.baryab.info;

public class GeocoderInfo {
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;

    public GeocoderInfo() {}

    public GeocoderInfo(String address, String city, String state, String country, String postalCode, String knownName) {
        this.setAddress(address);
        this.setCity(city);
        this.setState(state);
        this.setCountry(country);
        this.setPostalCode(postalCode);
        this.setKnownName(knownName);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }
}