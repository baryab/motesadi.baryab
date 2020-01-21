package com.noavaran.system.vira.baryab.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InternetHelper {
    public static void setInternetEnabled(Context context, boolean enabled) {
        setMobileDataEnabled(context, enabled);
        setWifiEnabled(context, enabled);
    }

    public static void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setWifiEnabled(Context context, boolean enabled) {
        @SuppressLint("WifiManagerPotentialLeak")
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mnetwork = connManager.getActiveNetworkInfo();

        return mnetwork != null && mnetwork.isConnected();
    }

    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetwork = connManager.getActiveNetworkInfo();

        return mNetwork != null && mNetwork.isConnected() && mNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isInternetOn() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);

            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());
    }
}