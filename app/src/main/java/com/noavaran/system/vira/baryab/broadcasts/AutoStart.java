package com.noavaran.system.vira.baryab.broadcasts;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.noavaran.system.vira.baryab.services.SignalRService;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        if (!isServiceRunning(context, SignalRService.class)) {
            Intent intent = new Intent(context, SignalRService.class);
            context.startService(intent);
        }
    }

    private boolean isServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}