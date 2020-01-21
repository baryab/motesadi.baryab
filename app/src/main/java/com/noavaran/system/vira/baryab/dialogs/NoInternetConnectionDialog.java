package com.noavaran.system.vira.baryab.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

public class NoInternetConnectionDialog extends Dialog {
    private Activity activity;
    private Context context;

    private CustomTextView btnWIFI;
    private CustomTextView btnDATA;

    public NoInternetConnectionDialog(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    public NoInternetConnectionDialog(Activity activity) {
        super(activity);

        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_no_internet_connection);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        findViews();
        initComponents();
        setViewsListeners();

        registerInternetConnectivityReceiver();
    }

    private void findViews() {
        btnWIFI = (CustomTextView) findViewById(R.id.dgNoInternetConnection_btnWIFI);
        btnDATA = (CustomTextView) findViewById(R.id.dgNoInternetConnection_btnDATA);
    }

    private void initComponents() {
    }

    private void setViewsListeners() {
        btnWIFI.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                activity.startActivityForResult(intent, Configuration.REQUEST_CODE_WIFI_SETTING);
            }
        });

        btnDATA.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                activity.startActivityForResult(intent, Configuration.REQUEST_CODE_MOBILE_DATA_SETTING);
            }
        });
    }

    private void registerInternetConnectivityReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        if (activity != null)
            activity.registerReceiver(broadcastReceiverInternetConnectivity, intentFilter);
        else
            context.registerReceiver(broadcastReceiverInternetConnectivity, intentFilter);
    }

    private void unregisterInternetConnectivityReceiver() {
        if (broadcastReceiverInternetConnectivity != null) {
            if (activity != null)
                activity.unregisterReceiver(broadcastReceiverInternetConnectivity);
            else
                context.unregisterReceiver(broadcastReceiverInternetConnectivity);
            broadcastReceiverInternetConnectivity = null;
        }
    }

    BroadcastReceiver broadcastReceiverInternetConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                dismiss();
            }
        }
    };

    @Override
    protected void onStop() {
        unregisterInternetConnectivityReceiver();

        super.onStop();
    }
}