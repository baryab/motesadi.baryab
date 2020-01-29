package com.noavaran.system.vira.baryab.dialogs;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.database.models.LoadingFareType;
import com.noavaran.system.vira.baryab.database.models.ProvinceType;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.helpers.InternetHelper;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class SynchronizationDialog extends Dialog {
    private Context context;
    private CustomTextView tvNoInternetConnection;

    private Animation animBlink;

    private int tryServerConnectionCounter = 0;

    private OnSynchronizationListener onSynchronizationListener;
    public interface OnSynchronizationListener {
        public abstract void onSuccess();
        public abstract void onFailure();
    }

    public SynchronizationDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        findViews();
        initComponents();
        setViewsListeners();

        if (InternetHelper.isNetworkAvailable(context)) {
            getDraftsFromServer();
        } else {
            registerInternetConnectivityReceiver();
            showTextViewNoInternetConnection();
        }
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_synchronization);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void findViews() {
        tvNoInternetConnection = (CustomTextView) findViewById(R.id.dgSynchronization_tvNoInternetConnection);
    }

    private void initComponents() {
        setCancelable(false);

        animBlink = AnimationUtils.loadAnimation(context, R.anim.blink);
    }

    private void setViewsListeners() {

    }

    private void getDraftsFromServer() {
        OkHttpHelper okHttpHelper = new OkHttpHelper(context, Configuration.API_INIT_DRAFT, OkHttpHelper.MEDIA_TYPE_JSON);
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
                doRequiredStuffForOnFailureResult();
            }

            @Override
            public void onFailure(final String errorMessage) {
                doRequiredStuffForOnFailureResult();
            }

            @Override
            public void onNoInternetConnection() {
                doRequiredStuffForOnFailureResult();
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

            //Getting Province Type
            ProvinceType.deleteAll(ProvinceType.class);
            JSONArray jaProvinceType = result.optJSONObject("data").optJSONArray("province");
            for (int i = 0; i < jaProvinceType.length(); i++) {
                ProvinceType provinceType = new ProvinceType(jaProvinceType.optJSONObject(i).optInt("Id"), jaProvinceType.optJSONObject(i).optString("cityName"));
                provinceType.save();
            }

            dismiss();

            onSynchronizationListener.onSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doGettingDraftsFromServerAgain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tryServerConnectionCounter++;
                hideTextViewNoInternetConnection();
                getDraftsFromServer();
            }
        }, 15000);
    }

    private void showTextViewNoInternetConnection() {
        tvNoInternetConnection.setVisibility(View.VISIBLE);
        tvNoInternetConnection.startAnimation(animBlink);
    }

    private void hideTextViewNoInternetConnection() {
        tvNoInternetConnection.setVisibility(View.INVISIBLE);
    }

    private void doRequiredStuffForOnFailureResult() {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (tryServerConnectionCounter >= 3) {
                        onSynchronizationListener.onFailure();
                        dismiss();
                    } else {
                        if (tvNoInternetConnection.getVisibility() == View.INVISIBLE)
                            showTextViewNoInternetConnection();

                        tvNoInternetConnection.setText("خطا در ارتباط با سرور");
                        doGettingDraftsFromServerAgain();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setOnSynchronizationListener(OnSynchronizationListener onSynchronizationListener) {
        this.onSynchronizationListener = onSynchronizationListener;
    }

    private void registerInternetConnectivityReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(broadcastReceiverInternetConnectivity, intentFilter);
    }

    private void unregisterInternetConnectivityReceiver() {
        if (broadcastReceiverInternetConnectivity != null) {
            try {
                context.unregisterReceiver(broadcastReceiverInternetConnectivity);
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
                        hideTextViewNoInternetConnection();
                        getDraftsFromServer();
                    }
                }
            }, 15000);
        }
    };

    @Override
    protected void onStop() {
        unregisterInternetConnectivityReceiver();

        super.onStop();
    }
}