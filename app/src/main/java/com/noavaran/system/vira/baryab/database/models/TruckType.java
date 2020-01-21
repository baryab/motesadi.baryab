package com.noavaran.system.vira.baryab.database.models;

import com.orm.SugarRecord;

public class TruckType extends SugarRecord {
    private int truckTypeId;
    private String name;
    private int pid;
    private String fullName;
    private float minLength;
    private float maxLength;
    private float minWidth;
    private float maxWidth;
    private float minHeight;
    private float maxHeight;
    private boolean isRoof;
    private boolean hasChild;

    public TruckType() {}

    public TruckType(int truckTypeId, String name, int pid, String fullName, float minLength, float maxLength, float minWidth, float maxWidth, float minHeight, float maxHeight, boolean isRoof, boolean hasChild) {
        super();

        this.truckTypeId = truckTypeId;
        this.name = name;
        this.pid = pid;
        this.fullName = fullName;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.isRoof = isRoof;
        this.hasChild = hasChild;
    }

    public int getTruckTypeId() {
        return truckTypeId;
    }

    public void setTruckTypeId(int truckTypeId) {
        this.truckTypeId = truckTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public float getMinLength() {
        return minLength;
    }

    public void setMinLength(float minLength) {
        this.minLength = minLength;
    }

    public float getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(float maxLength) {
        this.maxLength = maxLength;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    public boolean isRoof() {
        return isRoof;
    }

    public void setRoof(boolean roof) {
        isRoof = roof;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }
}
