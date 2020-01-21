package com.noavaran.system.vira.baryab.onesignal;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class HandlerNotificationOpened implements OneSignal.NotificationOpenedHandler {
    private ObjectMapper mapper;

    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;

        if (data != null) {
            //While sending a Push notification from OneSignal dashboard
            // you can send an addtional data named "customkey" and retrieve the value of it and do necessary operation
            customKey = data.optString("customkey", null);

            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);

            if (data != null) {
                String newRequest = data.optString("type", null);

                if (newRequest != null && newRequest.equals("inbox")) {
                }
            }
        }

//        OneSignal.clearOneSignalNotifications();
    }
}