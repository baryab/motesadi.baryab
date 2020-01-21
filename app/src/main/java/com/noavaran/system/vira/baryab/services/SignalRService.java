package com.noavaran.system.vira.baryab.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.helpers.InternetHelper;
import com.noavaran.system.vira.baryab.info.SignalRCustomMessage;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;


public class SignalRService extends Service {
    private HubConnection mHubConnection;
    private HubProxy mHubProxy;
    private Handler mHandler; // to display Toast message

    private String serverUrl = "http://89.39.208.236/api/signalr";
    private String SERVER_HUB_CHAT = "baryabHub";
    private String CLIENT_METHOD_BROADAST_MESSAGE = "hello";

    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private Notification notification;

    public SignalRService() {
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
        registerInternetConnectivityReceiver();

        startSignalR();

        return START_STICKY;
    }

    private void startSignalR() {
        try {
            Platform.loadPlatformComponent(new AndroidPlatformComponent());

            Credentials credentials = new Credentials() {
                @Override
                public void prepareRequest(Request request) {
                    request.addHeader("Authorization", SPreferences.getInstance(getApplicationContext()).getToken());
                }
            };

            mHubConnection = new HubConnection(serverUrl);
            mHubConnection.setCredentials(credentials);

            mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT);
            ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
            SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);

            signalRFuture.get();

            mHubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE, new SubscriptionHandler1<SignalRCustomMessage>() {
                @Override
                public void run(final SignalRCustomMessage signalRCustomMessage) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showNotification(signalRCustomMessage);
                        }
                    });
                }
            }, SignalRCustomMessage.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showNotification(SignalRCustomMessage message) {
        showNotification(message.ticker, message.contentTitle, message.contentText);
    }

    public void showNotification(String ticker, String contentTitle, String contentText) {
        try {
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            notification = new NotificationCompat.Builder(this)
                    .setTicker(ticker)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .build();

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        mHubConnection.stop();
        unregisterInternetConnectivityReceiver();
        super.onDestroy();
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
                        startSignalR();
                    } else if (InternetHelper.isInternetOn()) {
                        startSignalR();
                    }
                }
            }, 15000);
        }
    };

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
}