package com.noavaran.system.vira.baryab.enums;

public enum ShipmentEditTypeEnum {
    edit(0), requestAgain(1);

    private int _value;

    ShipmentEditTypeEnum(int val) {
        this._value = val;
    }

    public int getValue() {
        return _value;
    }

    public static ShipmentEditTypeEnum byValue(int val) {
        for (ShipmentEditTypeEnum item : ShipmentEditTypeEnum.values()) {
            if (item.getValue() == val)
                return item;
        }

        return null;
    }
}