package com.noavaran.system.vira.baryab.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class GlobalUtils {
    public static int getHeight(Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 400;
    }

    public static int getHeightPercent(Context context, int percent) {
        return (percent * getHeight(context)) / 100;
    }

    public static int getWidth(Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 200;
    }

    public static int getWidthPercent(Context context, int percent) {
        return (percent * getWidth(context)) / 100;
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        return actionBarHeight;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static boolean IsNullOrEmpty(String string) {
        return string == null || string.length() <= 0 || string.equals("null");
    }

    public static String getFirstInstallTime(Context context) {
        try {
            return String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "null";
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static int convertDpToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);
    }

    public static int toInt32(String obj) {
        return toInt32(obj, -1);
    }

    public static int toInt32(String obj, int defaultError) {
        try {
            if (obj == null)
                return defaultError;
            if (obj.length() <= 0)
                return defaultError;

            Locale theLocale = Locale.getDefault();
            NumberFormat numberFormat = NumberFormat.getInstance(theLocale);
            Number theNumber;
            theNumber = numberFormat.parse(obj.replaceAll(",", "."));
            return theNumber.intValue();
        } catch (java.text.ParseException e) {
            return defaultError;

        } catch (Exception e) {
            return defaultError;
        }
    }

    public static String toMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(String.format("%02X", messageDigest[i]).toLowerCase());// Integer.toHexString(0xff

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getIMEI(Context context) {
        String imei = "";

        if (context != null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // get IMEI
            imei = tm.getDeviceId();
            // String phone = tm.getLine1Number();
            if (imei == null || imei == "")
                return "unknown";
        } else {
            Log.e(TAG, "getIMEI: " + "is null.");
        }

        return imei;
    }

    public static String getUnknownValueForEmptyOrNullValue(String value) {
        String result = value;

        if (value == null || value.equalsIgnoreCase("null") || value.isEmpty())
            result = "نامشخص";

        return result;
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isInternetAvailable() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            final InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int getDimensionInDp(Context context, int dimensionInPixel) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, context.getResources().getDisplayMetrics());
    }

    public static int getDimensionInSp(Context context, int dimensionInPixel) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dimensionInPixel, context.getResources().getDisplayMetrics());
    }

    public static boolean isGooglePlayServicesAvailable(Context context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    // GlobalUtil.appInstalledOrNot(getPackageManager(), "com.check.application");
    public static boolean appInstalledOrNot(PackageManager pm, String uri) {
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    private void setBackgroundResource(View view, int drawableId) {
        view.setBackgroundResource(drawableId);

    }
}
