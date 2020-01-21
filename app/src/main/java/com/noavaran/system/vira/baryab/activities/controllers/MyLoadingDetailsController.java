package com.noavaran.system.vira.baryab.activities.controllers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.activities.MyLoadingDetailsActivity;
import com.noavaran.system.vira.baryab.activities.delegates.MyLoadingDetailsDelegate;
import com.noavaran.system.vira.baryab.activities.models.MyLoadingDetailsModel;
import com.noavaran.system.vira.baryab.database.models.LoadingFareType;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.googlemap.DirectionUtil;
import com.noavaran.system.vira.baryab.utils.DateUtil;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.NumberCommafy;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.noavaran.system.vira.baryab.utils.okhttp.response.ShipmentDetailResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class MyLoadingDetailsController implements MyLoadingDetailsDelegate.Controller {
    private final MyLoadingDetailsActivity activity;
    private final MyLoadingDetailsDelegate.View view;
    private String shipmentId;
    private MyLoadingDetailsModel model;
    private ShipmentDetailResponse data;

    public MyLoadingDetailsController(MyLoadingDetailsActivity activity, MyLoadingDetailsDelegate.View view, String shipmentId) {
        this.activity = activity;
        this.view = view;
        this.shipmentId = shipmentId;

        model = new MyLoadingDetailsModel();
    }

    @Override
    public void deleteLoadingFromServer(String shipmentId) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(activity, Configuration.API_DELETE_LOADING, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.deletetMyLoading(shipmentId, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                activity.showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                SPreferences.getInstance(activity).setRefresh(true);
            }

            @Override
            public void onRequestReject(String message) {
                activity.dismissDialogProgress();
                activity.showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                activity.dismissDialogProgress();
                activity.showToastError(errorMessage);
            }

            @Override
            public void onNoInternetConnection() {
                activity.dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                activity.dismissDialogProgress();

                Intent returnIntent = new Intent();
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();
            }
        });
    }

    @Override
    public String getShipmentData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(data);

            return jsonInString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void getMyLoadingDetailsFomServer(String shipmentId, final int mapViewWidth, final int mapViewHeight) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(activity, Configuration.API_GET_LOADING_DETAIL, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getMyLoadingsDetails(shipmentId, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                activity.showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                loadMyLoadingDetails(result, mapViewWidth, mapViewHeight);
            }

            @Override
            public void onRequestReject(String message) {
                activity.showToastWarning(message);
                activity.dismissDialogProgress();
            }

            @Override
            public void onFailure(String errorMessage) {
                activity.showToastError(errorMessage);
                activity.dismissDialogProgress();
            }

            @Override
            public void onNoInternetConnection() {
                activity.dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                activity.dismissDialogProgress();
            }
        });
    }

    private void loadMyLoadingDetails(final JSONObject result, final int mapViewWidth, final int mapViewHeight) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                        data = mapper.readValue(result.optString("data"), ShipmentDetailResponse.class);

                        for (int i = 0 ; i < result.optJSONObject("data").optJSONArray("listOriginPlacesInfo").length() ; i++) {
                            data.getListOriginPlacesInfo().get(i).getAddressInfo().setId(result.optJSONObject("data").optJSONArray("listOriginPlacesInfo").optJSONObject(i).optJSONObject("addressInfo").optInt("Id", 0));
                        }

                        for (int i = 0 ; i < result.optJSONObject("data").optJSONArray("listDestinationPlacesInfo").length() ; i++) {
                            data.getListDestinationPlacesInfo().get(i).getAddressInfo().setId(result.optJSONObject("data").optJSONArray("listDestinationPlacesInfo").optJSONObject(i).optJSONObject("addressInfo").optInt("Id", 0));
                        }

                        List<TruckType> listTruckType = TruckType.findWithQuery(TruckType.class, "Select full_name from " + TruckType.SUGAR + " where truck_type_id = ?", String.valueOf(data.getTruckTypeId()));
                        List<LoadingFareType> listLoadingFareType = LoadingFareType.findWithQuery(LoadingFareType.class, "Select name from " + LoadingFareType.SUGAR + " where loading_fare_type_id = ?", String.valueOf(data.getMoneyTypeId()));

                        view.getTvCarType().setText(listTruckType.get(0).getFullName().replace("/", " ، ") + " " + data.getListDestinationPlacesInfo().get(data.getListDestinationPlacesInfo().size() - 1).getAddressInfo().getProvince() + "، " + data.getListDestinationPlacesInfo().get(data.getListDestinationPlacesInfo().size() - 1).getAddressInfo().getCity());
                        Picasso.with(activity).load(Configuration.BASE_IMAGE_URL + data.getPicPath()).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(view.getIvLoadingPicture());

                        if (!GlobalUtils.IsNullOrEmpty(data.getCreateDate()))
                            view.getEtRegisterTime().setText(PersianDateUtil.getPersianDate(DateUtil.getDateFromDateString(data.getCreateDate(), DateUtil.DATETIME_FORMAT).getTime()));
                        else
                            view.getEtRegisterTime().setText(PersianDateUtil.getPersianDate(DateUtil.getDateFromDateString(DateUtil.getCurrentDateAsString(DateUtil.DATETIME_FORMAT), DateUtil.DATETIME_FORMAT).getTime()));

                        view.getEtShipmentType().setText(data.getShipmentType());
                        view.getEtWeight().setText(String.valueOf("" + data.getWeight() + " تن"));
                        view.getEtTruckType().setText(listTruckType.get(0).getFullName().replace("/", " ، "));

                        view.getEtTruckLength().setText("از " + data.getTruckMinLength() + " تا " + data.getTruckMaxLength() + " متر");
                        view.getEtTruckWidth().setText("از " + data.getTruckMinWidth() + " تا " + data.getTruckMaxWidth() + " متر");
                        if (data.getTruckMinHeight() > 0 && data.getTruckMaxHeight() > 0)
                            view.getEtTruckHeight().setText("از " + data.getTruckMinHeight() + " تا " + data.getTruckMaxHeight() + " متر");
                        else {
                            view.getTvTruckHeight().setVisibility(View.GONE);
                            view.getEtTruckHeight().setVisibility(View.GONE);
                        }

                        view.getEtLoadingFareType().setText(listLoadingFareType.get(0).getName());
                        view.getEtLoadingFare().setText(NumberCommafy.decimalFormatCommafy(String.valueOf(data.getMoney())) + " تومان ");
                        if (data.getMoneyTypeId() == 2) {
                            view.getTvLoadingFare().setVisibility(View.GONE);
                            view.getEtLoadingFare().setVisibility(View.GONE);
                        } else {
                            view.getTvLoadingFare().setVisibility(View.VISIBLE);
                            view.getEtLoadingFare().setVisibility(View.VISIBLE);
                        }

                        if (!GlobalUtils.IsNullOrEmpty(String.valueOf(data.getLoadDate())))
                            view.getEtLoadingTime().setText(PersianDateUtil.getPersianDate(data.getLoadDate()));
                        else
                            view.getEtLoadingTime().setText("تعیین نشده");

                        view.getEtLoadingDescription().setText(data.getDesc());

                        if (data.getStoreCost() <= 0) {
                            view.getTvWareHouseSpending().setVisibility(View.GONE);
                            view.getEtWareHouseSpending().setVisibility(View.GONE);
                        } else {
                            view.getEtWareHouseSpending().setText(NumberCommafy.decimalFormatCommafy(String.valueOf(data.getStoreCost())) + " تومان ");
                        }

                        if (data.getDisChDate() > 0) {
                            activity.findViewById(R.id.acMyLoadingDetails_tvWareDischargeTime).setVisibility(View.VISIBLE);
                            view.getEtWareDischargeTime().setVisibility(View.VISIBLE);
                            view.getEtWareDischargeTime().setText(PersianDateUtil.getPersianDate(data.getDisChDate()));
                        }
                        else {
                            activity.findViewById(R.id.acMyLoadingDetails_tvWareDischargeTime).setVisibility(View.GONE);
                            view.getEtWareDischargeTime().setVisibility(View.GONE);
                        }

                        if (!GlobalUtils.IsNullOrEmpty(String.valueOf(data.getExpDate())))
                            view.getEtWareExpirationTime().setText(PersianDateUtil.getPersianDate(data.getExpDate()));
                        else
                            view.getEtWareExpirationTime().setText("تعیین نشده");

                        view.getTvSettlementAfterArrived().setVisibility(data.isAfterRecive() ? View.VISIBLE : View.GONE);
                        view.getTvShipmentIsTransit().setVisibility(data.isGoback() ? View.VISIBLE : View.GONE);
                        view.getTvHavingWellTent().setVisibility(data.isHasTent() ? View.VISIBLE : View.GONE);

                        double originLatitude = data.getListOriginPlacesInfo().size() > 0 ? data.getListOriginPlacesInfo().get(0).getLatLngInfo().getLatitude() : 0;
                        double originLongitude = data.getListOriginPlacesInfo().size() > 0 ? data.getListOriginPlacesInfo().get(0).getLatLngInfo().getLongitude() : 0;
                        double destinationLatitude = data.getListDestinationPlacesInfo().size() > 0 ? data.getListDestinationPlacesInfo().get(data.getListDestinationPlacesInfo().size() - 1).getLatLngInfo().getLatitude() : 0;
                        double destinationLongitude = data.getListDestinationPlacesInfo().size() > 0 ? data.getListDestinationPlacesInfo().get(data.getListDestinationPlacesInfo().size() - 1).getLatLngInfo().getLongitude() : 0;
                        if (originLatitude > 0 && originLongitude > 0 && destinationLatitude > 0 && destinationLongitude > 0) {
                            DirectionUtil directionUtil = new DirectionUtil(activity, view.getGoogleMapHelper().getGoogleMap(), activity.getResources().getIdentifier("ic_origin_small", "drawable", activity.getPackageName()), activity.getResources().getIdentifier("ic_destination_small", "drawable", activity.getPackageName()), mapViewWidth, mapViewHeight);
                            directionUtil.doDirection(new LatLng(originLatitude, originLongitude), new LatLng(destinationLatitude, destinationLongitude));
                        }

                        if (data.getListOriginPlacesInfo().size() == 1) {
                            view.getRlAddress().setVisibility(View.VISIBLE);
                            view.getTvOriginMarker().setText(data.getListOriginPlacesInfo().get(0).getAddressInfo().getProvince() + "، " + data.getListOriginPlacesInfo().get(0).getAddressInfo().getCity());
                        } else if (data.getListOriginPlacesInfo().size() == 2) {
                            view.getRlAddress().setVisibility(View.VISIBLE);
                            view.getTvOriginMarker().setText(data.getListOriginPlacesInfo().get(0).getAddressInfo().getProvince() + "، " + data.getListOriginPlacesInfo().get(0).getAddressInfo().getCity());
                            view.getRlAddress1().setVisibility(View.VISIBLE);
                            view.getTvOriginMarker1().setText(data.getListOriginPlacesInfo().get(1).getAddressInfo().getProvince() + "، " + data.getListOriginPlacesInfo().get(1).getAddressInfo().getCity());
                        } else if (data.getListOriginPlacesInfo().size() == 3) {
                            view.getRlAddress().setVisibility(View.VISIBLE);
                            view.getTvOriginMarker().setText(data.getListOriginPlacesInfo().get(0).getAddressInfo().getProvince() + "، " + data.getListOriginPlacesInfo().get(0).getAddressInfo().getCity());
                            view.getRlAddress1().setVisibility(View.VISIBLE);
                            view.getTvOriginMarker1().setText(data.getListOriginPlacesInfo().get(1).getAddressInfo().getProvince() + "، " + data.getListOriginPlacesInfo().get(1).getAddressInfo().getCity());
                            view.getRlAddress2().setVisibility(View.VISIBLE);
                            view.getTvOriginMarker2().setText(data.getListOriginPlacesInfo().get(2).getAddressInfo().getProvince() + "، " + data.getListOriginPlacesInfo().get(2).getAddressInfo().getCity());
                        }

                        if (data.getListDestinationPlacesInfo().size() == 1) {
                            view.getRlAddress().setVisibility(View.VISIBLE);
                            view.getTvDestinationMarker().setText(data.getListDestinationPlacesInfo().get(0).getAddressInfo().getProvince() + "، " + data.getListDestinationPlacesInfo().get(0).getAddressInfo().getCity());

                            hideNextDestinationViews(1);
                        } else if (data.getListDestinationPlacesInfo().size() == 2) {
                            view.getRlAddress().setVisibility(View.VISIBLE);
                            view.getTvDestinationMarker().setText(data.getListDestinationPlacesInfo().get(0).getAddressInfo().getProvince() + "، " + data.getListDestinationPlacesInfo().get(0).getAddressInfo().getCity());
                            view.getRlAddress1().setVisibility(View.VISIBLE);
                            view.getTvDestinationMarker1().setText(data.getListDestinationPlacesInfo().get(1).getAddressInfo().getProvince() + "، " + data.getListDestinationPlacesInfo().get(1).getAddressInfo().getCity());

                            hideNextDestinationViews(2);

                            if (data.getListOriginPlacesInfo().size() < 2) {
                                activity.findViewById(R.id.acMyLoadingDetails_ivOriginMarker1).setVisibility(View.GONE);
                                activity.findViewById(R.id.acMyLoadingDetails_tvOriginMarker1).setVisibility(View.GONE);
                            }
                        } else if (data.getListDestinationPlacesInfo().size() == 3) {
                            view.getRlAddress().setVisibility(View.VISIBLE);
                            view.getTvDestinationMarker().setText(data.getListDestinationPlacesInfo().get(0).getAddressInfo().getProvince() + "، " + data.getListDestinationPlacesInfo().get(0).getAddressInfo().getCity());
                            view.getRlAddress1().setVisibility(View.VISIBLE);
                            view.getTvDestinationMarker1().setText(data.getListDestinationPlacesInfo().get(1).getAddressInfo().getProvince() + "، " + data.getListDestinationPlacesInfo().get(1).getAddressInfo().getCity());
                            view.getRlAddress2().setVisibility(View.VISIBLE);
                            view.getTvDestinationMarker2().setText(data.getListDestinationPlacesInfo().get(2).getAddressInfo().getProvince() + "، " + data.getListDestinationPlacesInfo().get(2).getAddressInfo().getCity());

                            if (data.getListOriginPlacesInfo().size() < 2) {
                                activity.findViewById(R.id.acMyLoadingDetails_ivOriginMarker1).setVisibility(View.GONE);
                                activity.findViewById(R.id.acMyLoadingDetails_tvOriginMarker1).setVisibility(View.GONE);
                            } if (data.getListOriginPlacesInfo().size() < 3) {
                                activity.findViewById(R.id.acMyLoadingDetails_ivOriginMarker2).setVisibility(View.GONE);
                                activity.findViewById(R.id.acMyLoadingDetails_tvOriginMarker2).setVisibility(View.GONE);
                            }
                        }
                    } else {
                        activity.showToastWarning(result.getString("message"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void hideNextDestinationViews(int destinationLenght) {
        if (destinationLenght == 1) {
            activity.findViewById(R.id.acMyLoadingDetails_ivDestinationMarker1).setVisibility(View.GONE);
            activity.findViewById(R.id.acMyLoadingDetails_tvDestinationMarker1).setVisibility(View.GONE);
            activity.findViewById(R.id.acMyLoadingDetails_ivDestinationMarker2).setVisibility(View.GONE);
            activity.findViewById(R.id.acMyLoadingDetails_tvDestinationMarker2).setVisibility(View.GONE);
        } else if (destinationLenght == 2) {
            activity.findViewById(R.id.acMyLoadingDetails_ivDestinationMarker2).setVisibility(View.GONE);
            activity.findViewById(R.id.acMyLoadingDetails_tvDestinationMarker2).setVisibility(View.GONE);
        }
    }
}