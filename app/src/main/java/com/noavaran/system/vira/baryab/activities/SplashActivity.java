package com.noavaran.system.vira.baryab.activities;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;
import com.noavaran.system.vira.baryab.dialogs.NoInternetConnectionDialog;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.helpers.InternetHelper;
import com.noavaran.system.vira.baryab.services.SignalRService;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (GlobalUtils.isGooglePlayServicesAvailable(SplashActivity.this)) {
            new Handler(Looper.getMainLooper()).postDelayed(startSignalRService, 1000);
            checkInternetConnectivity();
        } else {
            MessageDialog messageDialog = new MessageDialog(SplashActivity.this, "Google Play Service", "برای استفاده از باریاب نو نسخه متصدیان به آخرین نسخه سرویس گول پلی نیاز است. لطفا آن را نصب و سپس مجددا برنامه را اجرا نمایید.");
            messageDialog.setOnClickListener(new MessageDialog.OnClickListener() {
                @Override
                public void onConfirm() {
                    if (GlobalUtils.appInstalledOrNot(getPackageManager(), "com.farsitel.bazaar")) {
                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.farsitel.bazaar");
                        startActivity(LaunchIntent);
                    } else if (GlobalUtils.appInstalledOrNot(getPackageManager(), "com.android.vending")) {
                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
                        startActivity(LaunchIntent);
                    }
                    finish();
                }
            });
            messageDialog.show();
        }
    }

    private void checkInternetConnectivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (InternetHelper.isNetworkAvailable(SplashActivity.this)) {
                    doRemaingSplashStuffs();
                } else {
                    registerInternetConnectivityReceiver();

                    NoInternetConnectionDialog noInternetConnectionDialog = new NoInternetConnectionDialog(SplashActivity.this);
                    noInternetConnectionDialog.show();
                }
            }
        }, 500);
    }

    private void doRemaingSplashStuffs() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = SPreferences.getInstance(SplashActivity.this).getToken();

                if (GlobalUtils.IsNullOrEmpty(token)) {
                    new ActivitiesHelpers(SplashActivity.this).gotoActivityLogin();
                    SplashActivity.this.finish();
                } else {
                    new ActivitiesHelpers(SplashActivity.this).gotoActivityMain();
                    SplashActivity.this.finish();
                }
            }
        }, 2500);
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

    private void registerInternetConnectivityReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiverInternetConnectivity, intentFilter);
    }

    private void unregisterInternetConnectivityReceiver() {
        if (broadcastReceiverInternetConnectivity != null) {
            try {
                unregisterReceiver(broadcastReceiverInternetConnectivity);
                broadcastReceiverInternetConnectivity = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private Runnable startSignalRService = new Runnable() {
        @Override
        public void run() {
            if (!isServiceRunning(SplashActivity.this, SignalRService.class)) {
                Intent intent = new Intent(SplashActivity.this, SignalRService.class);
                startService(intent);
            }
        }
    };

    BroadcastReceiver broadcastReceiverInternetConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                doRemaingSplashStuffs();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterInternetConnectivityReceiver();
        freeMemory();
        super.onDestroy();
    }
}