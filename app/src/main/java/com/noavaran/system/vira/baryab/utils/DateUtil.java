package com.noavaran.system.vira.baryab.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public static final String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    private static DateUtil dateUtil;

    public static DateUtil getInstance() {
        if (dateUtil == null)
            dateUtil = new DateUtil();

        return dateUtil;
    }

    public DateUtil() {
    }

    public static String getCurrentDateTimeAsString(String dateTimeFormat) {
        return new SimpleDateFormat(dateTimeFormat, Locale.getDefault()).format(new Date());
    }

    public static String getCurrentDateAsString(String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date());
    }

    public static Date getCurrentDate(String dateFormat) {
        return getDateFromDateString(new Date().toString(), dateFormat);
    }

    public static Date getDateFromDateString(String strDate, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static long getTimeStampFromDate(Date date) {
        return date.getTime();
    }

    public static String convertDateToString(Date date) {
        return new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault()).format(date);
    }

    // Date fromDate = DateUtils.getTimeFromTimeString("00:51", "hh:mm");
    public static Date getTimeFromTimeString(String time, String dateFormat) {
        SimpleDateFormat sdf = null;
        Date date = null;

        try {
            sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            date = sdf.parse(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return date;
    }

    // Date fromDate = DateUtils.getTimeFromTimeString("00:51");
    public static Date getTimeFromTimeString(String time) {
        SimpleDateFormat sdf = null;
        Date date = null;

        try {
            sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
            date = sdf.parse(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return date;
    }

    // String sumTwoTimes = DateUtils.addTwoStringTimes("03:30", "03:50")
    public static String addTwoStringTimes(String fromTime, String toTime) {
        SimpleDateFormat timeFormat = null;
        Date fromDate = null;
        Date toDate = null;
        long sum = 0;

        try {
            timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            fromDate = timeFormat.parse(fromTime);
            toDate = timeFormat.parse(toTime);

            sum = fromDate.getTime() + toDate.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (timeFormat != null) {
            return timeFormat.format(new Date(sum));
        }
        return "";
    }

    // String timeDiff = DateUtils.getTimeBetweenTwoDates(fromDate, toDate);
    public static String getTimeBetweenTwoDates(Date fromDate, Date toDate) {
        long diff = toDate.getTime() - fromDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
//		int diffInDays = (int) diff / (1000 * 60 * 60 * 24);

        String time = diffHours + ":" + diffMinutes + ":" + diffSeconds;
        String[] timeSplited = time.split(":");

        if (timeSplited[0].length() == 1)
            timeSplited[0] = "0" + timeSplited[0];
        if (timeSplited[1].length() == 1)
            timeSplited[1] = "0" + timeSplited[1];
        if (timeSplited[2].length() == 1)
            timeSplited[2] = "0" + timeSplited[2];

        time = timeSplited[0] + ":" + timeSplited[1] + ":" + timeSplited[2];

        return time;
    }

    // String time = DateUtils.getTimeFromDateTime("2017/01/10 14:52:30");
    public static String getTimeFromDateTime(String dateTime) {
        String[] datTime = dateTime.split(" ");
        return datTime[1];
    }

    // String date = DateUtils.getDateFromDateTime("2017/01/10 14:52:30");
    public static String getDateFromDateTime(String dateTime) {
        String[] datTime = dateTime.split(" ");
        return datTime[0];
    }

    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                return 0;
            case Calendar.SUNDAY:
                return 1;
            case Calendar.MONDAY:
                return 2;
            case Calendar.TUESDAY:
                return 3;
            case Calendar.WEDNESDAY:
                return 4;
            case Calendar.THURSDAY:
                return 5;
            case Calendar.FRIDAY:
                return 6;
            default:
                return 0;
        }
    }

    // int dayOfWeek = DateUtils.getDayOfWeek("2000/01/01")
    public static int getDayOfWeek(String strDate) {
        Calendar c = Calendar.getInstance();

        try {
            Date date = new SimpleDateFormat("yyyy/M/dd", Locale.getDefault()).parse(strDate);
            c.setFirstDayOfWeek(Calendar.SATURDAY);
            c.setTime(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                return 0;
            case Calendar.SUNDAY:
                return 1;
            case Calendar.MONDAY:
                return 2;
            case Calendar.TUESDAY:
                return 3;
            case Calendar.WEDNESDAY:
                return 4;
            case Calendar.THURSDAY:
                return 5;
            case Calendar.FRIDAY:
                return 6;
            default:
                return 0;
        }
    }

    public static String getTimeFromDate(Date date) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
        return localDateFormat.format(date);
    }

    // int duration = DateUtils.getDurationFromTimeString("02:10:30");
    // duration's result is 7830
    public static int getDurationFromTimeString(String time) {
        String[] tokens = time.split(":");

        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        int seconds = Integer.parseInt(tokens[2]);

        return (3600 * hours) + (60 * minutes) + seconds;
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DAY_OF_MONTH) == cdate.get(Calendar.DAY_OF_MONTH);
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
