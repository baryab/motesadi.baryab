package com.noavaran.system.vira.baryab.utils;

/**
 * Created by Droid A on 11/9/2017.
 */

public class StringUtil {
    private static StringUtil stringUtil;

    public StringUtil() {}

    public static StringUtil getInstance() {
        if (stringUtil == null)
            stringUtil = new StringUtil();

        return stringUtil;
    }

    public String[] reverse(String[] stringArray) {
        for(int i = 0 ; i < stringArray.length / 2 ; i++) {
            String temp = stringArray[i];
            stringArray[i] = stringArray[stringArray.length -i -1];
            stringArray[stringArray.length -i -1] = temp;
        }

        return stringArray;
    }

    public String[] reverseString(String[] words) {
        String[] t = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            t[i]= new StringBuilder(words[i]).reverse().toString();
        }
        return t;
    }

    public String convertToCommaSeparated(String[] strings) {
        StringBuffer sb = new StringBuffer("");

        for (int i = 0; strings != null && i < strings.length; i++) {
            sb.append(strings[i]);
            if (i < strings.length - 1) {
                sb.append(" , ");
            }
        }

        return sb.toString();
    }
}