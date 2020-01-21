package com.noavaran.system.vira.baryab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONObject;

public class CrashActivity extends BaseActivity {
    private CustomTextView btnRestartApp;
    private String stackTrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        btnRestartApp = (CustomTextView) findViewById(R.id.acCrash_btnRestartApp);
        btnRestartApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        getIntents();
        sendUncaughtExceptionToServer(stackTrace);
    }

    private void getIntents() {
        stackTrace = getIntent().getStringExtra("STACKTRACE");
    }

    private void sendUncaughtExceptionToServer(String exception) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(CrashActivity.this, Configuration.API_CRASH, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.sendUncaughtException(exception, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                showDialogProgress("در حال ارسال اطلاعات به سرور");
            }

            @Override
            public void onResponse(JSONObject result) {
                dismissDialogProgress();
                loadUncaughtException(result);
            }

            @Override
            public void onRequestReject(String message) {
                showToastWarning(message);
                dismissDialogProgress();
            }

            @Override
            public void onFailure(String errorMessage) {
                showToastError(errorMessage);
                dismissDialogProgress();
            }

            @Override
            public void onNoInternetConnection() {
                dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                dismissDialogProgress();
            }
        });
    }

    private void loadUncaughtException(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        showToastSuccess("اطلاعات با موفقیت ارسال شد");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}