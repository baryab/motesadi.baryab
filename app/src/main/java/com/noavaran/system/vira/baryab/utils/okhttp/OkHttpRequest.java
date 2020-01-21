package com.noavaran.system.vira.baryab.utils.okhttp;

import android.content.Context;
import android.util.Log;

import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.helpers.InternetHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpRequest {
    private Context context;
    private final String url;
    private final MediaType mediaType;

    private OkHttpClient client;
    private RequestBody body;
    private Request request;
    private HttpUrl.Builder urlBuilder;

    public interface OnCallback {
        public abstract void onResponse(Call call, Response response, JSONObject result);
        public abstract void onFailure(Call call, IOException exception);
        public abstract void onError(String errorMessage);
        public abstract void onNoInternetConnection();
    }

    public OkHttpRequest(Context context, String url, MediaType mediaType) {
        this.context = context;
        this.url = url;
        this.mediaType = mediaType;

        urlBuilder = HttpUrl.parse(url).newBuilder();
    }

    public void addQueryParameter(String key, String value) {
        urlBuilder.addQueryParameter(key, value);
    }

    public void addEncodedQueryParameter(String encodedName, String encodedValue) {
        urlBuilder.addEncodedQueryParameter(encodedName, encodedValue);
    }

    public void addPathSegment(String pathSegment) {
        urlBuilder.addPathSegment(pathSegment);
    }

    public void addPathSegments(String pathSegments) {
        urlBuilder.addPathSegments(pathSegments);
    }

    public void addEncodedPathSegment(String encodedPathSegment) {
        urlBuilder.addEncodedPathSegment(encodedPathSegment);
    }

    public void addEncodedPathSegments(String encodedPathSegments) {
        urlBuilder.addEncodedPathSegments(encodedPathSegments);
    }

    public void postRequest(String postBody, final OnCallback onCallback) {
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
        body = RequestBody.create(mediaType, postBody);
        request = new Request.Builder().header("Authorization", SPreferences.getInstance(context).getToken()).url(urlBuilder.build().toString()).post(body).build();

        Log.e("----- OkHttpRequest", "post request method, url is -> " + urlBuilder.build().toString());

        callRequest(client, request, onCallback);
    }

    public void getRequest(final OnCallback onCallback) {
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
        request = new Request.Builder().url(urlBuilder.build().toString()).get().addHeader("Content-Type", "application/json").addHeader("Authorization", SPreferences.getInstance(context).getToken()).addHeader("cache-control", "no-cache").build();

        Log.e("----- OkHttpRequest", "get request method, url is -> " + urlBuilder.build().toString());

        callRequest(client, request, onCallback);
    }

    public void putRequest(String postBody, final OnCallback onCallback) {
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
        body = RequestBody.create(mediaType, postBody);
        request = new Request.Builder().header("Authorization", SPreferences.getInstance(context).getToken()).url(urlBuilder.build().toString()).put(body).build();

        Log.e("----- OkHttpRequest", "put request method, url is -> " + urlBuilder.build().toString());

        callRequest(client, request, onCallback);
    }

    public void deleteRequest(String postBody, final OnCallback onCallback) {
        client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
        body = RequestBody.create(mediaType, postBody);
        request = new Request.Builder().header("Authorization", SPreferences.getInstance(context).getToken()).url(urlBuilder.build().toString()).delete(body).build();

        Log.e("----- OkHttpRequest", "delete request method, url is -> " + urlBuilder.build().toString());

        callRequest(client, request, onCallback);
    }

    private synchronized void callRequest(OkHttpClient client, Request request, final OnCallback onCallback) {
        if (InternetHelper.isDeviceOnline(context)) {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    onCallback.onFailure(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.code() == 403) {
                            SPreferences.getInstance(context).removeToken();
                            ActivitiesHelpers.getInstance(context).gotoActivityLogin();
                        } else {
                            String body = response.body().string();
                            Log.e("----- Toke is ", SPreferences.getInstance(context).getToken());
                            Log.e("----- OkHttpRequest", body);
                            if (body.isEmpty()) {
                                onCallback.onResponse(call, response, new JSONObject());
                            } else {
                                JSONObject json = new JSONObject(body);
                                onCallback.onResponse(call, response, json);
                            }
                        }
                    } catch (Exception ex) {
                        onCallback.onError(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            onCallback.onNoInternetConnection();
            MessageDialog messageDialog = new MessageDialog(context, "باریاب نو", "عدم برقراری ارتباط");
            messageDialog.show();
        }
    }
}