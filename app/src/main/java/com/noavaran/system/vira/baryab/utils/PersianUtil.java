package com.noavaran.system.vira.baryab.utils;

public class PersianUtil {
    public static String persianDigitToEnglish(String persianNumber) {
        return persianNumber
                .replace("۰", "0")
                .replace("۱", "1")
                .replace("۲", "2")
                .replace("۳", "3")
                .replace("۴", "4")
                .replace("۵", "5")
                .replace("۶", "6")
                .replace("۷", "7")
                .replace("۸", "8")
                .replace("۹", "9");
    }
}