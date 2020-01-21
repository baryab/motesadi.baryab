package com.noavaran.system.vira.baryab.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.SPreferences;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.dialogs.CancelRequestReasonDialog;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.helpers.GoogleMapHelper;
import com.noavaran.system.vira.baryab.info.OnlineDriverTrackingInfo;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.DateUtil;
import com.noavaran.system.vira.baryab.utils.DialerUtil;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.NumberCommafy;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class CarryingShipmentDetailActivity extends BaseActivity implements OnMapReadyCallback {
    private CustomTextView tvTitle;
    private CustomTextView btnBack;
    private ImageView ivDriverPhoto;
    private CustomTextView rbDriverName;
    private RatingBar rbDriverRating;
    private CustomTextView tvDriverCarType;
    private CustomTextView tvDriverCarDetails;
    private CustomTextView tvPlateLicenseNumber;
    private CustomTextView tvPlateLicenseIranNumber;
    private MapView mapView;
    private CustomTextView btnMap;
    private ImageView ivShipmentPhoto;
    private CustomTextView btnContactToDriver;
    private CustomTextView btnCancelRequest;
    private CustomTextView etRegisterLoadingDate;
    private CustomTextView etLoadingType;
    private CustomTextView etLoadingWeight;
    private CustomTextView etCarType;
    private CustomTextView etCarProperLength;
    private CustomTextView etCarProperWidth;
    private CustomTextView etCarProperHeight;
    private CustomTextView etLoadingFare;
    private RelativeLayout rlAddress;
    private CustomTextView tvOriginMarker;
    private CustomTextView tvDestinationMarker;
    private RelativeLayout rlAddress1;
    private CustomTextView tvOriginMarker1;
    private CustomTextView tvDestinationMarker1;
    private RelativeLayout rlAddress2;
    private CustomTextView tvOriginMarker2;
    private CustomTextView tvDestinationMarker2;
    private CustomTextView tvSettlementAfterArrived;
    private CustomTextView tvShipmentIsTransit;
    private CustomTextView tvHavingWellTent;
    private CustomTextView tvDescription;
    private CustomTextView etDescription;
    private ImageView ivOwnerSignature;
    private CustomTextView etOwnerSignature;

    private String shipmentId;

    private String phoneNumber;

    private GoogleMapHelper googleMapHelper;

    private OnlineDriverTrackingInfo onlineDriverTrackingInfo;
    private String jsonOnlineDriverTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrying_shipment_detail);

        findViews();
        setViewsListeners();
        initComponents();
        getIntents();
        initMapView(savedInstanceState);
    }

    private void findViews() {
        tvTitle = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvTitle);
        btnBack = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_btnBack);
        ivDriverPhoto = (ImageView) findViewById(R.id.acCarryingShipmentDetail_ivDriverPhoto);
        rbDriverName = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_rbDriverName);
        rbDriverRating = (RatingBar) findViewById(R.id.acCarryingShipmentDetail_rbDriverRating);
        tvDriverCarType = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvDriverCarType);
        tvDriverCarDetails = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvDriverCarDetails);
        tvPlateLicenseNumber = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvPlateLicenseNumber);
        tvPlateLicenseIranNumber = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvPlateLicenseIranNumber);
        mapView = (MapView) findViewById(R.id.acCarryingShipmentDetail_mapView);
        btnMap = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_btnMap);
        ivShipmentPhoto = (ImageView) findViewById(R.id.acCarryingShipmentDetail_ivShipmentPhoto);
        btnContactToDriver = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_btnContactToDriver);
        btnCancelRequest = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_btnCancelRequest);
        etRegisterLoadingDate = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etRegisterLoadingDate);
        etLoadingType = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etLoadingType);
        etLoadingWeight = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etLoadingWeight);
        etCarType = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etCarType);
        etCarProperLength = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etCarProperLength);
        etCarProperWidth = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etCarProperWidth);
        etCarProperHeight = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etCarProperHeight);
        etLoadingFare = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etLoadingFare);
        rlAddress = (RelativeLayout) findViewById(R.id.acCarryingShipmentDetail_rlAddress);
        tvOriginMarker = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvOriginMarker);
        tvDestinationMarker = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvDestinationMarker);
        rlAddress1 = (RelativeLayout) findViewById(R.id.acCarryingShipmentDetail_rlAddress1);
        tvOriginMarker1 = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvOriginMarker1);
        tvDestinationMarker1 = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvDestinationMarker1);
        rlAddress2 = (RelativeLayout) findViewById(R.id.acCarryingShipmentDetail_rlAddress2);
        tvOriginMarker2 = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvOriginMarker2);
        tvDestinationMarker2 = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvDestinationMarker2);
        tvSettlementAfterArrived = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvSettlementAfterArrived);
        tvShipmentIsTransit = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvShipmentIsTransit);
        tvHavingWellTent = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvHavingWellTent);
        tvDescription = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvDescription);
        etDescription = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etDescription);
        ivOwnerSignature = (ImageView) findViewById(R.id.acCarryingShipmentDetail_ivOwnerSignature);
        etOwnerSignature = (CustomTextView) findViewById(R.id.acCarryingShipmentDetail_etOwnerSignature);
    }

    private void initComponents() {
        this.mapView.getMapAsync(this);
    }

    private void setViewsListeners() {
        btnBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });

        btnMap.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(CarryingShipmentDetailActivity.this).gotoActivityOnlineDriverTracking(shipmentId, jsonOnlineDriverTracking);
            }
        });

        btnContactToDriver.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DialerUtil.getInstance(CarryingShipmentDetailActivity.this).openTheDialerAppAndDoTheCallAutomatically(phoneNumber);
            }
        });

        btnCancelRequest.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                CancelRequestReasonDialog cancelRequestReasonDialog = new CancelRequestReasonDialog(CarryingShipmentDetailActivity.this);
                cancelRequestReasonDialog.setOnDialogButtonsClickListener(new CancelRequestReasonDialog.OnDialogButtonsClickListener() {
                    @Override
                    public void onConfirm(String reason) {
                        doCancelShipmentRequest(shipmentId, reason);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                cancelRequestReasonDialog.show();
            }
        });
    }

    private void getIntents() {
        if (getIntent().getExtras() != null) {
            shipmentId = getIntent().getExtras().getString("shipmentId");
        }
    }

    private void initMapView(Bundle savedInstanceState) {
        try {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            MapsInitializer.initialize(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initGoogleMap(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(false);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void initGoogleMapHelper(GoogleMap googleMap) {
        this.googleMapHelper = new GoogleMapHelper(CarryingShipmentDetailActivity.this, googleMap);
        this.googleMapHelper.setZoomLevel(3);
        this.googleMapHelper.showIranMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initGoogleMap(googleMap);
        initGoogleMapHelper(googleMap);

        getCarryingShipmentDetailFromServer();
    }

    private void getCarryingShipmentDetailFromServer() {
        OkHttpHelper okHttpHelper = new OkHttpHelper(CarryingShipmentDetailActivity.this, Configuration.API_GET_CARRYING_LOADING_DETAILS, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getCarryingShipmentsDetails(shipmentId, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                loadCarriedShipmentDetails(result);
            }

            @Override
            public void onRequestReject(String message) {
                dismissDialogProgress();
                showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                dismissDialogProgress();
                showToastError(errorMessage);
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

    public void loadCarriedShipmentDetails(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        JSONObject data = result.optJSONObject("data");
                        JSONObject dfo = data.optJSONObject("Dfo");
                        JSONObject ufo = dfo.optJSONObject("Ufo");
                        JSONObject tfo = data.optJSONObject("Tfo");
                        JSONArray orgCity = data.optJSONArray("orgCity");
                        JSONArray decCity = data.optJSONArray("decCity");
                        JSONObject draft = data.optJSONObject("d");

                        tvTitle.setText(draft.optString("TruckType").replace("/", "، ") + " " + orgCity.getJSONObject(0).optJSONObject("PlaceName").optString("city"));
                        Picasso.with(CarryingShipmentDetailActivity.this).load(Configuration.BASE_IMAGE_URL + ufo.optString("Pic")).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(ivDriverPhoto);
                        rbDriverName.setText(ufo.optString("Name"));
                        rbDriverRating.setRating(BigDecimal.valueOf(dfo.optDouble("Rate", 0)).floatValue());
                        tvDriverCarType.setText(tfo.optString("Type").replace("/", " ، "));

                        if (!draft.isNull("TruckMinHeight") && BigDecimal.valueOf(draft.optDouble("TruckMinHeight")).floatValue() > 0)
                            tvDriverCarDetails.setText("طول " + BigDecimal.valueOf(tfo.optDouble("minLength", 0)).floatValue() + " عرض " + BigDecimal.valueOf(tfo.optDouble("minWidth", 0)).floatValue() + " ارتفاع " + BigDecimal.valueOf(tfo.optDouble("minHeight", 0)).floatValue() + "");
                        else
                            tvDriverCarDetails.setText("طول " + BigDecimal.valueOf(tfo.optDouble("minLength", 0)).floatValue() + " عرض " + BigDecimal.valueOf(tfo.optDouble("minWidth", 0)).floatValue());

                        ObjectMapper mapper = new ObjectMapper();
                        onlineDriverTrackingInfo = new OnlineDriverTrackingInfo(ufo.optString("Mobile"), draft.optString("ShipmentType"), ufo.optString("Pic"), ufo.optString("Name"), tfo.optString("Type"), tvDriverCarDetails.getText().toString());
                        jsonOnlineDriverTracking = mapper.writeValueAsString(onlineDriverTrackingInfo);

                        try {
                            if (!GlobalUtils.IsNullOrEmpty(tfo.optString("plake"))) {
                                String[] splitPelak = tfo.optString("plake").split(" ");
                                tvPlateLicenseNumber.setText(splitPelak[0] + " " + splitPelak[1] + " " + splitPelak[2]);
                                tvPlateLicenseIranNumber.setText(splitPelak[3]);
                            } else {
                                tvPlateLicenseNumber.setText("");
                                tvPlateLicenseIranNumber.setText("");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        Picasso.with(CarryingShipmentDetailActivity.this).load(Configuration.BASE_IMAGE_URL + draft.optString("PicPath")).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(ivShipmentPhoto);

                        if (!GlobalUtils.IsNullOrEmpty(draft.optString("CreateDate")))
                            etRegisterLoadingDate.setText(PersianDateUtil.getPersianDate(draft.optLong("CreatedDate")));
                        else
                            etRegisterLoadingDate.setText(PersianDateUtil.getPersianDate(DateUtil.getDateFromDateString(DateUtil.getCurrentDateAsString(DateUtil.DATETIME_FORMAT), DateUtil.DATETIME_FORMAT)));

                        etLoadingType.setText(draft.optString("ShipmentType"));
                        etLoadingWeight.setText(BigDecimal.valueOf(draft.optDouble("Weight", 0)).floatValue() + " تن ");
                        etCarType.setText(draft.optString("TruckType").replace("/", " ، "));

                        etCarProperLength.setText("از " + BigDecimal.valueOf(draft.optDouble("TruckMinLength", 0)).floatValue() + " تا " + BigDecimal.valueOf(draft.optDouble("TruckMaxLength", 0)).floatValue() + " متر ");
                        etCarProperWidth.setText("از " + BigDecimal.valueOf(draft.optDouble("TruckMinWidth", 0)).floatValue() + " تا " + BigDecimal.valueOf(draft.optDouble("TruckMaxWidth", 0)).floatValue() + " متر ");
                        if (!GlobalUtils.IsNullOrEmpty(String.valueOf(draft.optDouble("TruckMinHeight"))) && draft.optDouble("TruckMinHeight") > 0)
                            etCarProperHeight.setText("از " + BigDecimal.valueOf(draft.optDouble("TruckMinHeight", 0)).floatValue() + " تا " + BigDecimal.valueOf(draft.optDouble("TruckMaxHeight", 0)).floatValue() + " متر ");
                        else {
                            findViewById(R.id.acCarryingShipmentDetail_tvCarProperHeight).setVisibility(View.GONE);
                            etCarProperHeight.setVisibility(View.GONE);
                        }

                        if (draft.optString("MoneyType").equals("توافقی")) {
                            etLoadingFare.setVisibility(View.GONE);
                            etLoadingFare.setText("");
                        } else {
                            etLoadingFare.setVisibility(View.VISIBLE);
                            etLoadingFare.setText(NumberCommafy.decimalFormatCommafy(draft.optInt("Money", 0) + "") + " تومان ");
                        }

                        ((CustomTextView) findViewById(R.id.acCarryingShipmentDetail_tvLoadingFareHint)).setText(" (" + draft.optString("MoneyType", "نامشخص") + ") ");

                        phoneNumber = ufo.optString("Mobile");

                        if (orgCity.length() == 1) {
                            rlAddress.setVisibility(View.VISIBLE);
                            tvOriginMarker.setText(orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("city"));
                        } else if (orgCity.length() == 2) {
                            rlAddress.setVisibility(View.VISIBLE);
                            tvOriginMarker.setText(orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("city"));
                            rlAddress1.setVisibility(View.VISIBLE);
                            tvOriginMarker1.setText(orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + orgCity.optJSONObject(1).optJSONObject("PlaceName").optString("city"));
                        } else if (orgCity.length() == 3) {
                            rlAddress.setVisibility(View.VISIBLE);
                            tvOriginMarker.setText(orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("city"));
                            rlAddress1.setVisibility(View.VISIBLE);
                            tvOriginMarker1.setText(orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + orgCity.optJSONObject(1).optJSONObject("PlaceName").optString("city"));
                            rlAddress2.setVisibility(View.VISIBLE);
                            tvOriginMarker2.setText(orgCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + orgCity.optJSONObject(2).optJSONObject("PlaceName").optString("city"));
                        }

                        if (decCity.length() == 1) {
                            rlAddress.setVisibility(View.VISIBLE);
                            tvDestinationMarker.setText(decCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + decCity.optJSONObject(0).optJSONObject("PlaceName").optString("city"));

                            hideNextDestinationViews(1);
                        } else if (decCity.length() == 2) {
                            rlAddress.setVisibility(View.VISIBLE);
                            tvDestinationMarker.setText(decCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + decCity.optJSONObject(0).optJSONObject("PlaceName").optString("city"));
                            rlAddress1.setVisibility(View.VISIBLE);
                            tvDestinationMarker1.setText(decCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + decCity.optJSONObject(1).optJSONObject("PlaceName").optString("city"));

                            hideNextDestinationViews(2);

                            if (orgCity.length() < 2) {
                                findViewById(R.id.acCarryingShipmentDetail_ivOriginMarker1).setVisibility(View.GONE);
                                findViewById(R.id.acCarryingShipmentDetail_tvOriginMarker1).setVisibility(View.GONE);
                            }
                        } else if (decCity.length() == 3) {
                            rlAddress.setVisibility(View.VISIBLE);
                            tvDestinationMarker.setText(decCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + decCity.optJSONObject(0).optJSONObject("PlaceName").optString("city"));
                            rlAddress1.setVisibility(View.VISIBLE);
                            tvDestinationMarker1.setText(decCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + decCity.optJSONObject(1).optJSONObject("PlaceName").optString("city"));
                            rlAddress2.setVisibility(View.VISIBLE);
                            tvDestinationMarker2.setText(decCity.optJSONObject(0).optJSONObject("PlaceName").optString("province") + "، " + decCity.optJSONObject(2).optJSONObject("PlaceName").optString("city"));

                            if (orgCity.length() < 2) {
                                findViewById(R.id.acCarryingShipmentDetail_ivOriginMarker1).setVisibility(View.GONE);
                                findViewById(R.id.acCarryingShipmentDetail_tvOriginMarker1).setVisibility(View.GONE);
                            }
                            if (orgCity.length() < 3) {
                                findViewById(R.id.acCarryingShipmentDetail_ivOriginMarker2).setVisibility(View.GONE);
                                findViewById(R.id.acCarryingShipmentDetail_tvOriginMarker2).setVisibility(View.GONE);
                            }
                        }

                        if (draft.optBoolean("AfterRecive")) {
                            tvSettlementAfterArrived.setVisibility(View.VISIBLE);
                        } else {
                            tvSettlementAfterArrived.setVisibility(View.GONE);
                        }

                        if (draft.optBoolean("Goback")) {
                            tvShipmentIsTransit.setVisibility(View.VISIBLE);
                        } else {
                            tvShipmentIsTransit.setVisibility(View.GONE);
                        }

                        if (draft.optBoolean("HasTent")) {
                            tvHavingWellTent.setVisibility(View.VISIBLE);
                        } else {
                            tvHavingWellTent.setVisibility(View.GONE);
                        }

                        if (GlobalUtils.IsNullOrEmpty(draft.optString("Desc"))) {
                            tvDescription.setVisibility(View.GONE);
                            etDescription.setVisibility(View.GONE);
                        } else {
                            tvDescription.setVisibility(View.VISIBLE);
                            etDescription.setVisibility(View.VISIBLE);
                            etDescription.setText(draft.optString("Desc"));
                        }

                        Picasso.with(CarryingShipmentDetailActivity.this).load(Configuration.BASE_IMAGE_URL + data.optString("sign")).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(ivOwnerSignature);
                        etOwnerSignature.setText(data.optString("Name"));

                        for (int i = 0; i < orgCity.length(); i++) {
                            float lat = BigDecimal.valueOf(orgCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("latitude", 0)).floatValue();
                            float lon = BigDecimal.valueOf(orgCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("longitude", 0)).floatValue();

                            googleMapHelper.addCustomMarkerToMap(lat, lon, R.drawable.ic_origin_small, false);
                        }

                        for (int i = 0; i < decCity.length(); i++) {
                            float lat = BigDecimal.valueOf(decCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("latitude", 0)).floatValue();
                            float lon = BigDecimal.valueOf(decCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("longitude", 0)).floatValue();

                            googleMapHelper.addCustomMarkerToMap(lat, lon, R.drawable.ic_destination_small, false);
                        }
                    } else {
                        showToastError(result.getString("message"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void doCancelShipmentRequest(String draftId, String description) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(CarryingShipmentDetailActivity.this, Configuration.API_GET_LOADING, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.doCancelShipmentRequest(draftId, description, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                showDialogProgress("در حال ارسال اطلاعات به سرور");
            }

            @Override
            public void onResponse(JSONObject result) {
                loadCancelShipmentRequest(result);
            }

            @Override
            public void onRequestReject(String message) {
                dismissDialogProgress();
                showToastWarning(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                dismissDialogProgress();
                showToastError(errorMessage);
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

    private void loadCancelShipmentRequest(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        showToastInfo("لغو درخواست با موفقیت انجام شد");

                        SPreferences.getInstance(CarryingShipmentDetailActivity.this).setRefresh(true);

                        finish();
                    } else {
                        showToastError(result.getString("message"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void hideNextDestinationViews(int destinationLenght) {
        if (destinationLenght == 1) {
            findViewById(R.id.acCarryingShipmentDetail_ivDestinationMarker1).setVisibility(View.GONE);
            findViewById(R.id.acCarryingShipmentDetail_tvDestinationMarker1).setVisibility(View.GONE);
            findViewById(R.id.acCarryingShipmentDetail_ivDestinationMarker2).setVisibility(View.GONE);
            findViewById(R.id.acCarryingShipmentDetail_tvDestinationMarker2).setVisibility(View.GONE);
        } else if (destinationLenght == 2) {
            findViewById(R.id.acCarryingShipmentDetail_ivDestinationMarker2).setVisibility(View.GONE);
            findViewById(R.id.acCarryingShipmentDetail_tvDestinationMarker2).setVisibility(View.GONE);
        }
    }
}