package com.noavaran.system.vira.baryab.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static Boolean isMobileNumber(String value){
        value = value.replace("+98", "0");
        value = value.replace("+980", "0");

        if (value.length() != 11 && value.length() != 13) {
            return false;
        } else {
            Pattern p = Pattern.compile("(09)\\d{9}");
            Matcher m = p.matcher(value);
            return m.find();
        }
    }

    public static Boolean isNumber(String value){

        if (value.length() != 3) {
            return false;
        } else {

            Pattern p = Pattern.compile("\\d{3}");
            Matcher m = p.matcher(value);
            return m.find();
        }
    }
    public static Boolean isCarModeValid(int model){
        boolean result = false;

        if ((model > 1370 && model < 1396) || (model > 1990 && model < 2017)) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}
