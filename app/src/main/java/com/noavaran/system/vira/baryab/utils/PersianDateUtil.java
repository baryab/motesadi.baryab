package com.noavaran.system.vira.baryab.utils;

import com.noavaran.system.vira.baryab.utils.persianDateUtils.CivilDate;
import com.noavaran.system.vira.baryab.utils.persianDateUtils.DateConverter;
import com.noavaran.system.vira.baryab.utils.persianDateUtils.JalaliCalendar;
import com.noavaran.system.vira.baryab.utils.persianDateUtils.PersianDate;

import java.util.Calendar;
import java.util.Date;

public class PersianDateUtil {
    public static String getPersianMonth(int month) {
        switch (month) {
            case 1:
                return "فروردین ماه";
            case 2:
                return "اردیبهشت ماه";
            case 3:
                return "خرداد ماه";
            case 4:
                return "تیر ماه";
            case 5:
                return "مرداد ماه";
            case 6:
                return "شهریور ماه";
            case 7:
                return "مهر ماه";
            case 8:
                return "آبان ماه";
            case 9:
                return "آذر ماه";
            case 10:
                return "دی ماه";
            case 11:
                return "بهمن ماه";
            case 12:
                return "اسفند ماه";
            default:
                return "فروردین ماه";
        }
    }

    public static String getPersianDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 0:
                return "شنبه";
            case 1:
                return "یکشنبه";
            case 2:
                return "دوشنبه";
            case 3:
                return "سه شنبه";
            case 4:
                return "چهارشنبه";
            case 5:
                return "پنجشنبه";
            case 6:
                return "جمعه";
            default:
                return "شنبه";
        }
    }

    // String timeStamp = strDate.replaceAll("[^0-9]", "");
    // String strPersianDate = PersianDateUtil.getPersianDate("1493926200000");
    public static String getPersianDate(String timeStamp) {
        return getPersianDate(Long.parseLong(timeStamp));
    }

    /**
     *
     * @param timeStamp for example ---> PersianDateUtil.getPersianDate(new Date().getTime());
     * @return persian date as ---> "1396/08/21"
     */
    public static String getPersianDate(long timeStamp) {
        Date date = new Date(timeStamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        CivilDate civilDate = new CivilDate(calendar);
        PersianDate persianDate = DateConverter.civilToPersian(civilDate);

        int dayOfMonth = persianDate.getDayOfMonth();
        int month = persianDate.getMonth();
        int year = persianDate.getYear();

        return year + "/" + month + "/" + dayOfMonth;
    }

//    public static String getPersianDate(String strDate) {
//        return getPersianDate(DateUtil.getDateFromDateString(strDate, DateUtil.DATETIME_FORMAT));
//    }

    // return persian date like this "شنبه 13 مرداد ماه 1396"
    public static String getPersianDate(Date date) {
        String strPersianDate = getPersianDate(date.getTime());
        String[] split = strPersianDate.split("/");
        String dayName= getPersianDayOfWeek(DateUtil.getDayOfWeek(date));
        String monthName= getPersianMonth(Integer.parseInt(split[1]));

        return dayName + " " + split[2] + " " + monthName + " " + split[0];
    }

    public static Date getDateFromPersianDateString(String strPersianDate) {
        JalaliCalendar calendar = new JalaliCalendar();
        return calendar.getGregorianDate(strPersianDate);
    }

    public static CivilDate getCivilDateAsCivilDate(String strPersianDate) {
        String[] splitDateFromDateTime;
        String[] splitYearMonthDayFromDate;

        if (strPersianDate.split(" ").length > 1) {
            splitDateFromDateTime = strPersianDate.split(" "); // getting persian date from persian datetime
            splitYearMonthDayFromDate = splitDateFromDateTime[0].split("/"); // getting year, month, and day from persian date
        } else
            splitYearMonthDayFromDate = strPersianDate.split("/"); // getting year, month, and day from persian date

        int year = Integer.parseInt(splitYearMonthDayFromDate[0]);
        int month = Integer.parseInt(splitYearMonthDayFromDate[1]);
        int day = Integer.parseInt(splitYearMonthDayFromDate[2]);

        PersianDate persianDate = new PersianDate(year, month, day);
        return DateConverter.persianToCivil(persianDate);
    }

    public static CivilDate getCivilDateAsDate(String strPersianDate) {
        String[] splitDateFromDateTime;
        String[] splitYearMonthDayFromDate;

        if (strPersianDate.split(" ").length > 1) {
            splitDateFromDateTime = strPersianDate.split(" "); // getting persian date from persian datetime
            splitYearMonthDayFromDate = splitDateFromDateTime[0].split("/"); // getting year, month, and day from persian date
        } else
            splitYearMonthDayFromDate = strPersianDate.split("/"); // getting year, month, and day from persian date

        int year = Integer.parseInt(splitYearMonthDayFromDate[0]);
        int month = Integer.parseInt(splitYearMonthDayFromDate[1]);
        int day = Integer.parseInt(splitYearMonthDayFromDate[2]);

        PersianDate persianDate = new PersianDate(year, month, day);
        return DateConverter.persianToCivil(persianDate);
    }

    public static String getCivilDateAsString(String strPersianDate) {
        String[] splitDateFromDateTime;
        String[] splitYearMonthDayFromDate;

        if (strPersianDate.split(" ").length > 1) {
            splitDateFromDateTime = strPersianDate.split(" "); // getting persian date from persian datetime
            splitYearMonthDayFromDate = splitDateFromDateTime[0].split("/"); // getting year, month, and day from persian date
        } else
            splitYearMonthDayFromDate = strPersianDate.split("/"); // getting year, month, and day from persian date

        int year = Integer.parseInt(splitYearMonthDayFromDate[0]);
        int month = Integer.parseInt(splitYearMonthDayFromDate[1]);
        int day = Integer.parseInt(splitYearMonthDayFromDate[2]);

        PersianDate persianDate = new PersianDate(year, month, day);
        return DateConverter.persianToCivil(persianDate).toString();
    }
}
