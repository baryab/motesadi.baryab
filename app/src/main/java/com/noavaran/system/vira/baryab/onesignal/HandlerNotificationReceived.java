package com.noavaran.system.vira.baryab.onesignal;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class HandlerNotificationReceived implements OneSignal.NotificationReceivedHandler {
    private ObjectMapper mapper;

    @Override
    public void notificationReceived(OSNotification notification) {
        try {
            JSONObject data = notification.payload.additionalData;

            if (data != null) {
                String customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);

                if (data != null) {
                    String newRequest = data.optString("type", null);
                    if (newRequest != null && newRequest.equals("new_request")) {
                        String result = data.getString("payload").replace("\\", "");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            OneSignal.clearOneSignalNotifications();
        }
    }
}