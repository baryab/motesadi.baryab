package com.noavaran.system.vira.baryab.helpers;

import android.content.Context;
import android.location.LocationManager;

public class GPSHelper {
    public static boolean isGPSProviderEnabled(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return false;
        else
            return true;
    }
}
