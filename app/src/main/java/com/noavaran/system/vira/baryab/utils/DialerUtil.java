package com.noavaran.system.vira.baryab.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class DialerUtil {
    private static DialerUtil dialerUtil;
    private Context context;

    private DialerUtil(Context context) {
        this.context = context;
    }

    public static DialerUtil getInstance(Context context) {
        if (dialerUtil == null)
            dialerUtil = new DialerUtil(context);

        return dialerUtil;
    }

    public void openTheDialerApp(String phoneNumber) {
        Uri call = Uri.parse("tel:" + phoneNumber);
        Intent surf = new Intent(Intent.ACTION_DIAL, call);
        context.startActivity(surf);
    }

    public void openTheDialerAppAndDoTheCallAutomatically(String phoneNumber) {
//        Uri call = Uri.parse("tel:" + phoneNumber);
//        Intent surf = new Intent(Intent.ACTION_CALL, call);
//        context.startActivity(surf);

        openTheDialerApp(phoneNumber);
    }
}
