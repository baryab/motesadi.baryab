package com.noavaran.system.vira.baryab;

import android.content.Context;
import android.content.SharedPreferences;

public class SPreferences {
    private final static String PREFNAME = "com.noavaran.system.vira.baryab";
    private final static String KEY_TOKEN = "token";
    private final static String KEY_ONESIGNAL_USERID = "one_signal_user_id";
    private final static String KEY_ONESIGNAL_REGISTRATIONID = "one_signal_registration_id";
    private final static String KEY_ZOOMLEVEL = "zoom_level";
    private final static String KEY_REFRESH = "need_refresh";

    private static SPreferences sPreferences;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SPreferences(Context context) {
        this.context = context;
        preferences = context.getApplicationContext().getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static SPreferences getInstance(Context context) {
        if (sPreferences == null)
            sPreferences = new SPreferences(context);

        return sPreferences;
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return preferences.getString(KEY_TOKEN, "");
    }

    public void removeToken() {
        editor.remove(KEY_TOKEN);
        editor.apply();
    }

    public void setOneSignalInfo(String userId, String registrationId) {
        editor.putString(KEY_ONESIGNAL_USERID, userId);
        editor.putString(KEY_ONESIGNAL_REGISTRATIONID, registrationId);
        editor.commit();
    }

    public String getOneSignalUserid() {
        return preferences.getString(KEY_ONESIGNAL_USERID, "");
    }

    public String getOneSignalRegistrationid() {
        return preferences.getString(KEY_ONESIGNAL_REGISTRATIONID, "");
    }

    public void removeOneSignalInfo() {
        editor.remove(KEY_ONESIGNAL_USERID);
        editor.apply();

        editor.remove(KEY_ONESIGNAL_REGISTRATIONID);
        editor.apply();
    }

    public void setZoomLevel(float zoomLevel) {
        editor.putFloat(KEY_ZOOMLEVEL, zoomLevel);
        editor.commit();
    }

    public float getZoomLevel() {
        return preferences.getFloat(KEY_ZOOMLEVEL, 4.5f);
    }

    public void setRefresh(boolean needRefresh) {
        editor.putBoolean(KEY_REFRESH, needRefresh);
        editor.commit();
    }

    public boolean hasRefresh() {
        return preferences.getBoolean(KEY_REFRESH, false);
    }
}
