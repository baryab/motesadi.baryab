package com.noavaran.system.vira.baryab.info;

public class AddressInfo {
    private int Id;
    private String province;
    private String city;

    public AddressInfo() {}

    public AddressInfo(int Id, String province, String city) {
        this.Id = Id;
        this.province = province;
        this.city = city;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
