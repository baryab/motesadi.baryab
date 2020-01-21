package com.noavaran.system.vira.baryab.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.database.models.LoadingFareType;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.helpers.InternetHelper;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class SyncService extends Service {
    private Intent intent;
    private int flags;
    private int startId;

    private Handler mHandler; // to display Toast message

    public SyncService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        this.flags = flags;
        this.startId = startId;

        Log.d("----- Sync Service", "Sync Service has been started ... ");

        registerInternetConnectivityReceiver();

        return START_NOT_STICKY;
    }

    private void getDraftsFromServer() {
        OkHttpHelper okHttpHelper = new OkHttpHelper(getApplicationContext(), Configuration.API_INIT_DRAFT, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getDrafts(new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onResponse(JSONObject result) {
                loadDrafts(result);
            }

            @Override
            public void onRequestReject(final String message) {
                Log.d("----- Sync Service", "Error in getting data from server.");
                showMessage("خطا در همگام سازی اطلاعات با سرور");
                doGettingDraftsFromServerAgain();
            }

            @Override
            public void onFailure(final String errorMessage) {
                Log.d("----- Sync Service", "Error in getting data from server.");
                showMessage("خطا در همگام سازی اطلاعات با سرور");
                doGettingDraftsFromServerAgain();
            }

            @Override
            public void onNoInternetConnection() {
                Log.d("----- Sync Service", "Error in getting data from server.");
                showMessage("خطا در همگام سازی اطلاعات با سرور");
                doGettingDraftsFromServerAgain();
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void loadDrafts(final JSONObject result) {
        try {
            // Getting loading fare type
            LoadingFareType.deleteAll(LoadingFareType.class);
            JSONArray jaMoneyType = result.optJSONObject("data").optJSONArray("moneyType");
            for (int i = 0; i < jaMoneyType.length(); i++) {
                LoadingFareType loadingFareType = new LoadingFareType(jaMoneyType.optJSONObject(i).optInt("Id"), jaMoneyType.optJSONObject(i).optString("name"));
                loadingFareType.save();
            }

            // Getting truck type
            TruckType.deleteAll(TruckType.class);
            JSONArray jaTruckType = result.optJSONObject("data").optJSONArray("truckType");
            for (int i = 0; i < jaTruckType.length(); i++) {
                int id = jaTruckType.optJSONObject(i).optInt("Id", 0);
                String name = jaTruckType.optJSONObject(i).getString("Name");
                int pid = jaTruckType.optJSONObject(i).optInt("Pid", 0);
                String fullName = jaTruckType.optJSONObject(i).getString("FullName");
                float minLength = jaTruckType.optJSONObject(i).has("MinLength") ? BigDecimal.valueOf(jaTruckType.optJSONObject(i).optDouble("MinLength", 0)).floatValue() : 0;
                float maxLength = jaTruckType.optJSONObject(i).has("MaxLength") ? BigDecimal.valueOf(jaTruckType.optJSONObject(i).optDouble("MaxLength", 0)).floatValue() : 0;
                float minWidth = jaTruckType.optJSONObject(i).has("MinWidth") ? BigDecimal.valueOf(jaTruckType.optJSONObject(i).optDouble("MinWidth", 0)).floatValue() : 0;
                float maxWidth = jaTruckType.optJSONObject(i).has("MaxWidth") ? BigDecimal.valueOf(jaTruckType.optJSONObject(i).optDouble("MaxWidth", 0)).floatValue() : 0;
                float minHeight = jaTruckType.optJSONObject(i).has("MinHeight") ? BigDecimal.valueOf(jaTruckType.optJSONObject(i).optDouble("MinHeight", 0)).floatValue() : 0;
                float maxHeight = jaTruckType.optJSONObject(i).has("MaxHeight") ? BigDecimal.valueOf(jaTruckType.optJSONObject(i).optDouble("MaxHeight", 0)).floatValue() : 0;
                boolean isRoof = jaTruckType.optJSONObject(i).optBoolean("IsRoof", false);
                boolean hasChild = jaTruckType.optJSONObject(i).optBoolean("HasChild", false);

                TruckType truckType = new TruckType(id, name, pid, fullName, minLength, maxLength, minWidth, maxWidth, minHeight, maxHeight, isRoof, hasChild);
                truckType.save();
            }

            Log.d("----- Sync Service", "All data have been saved in database.");
            showMessage("همگام سازی اطلاعات با سرور انجام شد");
            stopSyncService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showMessage(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SyncService.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void stopSyncService() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("----- Sync Service", "Sync service has been stopped.");
                stopSelf(startId);
            }
        });
    }

    private void doGettingDraftsFromServerAgain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (InternetHelper.isInternetOn()) {
                    Log.d("----- Sync Service", "Trying to get all drafts from server again");
                    getDraftsFromServer();
                } else {
                    Log.d("----- Sync Service", "No Internet Connection ... !");
                    doGettingDraftsFromServerAgain();
                }
            }
        }, 60000);
    }

    @Override
    public void onDestroy() {
        unregisterInternetConnectivityReceiver();

        super.onDestroy();
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

    BroadcastReceiver broadcastReceiverInternetConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (netInfo != null && netInfo.isConnected()) {
                        if (InternetHelper.isInternetOn()) {
                            getDraftsFromServer();
                        }
                    }
                }
            }, 15000);
        }
    };
}