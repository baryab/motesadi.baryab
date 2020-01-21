package com.noavaran.system.vira.baryab.utils;

import java.text.DecimalFormat;

public class NumberCommafy {
    //Adding comma in between every 3 digits to a large number by parsing the number string.
    public static String stringParserCommafy(String inputNum) {

        String commafiedNum = "";
        Character firstChar = inputNum.charAt(0);

        //If there is a positive or negative number sign,
        //then put the number sign to the commafiedNum and remove the sign from inputNum.
        if (firstChar == '+' || firstChar == '-') {
            commafiedNum = commafiedNum + Character.toString(firstChar);
            inputNum = inputNum.replaceAll("[-\\+]", "");
        }

        //If the input number has decimal places,
        //then split it into two, save the first part to inputNum
        //and save the second part to decimalNum which will be appended to the final result at the end.
        String[] splittedNum = inputNum.split("\\.");
        String decimalNum = "";
        if (splittedNum.length == 2) {
            inputNum = splittedNum[0];
            decimalNum = "." + splittedNum[1];
        }

        //The main logic for adding commas to the number.
        int numLength = inputNum.length();
        for (int i = 0; i < numLength; i++) {
            if ((numLength - i) % 3 == 0 && i != 0) {
                commafiedNum += ",";
            }
            commafiedNum += inputNum.charAt(i);
        }

        return commafiedNum + decimalNum;
    }


    //Adding comma in between every 3 digits to a large number by using regex.
    public static String regexCommafy(String inputNum) {
        String regex = "(\\d)(?=(\\d{3})+$)";
        String[] splittedNum = inputNum.split("\\.");
        if (splittedNum.length == 2) {
            return splittedNum[0].replaceAll(regex, "$1,") + "." + splittedNum[1];
        } else {
            return inputNum.replaceAll(regex, "$1,");
        }
    }

    //Adding comma in between every 3 digits to a large number by using DecimalFormat.
    public static String decimalFormatCommafy(String inputNum) {
        //If the input number has decimal places,
        //then split it into two, save the first part to inputNum
        //and save the second part to decimalNum which will be appended to the final result at the end.
        String[] splittedNum = inputNum.split("\\.");
        String decimalNum = "";
        if (splittedNum.length == 2) {
            inputNum = splittedNum[0];
            decimalNum = "." + splittedNum[1];
        }

        Double inputDouble = Double.parseDouble(inputNum);
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String output = myFormatter.format(inputDouble);


        return output + decimalNum;
    }
}