package com.noavaran.system.vira.baryab.utils.okhttp;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noavaran.system.vira.baryab.enums.ShipmentEditTypeEnum;
import com.noavaran.system.vira.baryab.utils.okhttp.request.RateForLoadRequest;
import com.noavaran.system.vira.baryab.utils.okhttp.request.RequestUploadProfilePicture;
import com.noavaran.system.vira.baryab.utils.okhttp.response.AcceptDriverResponse;
import com.noavaran.system.vira.baryab.utils.okhttp.response.CommentResponse;
import com.noavaran.system.vira.baryab.utils.okhttp.response.VerifyResponse;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHelper {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_TEXT_HTML = MediaType.parse("text/html; charset=utf-8");

    private final OkHttpRequest okHttpRequest;

    private Context context;
    private final String url;
    private final MediaType mediaType;/**/

    public interface OnCallback {
        public abstract void onStart();

        public abstract void onResponse(JSONObject result);

        public abstract void onRequestReject(String message);

        public abstract void onFailure(String errorMessage);

        public abstract void onNoInternetConnection();

        public abstract void onFinish();
    }

    public OkHttpHelper(Context context, String url, MediaType mediaType) {
        this.context = context;
        this.url = url;
        this.mediaType = mediaType;

        okHttpRequest = new OkHttpRequest(context, url, mediaType);
    }

    public void getAddressFromLatLng(String box, OnCallback onCallback) {
        okHttpRequest.addQueryParameter("BBOX", box);

        getGeoRequest(onCallback);
    }

    public void doLogin(String phoneNumber, OnCallback onCallback) {
        okHttpRequest.addPathSegment("Operator");
        okHttpRequest.addQueryParameter("mobile", phoneNumber);

        getRequest(onCallback);
    }

    public void doVerify(int verifyCode, String phoneNumber, String userId, String registrationId, OnCallback onCallback) {
        try {
            okHttpRequest.addPathSegment("Verify");

            VerifyResponse verifyResponse = new VerifyResponse(verifyCode, phoneNumber, true, userId, registrationId);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(verifyResponse);

            postRequest(jsonInString, onCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getProfile(OnCallback onCallback) {
        okHttpRequest.addPathSegment("getProfile");

        getRequest(onCallback);
    }

    public void uploadProfilePicture(String encodedPicture, OnCallback onCallback) {
        try {
            okHttpRequest.addPathSegment("ChangePicture");

            RequestUploadProfilePicture request = new RequestUploadProfilePicture(encodedPicture);
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(request);

            postRequest(jsonInString, onCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getDrafts(OnCallback onCallback) {
        okHttpRequest.addPathSegment("getInit");

        getRequest(onCallback);
    }

    public void doNewLoading(String jsonLoading, OnCallback onCallback) {
        okHttpRequest.addPathSegment("Add");

        postRequest(jsonLoading, onCallback);
    }

    public void doEditLoading(String shipmentId, int type, String jsonLoading, OnCallback onCallback) {
        okHttpRequest.addPathSegment("put");
        okHttpRequest.addPathSegment(shipmentId);
        okHttpRequest.addPathSegment(type == ShipmentEditTypeEnum.edit.getValue() ? "false" : "true");

        putRequest(jsonLoading, onCallback);
    }

    public void getMyLoadings(String page, String limit, OnCallback onCallback) {
        okHttpRequest.addPathSegment("getAll");

        okHttpRequest.addQueryParameter("page", page);
        okHttpRequest.addQueryParameter("limit", limit);

        getRequest(onCallback);
    }

    public void getMyLoadingsDetails(String shipmentId, OnCallback onCallback) {
        okHttpRequest.addPathSegment("detail");
        okHttpRequest.addPathSegment(shipmentId);

        getRequest(onCallback);
    }

    public void deletetMyLoading(String shipmentId, OnCallback onCallback) {
        okHttpRequest.addPathSegment("Delete");
        okHttpRequest.addPathSegment(shipmentId);

        deleteRequest("", onCallback);
    }

    public void getApplicantDrivers(String page, String limit, String shipmentId, OnCallback onCallback) {
        okHttpRequest.addPathSegment("Drivers");

        okHttpRequest.addQueryParameter("page", page);
        okHttpRequest.addQueryParameter("limit", limit);

        okHttpRequest.addPathSegment(shipmentId);

        getRequest(onCallback);
    }

    public void doDriverAccesptForThisLoading(String shipmentId, String driverId, OnCallback onCallback) {
        try {
            okHttpRequest.addPathSegment("AcceptDraft");

            AcceptDriverResponse response = new AcceptDriverResponse(shipmentId, driverId);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(response);

            postRequest(jsonInString, onCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getListOfDriversForRating(OnCallback onCallback) {
        okHttpRequest.addPathSegment("DWithoutR");

        getRequest(onCallback);
    }

    public void getCarriedShipments(String page, String limit, OnCallback onCallback) {
        okHttpRequest.addPathSegment("LgetAll");

        okHttpRequest.addQueryParameter("page", page);
        okHttpRequest.addQueryParameter("limit", limit);

        getRequest(onCallback);
    }

    public void getCarryingShipments(String page, String limit, OnCallback onCallback) {
        okHttpRequest.addPathSegment("DgetAll");

        okHttpRequest.addQueryParameter("page", page);
        okHttpRequest.addQueryParameter("limit", limit);

        getRequest(onCallback);
    }

    public void doCancelShipmentRequest(String draftId, String description, OnCallback onCallback) {
        try {
            okHttpRequest.addPathSegment("CancelDraft");
            okHttpRequest.addQueryParameter("draftId", draftId);
            okHttpRequest.addQueryParameter("desc", description);

            putRequest("", onCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCarriedShipmentsDetails(String shipmentId, OnCallback onCallback) {
        okHttpRequest.addPathSegment("LDetail");
        okHttpRequest.addPathSegment(shipmentId);

        getRequest(onCallback);
    }

    public void getCarryingShipmentsDetails(String shipmentId, OnCallback onCallback) {
        okHttpRequest.addPathSegment("DDetail");
        okHttpRequest.addPathSegment(shipmentId);

        getRequest(onCallback);
    }

    public void getLastDriverLocation(String shipmentId, OnCallback onCallback) {
        okHttpRequest.addPathSegment("liveDrv");
        okHttpRequest.addQueryParameter("id", shipmentId);

        getRequest(onCallback);
    }

    public void sendUserComment(String subject, String body, OnCallback onCallback) {
        try {
            okHttpRequest.addPathSegment("Add");

            CommentResponse commentResponse = new CommentResponse(subject, body);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(commentResponse);

            postRequest(jsonInString, onCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendUncaughtException(String exception, OnCallback onCallback) {
        try {
            okHttpRequest.addPathSegment("AddCrash");

            postRequest(exception, onCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setRateForLoad(String draftId, float rate, OnCallback onCallback) {
        try {
            okHttpRequest.addPathSegment("RateForLoad");

            RateForLoadRequest rateForLoadRequest = new RateForLoadRequest(draftId, rate);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(rateForLoadRequest);

            postRequest(jsonInString, onCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doSharring(OnCallback onCallback) {
        okHttpRequest.addPathSegment("Intro");

        getRequest(onCallback);
    }

    public void getLastDriverLocation(int cellId, int cellLac, int cellSds, String cellStatus, final OnCallback onCallback) {
        try {
            onCallback.onStart();

            OkHttpClient httpClient = new OkHttpClient();

            HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();
            httpBuider.addQueryParameter("mcc", cellStatus);
            httpBuider.addQueryParameter("mnc", cellSds + "");
            httpBuider.addQueryParameter("lac", cellLac + "");
            httpBuider.addQueryParameter("cellid", cellId + "");

            final Request request = new Request.Builder().url(httpBuider.build()).build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onCallback.onFailure(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        onCallback.onRequestReject("خطا در ئریافت اطلاعات از سرور");
                    } else {
                        try {
                            String body = response.body().string();
                            JSONObject json = new JSONObject(body);
                            if (json.optInt("result") == 200)
                                onCallback.onResponse(json);
                            else
                                onCallback.onRequestReject("خطا در دریافت اطلاعات از سرور");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            onCallback.onFailure(ex.getMessage());
                        }
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            onCallback.onFailure(ex.getMessage());
        }
    }

    //----------------------------------------------------------------------------------------------

    private void postRequest(String postBody, final OnCallback onCallback) {
        onCallback.onStart();
        okHttpRequest.postRequest(postBody, new OkHttpRequest.OnCallback() {
            @Override
            public void onResponse(Call call, Response response, JSONObject result) {
                try {
                    if (result.getBoolean("result")) {
                        onCallback.onResponse(result);
                    } else {
                        onCallback.onRequestReject(result.getString("message"));
                    }

                    onCallback.onFinish();
                } catch (Exception ex) {
                    onCallback.onFailure(ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException exception) {
                onCallback.onFailure(exception.getMessage());
            }

            @Override
            public void onError(String errorMessage) {
                onCallback.onFailure(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                onCallback.onNoInternetConnection();
            }
        });
    }

    private void getRequest(final OnCallback onCallback) {
        onCallback.onStart();
        okHttpRequest.getRequest(new OkHttpRequest.OnCallback() {
            @Override
            public void onResponse(Call call, Response response, JSONObject result) {
                try {
                    if (result.getBoolean("result")) {
                        onCallback.onResponse(result);
                    } else {
                        onCallback.onRequestReject(result.getString("message"));
                    }

                    onCallback.onFinish();
                } catch (Exception ex) {
                    onCallback.onFailure(ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException exception) {
                onCallback.onFailure(exception.getMessage());
            }

            @Override
            public void onError(String errorMessage) {
                onCallback.onFailure(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                onCallback.onNoInternetConnection();
            }
        });
    }

    private void deleteRequest(String postBody, final OnCallback onCallback) {
        onCallback.onStart();
        okHttpRequest.deleteRequest(postBody, new OkHttpRequest.OnCallback() {
            @Override
            public void onResponse(Call call, Response response, JSONObject result) {
                try {
                    if (result.getBoolean("result")) {
                        onCallback.onResponse(result);
                    } else {
                        onCallback.onRequestReject(result.getString("message"));
                    }

                    onCallback.onFinish();
                } catch (Exception ex) {
                    onCallback.onFailure(ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException exception) {
                onCallback.onFailure(exception.getMessage());
            }

            @Override
            public void onError(String errorMessage) {
                onCallback.onFailure(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                onCallback.onNoInternetConnection();
            }
        });
    }

    private void putRequest(String postBody, final OnCallback onCallback) {
        onCallback.onStart();
        okHttpRequest.putRequest(postBody, new OkHttpRequest.OnCallback() {
            @Override
            public void onResponse(Call call, Response response, JSONObject result) {
                try {
                    if (result.getBoolean("result")) {
                        onCallback.onResponse(result);
                    } else {
                        onCallback.onRequestReject(result.getString("message"));
                    }

                    onCallback.onFinish();
                } catch (Exception ex) {
                    onCallback.onFailure(ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException exception) {
                onCallback.onFailure(exception.getMessage());
            }

            @Override
            public void onError(String errorMessage) {
                onCallback.onFailure(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                onCallback.onNoInternetConnection();
            }
        });
    }

    private void getGeoRequest(final OnCallback onCallback) {
        onCallback.onStart();
        okHttpRequest.getRequest(new OkHttpRequest.OnCallback() {
            @Override
            public void onResponse(Call call, Response response, JSONObject result) {
                try {
                    if (result != null) {
                        onCallback.onResponse(result);
                    } else {
                        onCallback.onRequestReject("عدم ارتباط با سرور");
                    }

                    onCallback.onFinish();
                } catch (Exception ex) {
                    onCallback.onFailure(ex.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException exception) {
                onCallback.onFailure(exception.getMessage());
            }

            @Override
            public void onError(String errorMessage) {
                onCallback.onFailure(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                onCallback.onNoInternetConnection();
            }
        });
    }
}