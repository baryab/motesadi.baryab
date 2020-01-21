package com.noavaran.system.vira.baryab;

import android.content.Context;

import com.noavaran.system.vira.baryab.onesignal.HandlerNotificationOpened;
import com.noavaran.system.vira.baryab.onesignal.HandlerNotificationReceived;
import com.noavaran.system.vira.baryab.utils.uncaughtexception.ExceptionHandler;
import com.onesignal.OneSignal;
import com.orm.SugarApp;

import ir.map.sdk_map.Mapir;

public class AppController extends SugarApp {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        context = getApplicationContext();
        initOneSignalId();
        Mapir.getInstance(this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU0MDYzMzQxMDkwMmU2MTM0NDFiY2U5NzdiOTc4NGM4NzJlODQ0ZjRkOTQ2Njc5NzEyYjMyOWU2MDkxM2EwNmY4NDZiYmFkNGU0YzM1ODgzIn0.eyJhdWQiOiI3MzUyIiwianRpIjoiZTQwNjMzNDEwOTAyZTYxMzQ0MWJjZTk3N2I5Nzg0Yzg3MmU4NDRmNGQ5NDY2Nzk3MTJiMzI5ZTYwOTEzYTA2Zjg0NmJiYWQ0ZTRjMzU4ODMiLCJpYXQiOjE1NzgwNjA4NzQsIm5iZiI6MTU3ODA2MDg3NCwiZXhwIjoxNTgwNTY2NDc0LCJzdWIiOiIiLCJzY29wZXMiOlsiYmFzaWMiXX0.H1BGBK6a-4SqL0-W1BHBKBnlzyN6ebsity2-pzPvXhYbosQ5QyS5tJkOqJtEAzYHAOuMU17wki0JqfZMtW4QaYdJe26NRT6pfjEGOm1DduBEs7cOC8lAsyDQY02tK3BjxO1AoIobrA2erJnx-HQM77jwpPIJ4ChBW-BBAxV9KagJqQzOWqsSqrihhJGiHu9a9KAH4ozyH2a5c4p4MIgja0CZ53e7XD3KgbIHY3t3INqvinfw04o4IOHJqHiT8JSy-coZIilbb1YtKvhe1_feTNrNlJt4PxgoCiyTQBeraRlzERJNafoDGLonLoOhJ3kJSJuxi9ti5w8lDluGhIpqfQ");



    }

    public static Context getContext() {
        return  context;
    }

    private void initOneSignalId() {
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .setNotificationOpenedHandler(new HandlerNotificationOpened())
                .setNotificationReceivedHandler(new HandlerNotificationReceived())
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                if (registrationId != null) {
                    SPreferences.getInstance(context).setOneSignalInfo(userId, registrationId);
                } else {
                    SPreferences.getInstance(context).setOneSignalInfo(userId, "");
                }
            }
        });
    }
}