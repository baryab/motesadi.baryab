package com.noavaran.system.vira.baryab.utils;

public class IntegerUtil {
    public static String getDigitsFromString(String str) {
        return str.replaceAll("[^0-9]", "");
    }
}
