package com.noavaran.system.vira.baryab.database.models;

import com.orm.SugarRecord;

public class ProvinceType extends SugarRecord {
    private int idCity;
    private String cityName;

    public ProvinceType(int id, String name) {
        super();

        this.idCity = id;
        this.cityName = name;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
