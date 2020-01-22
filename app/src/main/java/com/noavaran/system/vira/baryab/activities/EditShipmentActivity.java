package com.noavaran.system.vira.baryab.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noavaran.system.vira.baryab.AppController;
import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.controllers.EditShipmentController;
import com.noavaran.system.vira.baryab.activities.delegates.EditShipmentDelegate;
import com.noavaran.system.vira.baryab.customviews.CustomEditText;
import com.noavaran.system.vira.baryab.customviews.CustomSwitchButton;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.customviews.ExpandableLayout;
import com.noavaran.system.vira.baryab.database.models.LoadingFareType;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.enums.ShipmentEditTypeEnum;
import com.noavaran.system.vira.baryab.helpers.ActivitiesHelpers;
import com.noavaran.system.vira.baryab.info.AddressInfo;
import com.noavaran.system.vira.baryab.info.LatLngInfo;
import com.noavaran.system.vira.baryab.info.PlacesInfo;
import com.noavaran.system.vira.baryab.info.TruckTypeInfo;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.EditTextUtil;
import com.noavaran.system.vira.baryab.utils.ImagePicker;
import com.noavaran.system.vira.baryab.utils.ImageUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.response.ShipmentDetailResponse;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditShipmentActivity extends BaseActivity implements EditShipmentDelegate.View {
    private CustomTextView btnLocation;
    private CustomTextView btnCarChoose;
    private CustomTextView tvCarChooseHint;
    private ExpandableLayout elCarLength;
    private CustomTextView btnCarLengthArrowDropDown;
    private CustomTextView tvCarLengthHeaderLabel;
    private CustomEditText etCarLengthFrom;
    private CustomEditText etCarLengthTo;
    private ExpandableLayout elCarWidth;
    private CustomTextView btnCarWidthArrowDropDown;
    private CustomTextView tvCarWidthHeaderLabel;
    private CustomEditText etCarWidthFrom;
    private CustomEditText etCarWidthTo;
    private ExpandableLayout elCarHeight;
    private CustomTextView btnCarHeightArrowDropDown;
    private CustomTextView tvCarHeightHeaderLabel;
    private CustomEditText etCarHeightFrom;
    private CustomEditText etCarHeightTo;
    private CustomEditText etLoadingType;
    private CustomEditText etLoadingWeight;
    private ExpandableLayout elLoadingFareType;
    private CustomTextView btnLoadingFareTypeArrowDropDown;
    private RecyclerView rvLoadingFareType;
    private CustomTextView tvLoadingFareTypeHint;
    private CustomEditText etLoadingFare;
    private CustomTextView btnLoadingTime;
    private CustomTextView tvLoadingTimeHint;
    private CustomEditText etّDescription;
    private CustomTextView btnChooseLoadingPhoto;
    private ImageView ivLoadingPhotoPreview;
    private ExpandableLayout elMoreDetails;
    private RelativeLayout rlMoreDetails;
    private CustomTextView btnMoreDetailsArrowDropDown;
    private CustomEditText etSpendingWarehouse;
    private CustomTextView tvDetermineDischargeTimeHint;
    private CustomTextView btnDetermineDischargeTime;
    private CustomTextView tvDetermineExpirationTimeHint;
    private CustomTextView btnDetermineExpirationTime;
    private CustomSwitchButton btnPaymentIsPaidAfterLoadingArriveToDestination;
    private CustomSwitchButton btnLoadingIsGoingRound;
    private CustomSwitchButton btnHavingIntactTent;
    private RelativeLayout btnEdit;

    private EditShipmentController controller;

    private String shipmentDetail;
    private int type;

    private int rlMoreDetailsMeasuredHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shipment);

        findViews();
        initComponents();
        setViewsListeners();
        getIntents();
    }

    private void findViews() {
        this.btnLocation = findViewById(R.id.acEditShipment_btnLocation);

        this.btnCarChoose = findViewById(R.id.acEditShipment_btnCarChoose);
        this.tvCarChooseHint = findViewById(R.id.acEditShipment_tvCarChooseHint);

        this.elCarLength = findViewById(R.id.acEditShipment_elCarLength);
        this.btnCarLengthArrowDropDown = findViewById(R.id.acEditShipment_btnCarLengthArrowDropDown);
        this.tvCarLengthHeaderLabel = findViewById(R.id.acEditShipment_tvCarLengthHeaderLabel);
        this.etCarLengthFrom = findViewById(R.id.acEditShipment_etCarLengthFrom);
        this.etCarLengthTo = findViewById(R.id.acEditShipment_etCarLengthTo);

        this.elCarWidth = findViewById(R.id.acEditShipment_elCarWidth);
        this.btnCarWidthArrowDropDown = findViewById(R.id.acEditShipment_btnCarWidthArrowDropDown);
        this.tvCarWidthHeaderLabel = findViewById(R.id.acEditShipment_tvCarWidthHeaderLabel);
        this.etCarWidthFrom = findViewById(R.id.acEditShipment_etCarWidthFrom);
        this.etCarWidthTo = findViewById(R.id.acEditShipment_etCarWidthTo);

        this.elCarHeight = findViewById(R.id.acEditShipment_elCarHeight);
        this.btnCarHeightArrowDropDown = findViewById(R.id.acEditShipment_btnCarHeightArrowDropDown);
        this.tvCarHeightHeaderLabel = findViewById(R.id.acEditShipment_tvCarHeightHeaderLabel);
        this.etCarHeightFrom = findViewById(R.id.acEditShipment_etCarHeightFrom);
        this.etCarHeightTo = findViewById(R.id.acEditShipment_etCarHeightTo);

        this.etLoadingType = findViewById(R.id.acEditShipment_etLoadingType);
        this.etLoadingWeight = findViewById(R.id.acEditShipment_etLoadingWeight);

        this.tvLoadingFareTypeHint = findViewById(R.id.acEditShipment_tvLoadingFareTypeHint);
        this.elLoadingFareType = findViewById(R.id.acEditShipment_elLoadingFareType);
        this.btnLoadingFareTypeArrowDropDown = findViewById(R.id.acEditShipment_btnLoadingFareTypeArrowDropDown);
        this.rvLoadingFareType = findViewById(R.id.acEditShipment_rvLoadingFareType);

        this.etLoadingFare = findViewById(R.id.acEditShipment_etLoadingFare);

        this.btnLoadingTime = findViewById(R.id.acEditShipment_btnLoadingTime);
        this.tvLoadingTimeHint = findViewById(R.id.acEditShipment_tvLoadingTimeHint);

        this.etّDescription = findViewById(R.id.acEditShipment_etّDescription);

        this.btnChooseLoadingPhoto = findViewById(R.id.acEditShipment_btnChooseLoadingPhoto);
        this.ivLoadingPhotoPreview = findViewById(R.id.acEditShipment_ivLoadingPhotoPreview);

        this.elMoreDetails = findViewById(R.id.acEditShipment_elMoreDetails);
        this.rlMoreDetails = findViewById(R.id.acEditShipment_rlMoreDetails);
        this.btnMoreDetailsArrowDropDown = findViewById(R.id.acEditShipment_btnMoreDetailsArrowDropDown);
        this.etSpendingWarehouse = findViewById(R.id.acEditShipment_etSpendingWarehouse);
        this.tvDetermineDischargeTimeHint = findViewById(R.id.acEditShipment_tvDetermineDischargeTimeHint);
        this.btnDetermineDischargeTime = findViewById(R.id.acEditShipment_btnDetermineDischargeTime);
        this.tvDetermineExpirationTimeHint = findViewById(R.id.acEditShipment_tvDetermineExpirationTimeHint);
        this.btnDetermineExpirationTime = findViewById(R.id.acEditShipment_btnDetermineExpirationTime);
        this.btnPaymentIsPaidAfterLoadingArriveToDestination = findViewById(R.id.acEditShipment_btnPaymentIsPaidAfterLoadingArriveToDestination);
        this.btnLoadingIsGoingRound = findViewById(R.id.acEditShipment_btnLoadingIsGoingRound);
        this.btnHavingIntactTent = findViewById(R.id.acEditShipment_btnHavingIntactTent);

        this.btnEdit = findViewById(R.id.acEditShipment_btnEdit);
    }

    private void initComponents() {
        controller = new EditShipmentController(EditShipmentActivity.this, this);

        registerReceiver(controller.broadcastRequestCarType, new IntentFilter("com.noavaran.system.vira.baryab.REQUEST_CARType"));

        ImagePicker.setMinQuality(450, 450);

        rlMoreDetailsMeasuredHeight = rlMoreDetails.getLayoutParams().height;
    }

    private void setViewsListeners() {
        btnLocation.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(EditShipmentActivity.this).gotoActivityMaps(EditShipmentActivity.this, controller.getPlaceInfo());
            }
        });

        btnCarChoose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                controller.getCarTypeChooserDialog().show(getSupportFragmentManager(), "CarTypeChooserDialogTag");
            }
        });

        this.elCarLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elCarWidth.isExpanded()) elCarWidth.toggleExpansion();
                if (elCarHeight.isExpanded()) elCarHeight.toggleExpansion();

                EditTextUtil.getInstance(EditShipmentActivity.this).setRequestFocus(etCarLengthFrom);
                elCarLength.toggleExpansion();

                elCarLength.post(new Runnable() {
                    @Override
                    public void run() {
                        if (elCarLength.isExpanded())
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight;
                        else
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight + elCarLength.getLayoutParams().height;
                    }
                });
            }
        });

        this.elCarWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elCarLength.isExpanded()) elCarLength.toggleExpansion();
                if (elCarHeight.isExpanded()) elCarHeight.toggleExpansion();

                EditTextUtil.getInstance(EditShipmentActivity.this).setRequestFocus(etCarWidthFrom);
                elCarWidth.toggleExpansion();

                elCarWidth.post(new Runnable() {
                    @Override
                    public void run() {
                        if (elCarWidth.isExpanded())
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight;
                        else
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight + elCarWidth.getLayoutParams().height;
                    }
                });
            }
        });

        this.elCarHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elCarLength.isExpanded()) elCarLength.toggleExpansion();
                if (elCarWidth.isExpanded()) elCarWidth.toggleExpansion();

                EditTextUtil.getInstance(EditShipmentActivity.this).setRequestFocus(etCarHeightFrom);
                elCarHeight.toggleExpansion();

                elCarHeight.post(new Runnable() {
                    @Override
                    public void run() {
                        if (elCarHeight.isExpanded())
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight;
                        else
                            rlMoreDetails.getLayoutParams().height = rlMoreDetailsMeasuredHeight + elCarHeight.getLayoutParams().height;
                    }
                });

            }
        });

        elLoadingFareType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseAllExpandableLayout();
                elLoadingFareType.toggleExpansion();
            }
        });

        btnLoadingTime.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                controller.showPersianDatePickerDialog(tvLoadingTimeHint);
            }
        });

        btnChooseLoadingPhoto.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ImagePicker.pickImage(EditShipmentActivity.this, "لطفا عکس محموله خود را مشخص نمایید");
            }
        });

        elMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseAllExpandableLayout();
                elMoreDetails.toggleExpansion();
            }
        });

        btnDetermineDischargeTime.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                controller.showPersianDatePickerDialog(tvDetermineDischargeTimeHint);
            }
        });

        btnDetermineExpirationTime.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                controller.showPersianDatePickerDialog(tvDetermineExpirationTimeHint);
            }
        });

        this.btnPaymentIsPaidAfterLoadingArriveToDestination.setOnCheckedChangeListener(new CustomSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomSwitchButton view, boolean isChecked) {
                if (isChecked) {
                } else {
                }
            }
        });

        this.btnLoadingIsGoingRound.setOnCheckedChangeListener(new CustomSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomSwitchButton view, boolean isChecked) {
                if (isChecked) {
                } else {
                }
            }
        });

        this.btnHavingIntactTent.setOnCheckedChangeListener(new CustomSwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomSwitchButton view, boolean isChecked) {
                if (isChecked) {
                } else {
                }
            }
        });

        btnEdit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                controller.save();
            }
        });

        etLoadingWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etLoadingWeight.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    etLoadingWeight.setText(formattedString);
                    etLoadingWeight.setSelection(etLoadingWeight.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                etLoadingWeight.addTextChangedListener(this);

            }
        });

        etLoadingFare.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etLoadingFare.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    etLoadingFare.setText(formattedString);
                    etLoadingFare.setSelection(etLoadingFare.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                etLoadingFare.addTextChangedListener(this);

            }
        });

        etSpendingWarehouse.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etSpendingWarehouse.removeTextChangedListener(this);

                try {
                    String givenstring = s.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String formattedString = formatter.format(longval);
                    etSpendingWarehouse.setText(formattedString);
                    etSpendingWarehouse.setSelection(etSpendingWarehouse.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                etSpendingWarehouse.addTextChangedListener(this);

            }
        });
    }

    private void getIntents() {
        if (getIntent().getExtras() != null) {
            shipmentDetail = getIntent().getExtras().getString("shipment_detail");
            type = getIntent().getExtras().getInt("type");

            if (type == ShipmentEditTypeEnum.edit.getValue())
                setShipmentDataForEdit();
            else if (type == ShipmentEditTypeEnum.requestAgain.getValue())
                setShipmentDataForRequestAgain();

            setPlacesInfo();
            setTruckTypeInfo();
        }
    }

    private void setShipmentDataForEdit() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ShipmentDetailResponse shipmentData = mapper.readValue(shipmentDetail, ShipmentDetailResponse.class);

            controller.setType(type);
            controller.setShipmentData(shipmentData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setShipmentDataForRequestAgain() {
        try {
            ShipmentDetailResponse shipmentData = new ShipmentDetailResponse();

            JSONObject data = new JSONObject(shipmentDetail);
            JSONObject dfo = data.optJSONObject("Dfo");
            JSONObject ufo = dfo.optJSONObject("Ufo");
            JSONObject tfo = data.optJSONObject("Tfo");
            JSONArray orgCity = data.optJSONArray("orgCity");
            JSONArray decCity = data.optJSONArray("decCity");
            JSONObject draft = data.optJSONObject("d");

            shipmentData.setDraftId(draft.optString("Id"));
            shipmentData.setCreateDate(draft.optString("CreateDate"));
            shipmentData.setTruckMinLength(BigDecimal.valueOf(draft.optDouble("TruckMinLength")).floatValue());
            shipmentData.setTruckMaxLength(BigDecimal.valueOf(draft.optDouble("TruckMaxLength")).floatValue());
            shipmentData.setTruckMinWidth(BigDecimal.valueOf(draft.optDouble("TruckMinWidth")).floatValue());
            shipmentData.setTruckMaxWidth(BigDecimal.valueOf(draft.optDouble("TruckMaxWidth")).floatValue());
            shipmentData.setTruckMinHeight(BigDecimal.valueOf(draft.optDouble("TruckMinHeight")).floatValue());
            shipmentData.setTruckMaxHeight(BigDecimal.valueOf(draft.optDouble("TruckMaxHeight")).floatValue());
            shipmentData.setWeight(BigDecimal.valueOf(draft.optDouble("Weight", 0)).floatValue());
            shipmentData.setMoney(draft.optInt("Money", 0));
            shipmentData.setLoadingDate(new Date().getTime() + "");
            shipmentData.setExpireDate((new Date().getTime() + 3 * 24 * 60 * 60 * 1000) + "");
            shipmentData.setDischargeDate("");
            shipmentData.setHasTent(draft.optBoolean("HasTent", false));
            shipmentData.setGoback(draft.optBoolean("Goback", false));
            shipmentData.setAfterRecive(draft.optBoolean("AfterRecive", false));
            shipmentData.setStoreCost(draft.optInt("StoreCost", 0));
            shipmentData.setPicPath(draft.optString("PicPath"));
            shipmentData.setDesc(draft.optString("Desc", ""));
            shipmentData.setShipmentType(draft.optString("ShipmentType", ""));

            int moneyTypeId = 0;
            String moneyType = draft.optString("MoneyType");
            List<LoadingFareType> listLoadingFareType = LoadingFareType.listAll(LoadingFareType.class);
            for (int i = 0; i < listLoadingFareType.size(); i++) {
                if (listLoadingFareType.get(i).getName().equals(moneyType)) {
                    moneyTypeId = listLoadingFareType.get(i).getLoadingFareTypeId();
                    break;
                }
            }
            shipmentData.setMoneyTypeId(moneyTypeId);

            int truckTypeId = 0;
            String truckType = draft.optString("TruckType");
            List<TruckType> listTruckType = TruckType.listAll(TruckType.class);
            for (int i = 0; i < listTruckType.size(); i++) {
                if (listTruckType.get(i).getFullName().equals(truckType)) {
                    truckTypeId = listTruckType.get(i).getTruckTypeId();
                    break;
                }
            }
            shipmentData.setTruckTypeId(truckTypeId);

            List<PlacesInfo> listOriginPlacesInfo = new ArrayList<>();
            for (int i = 0; i < orgCity.length(); i++) {
                PlacesInfo placesInfo = new PlacesInfo();
                placesInfo.setPlaceType(0);
                placesInfo.setAddressInfo(new AddressInfo(orgCity.optJSONObject(i).optJSONObject("PlaceName").getInt("Id"), "", orgCity.optJSONObject(i).optJSONObject("PlaceName").optString("city")));
                placesInfo.setLatLngInfo(new LatLngInfo(orgCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("latitude"), orgCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("longitude")));

                listOriginPlacesInfo.add(placesInfo);
            }
            shipmentData.setListOriginPlacesInfo(listOriginPlacesInfo);

            List<PlacesInfo> listDestinationPlacesInfo = new ArrayList<>();
            for (int i = 0; i < decCity.length(); i++) {
                PlacesInfo placesInfo = new PlacesInfo();
                placesInfo.setPlaceType(1);
                placesInfo.setAddressInfo(new AddressInfo(decCity.optJSONObject(i).optJSONObject("PlaceName").getInt("Id"), "", decCity.optJSONObject(i).optJSONObject("PlaceName").optString("city")));
                placesInfo.setLatLngInfo(new LatLngInfo(decCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("latitude"), decCity.optJSONObject(i).optJSONObject("lanLatvm").optDouble("longitude")));

                listDestinationPlacesInfo.add(placesInfo);
            }
            shipmentData.setListDestinationPlacesInfo(listDestinationPlacesInfo);

            controller.setType(type);
            controller.setShipmentData(shipmentData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPlacesInfo() {
        try {
            List<PlacesInfo> listPlacesInfo = new ArrayList<>();
            for (PlacesInfo placesInfo : controller.getShipmentData().getListOriginPlacesInfo()) {
                listPlacesInfo.add(placesInfo);
            }
            for (PlacesInfo placesInfo : controller.getShipmentData().getListDestinationPlacesInfo()) {
                listPlacesInfo.add(placesInfo);
            }

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(listPlacesInfo);

            controller.setPlacesInfo(json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setTruckTypeInfo() {
        List<TruckType> listTruckType = Select.from(TruckType.class).where(Condition.prop("truck_type_id").eq(controller.getShipmentData().getTruckTypeId())).list();
        TruckType truckType = listTruckType.get(0);
        controller.setTruckTypeInfo(new TruckTypeInfo(truckType.getTruckTypeId(), truckType.getName(), truckType.getPid(), truckType.getFullName(), truckType.getMinLength(), truckType.getMaxLength(), truckType.getMinWidth(), truckType.getMaxWidth(), truckType.getMinHeight(), truckType.getMaxHeight(), truckType.isRoof(), truckType.isHasChild()));
    }


    @Override
    public CustomTextView getTvLocationHint() {
        return null;
    }

    @Override
    public CustomTextView getTvCarChooseHint() {
        return tvCarChooseHint;
    }

    @Override
    public CustomTextView getTvCarLengthHeaderLabel() {
        return tvCarLengthHeaderLabel;
    }

    @Override
    public CustomEditText getEtCarLengthFrom() {
        return etCarLengthFrom;
    }

    @Override
    public CustomEditText getEtCarLengthTo() {
        return etCarLengthTo;
    }

    @Override
    public CustomTextView getTvCarWidthHeaderLabel() {
        return tvCarWidthHeaderLabel;
    }

    @Override
    public CustomEditText getEtCarWidthFrom() {
        return etCarWidthFrom;
    }

    @Override
    public CustomEditText getEtCarWidthTo() {
        return etCarWidthTo;
    }

    @Override
    public ExpandableLayout getElCarHeight() {
        return elCarHeight;
    }

    @Override
    public CustomTextView getTvCarHeightHeaderLabel() {
        return tvCarHeightHeaderLabel;
    }

    @Override
    public CustomEditText getEtCarHeightFrom() {
        return etCarHeightFrom;
    }

    @Override
    public CustomEditText getEtCarHeightTo() {
        return etCarHeightTo;
    }

    @Override
    public CustomEditText getEtLoadingType() {
        return etLoadingType;
    }

    @Override
    public CustomEditText getEtLoadingWeight() {
        return etLoadingWeight;
    }

    @Override
    public RecyclerView getLoadingFareTypeRecyclerView() {
        return rvLoadingFareType;
    }

    @Override
    public CustomTextView getTvLoadingFareTypeHint() {
        return tvLoadingFareTypeHint;
    }

    @Override
    public CustomEditText getEtLoadingFare() {
        return etLoadingFare;
    }

    @Override
    public void setLoadingFareViewVisiablity(int visiablity) {
        findViewById(R.id.acEditShipment_tvLoadingFare).setVisibility(visiablity);
        findViewById(R.id.acEditShipment_tvLoadingFareHint).setVisibility(visiablity);
        findViewById(R.id.acEditShipment_etLoadingFare).setVisibility(visiablity);
    }

    @Override
    public CustomTextView getTvLoadingTimeHint() {
        return tvLoadingTimeHint;
    }

    @Override
    public CustomEditText getEtّDescription() {
        return etّDescription;
    }

    @Override
    public ImageView getIvLoadingPhotoPreview() {
        return ivLoadingPhotoPreview;
    }

    @Override
    public CustomEditText getEtSpendingWarehouse() {
        return etSpendingWarehouse;
    }

    @Override
    public CustomTextView getTvDetermineDischargeTimeHint() {
        return tvDetermineDischargeTimeHint;
    }

    @Override
    public CustomTextView getTvDetermineExpirationTimeHint() {
        return tvDetermineExpirationTimeHint;
    }

    @Override
    public CustomSwitchButton getBtnPaymentIsPaidAfterLoadingArriveToDestination() {
        return btnPaymentIsPaidAfterLoadingArriveToDestination;
    }

    @Override
    public CustomSwitchButton getBtnLoadingIsGoingRound() {
        return btnLoadingIsGoingRound;
    }

    @Override
    public CustomSwitchButton getBtnHavingIntactTent() {
        return btnHavingIntactTent;
    }

    @Override
    public void setCarChooserHint(String carTypeFullname) {
        try {
            tvCarChooseHint.setVisibility(View.VISIBLE);
            tvCarChooseHint.setText(carTypeFullname);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ExpandableLayout getCarLengthView() {
        return elCarLength;
    }

    @Override
    public void setCarLengthHeaderLabel(String text) {
        tvCarLengthHeaderLabel.setText(text);
    }

    @Override
    public ExpandableLayout getCarWidthView() {
        return elCarWidth;
    }

    @Override
    public void setCarWidthHeaderLabel(String text) {
        tvCarWidthHeaderLabel.setText(text);
    }

    @Override
    public ExpandableLayout getCarHeightView() {
        return elCarHeight;
    }

    @Override
    public void setCarHeightHeaderLabel(String text) {
        tvCarHeightHeaderLabel.setText(text);
    }

    @Override
    public void collapseAllExpandableLayout() {
        if (elCarLength.isExpanded()) elCarLength.toggleExpansion();
        if (elCarWidth.isExpanded()) elCarWidth.toggleExpansion();
        if (elCarHeight.isExpanded()) elCarHeight.toggleExpansion();
        if (elLoadingFareType.isExpanded()) elLoadingFareType.toggleExpansion();
        if (elMoreDetails.isExpanded()) elMoreDetails.toggleExpansion();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Configuration.REQUEST_CODE_CROP_IMAGE) {
            String path = data.getStringExtra(ImageCropperActivity.IMAGE_PATH);
            if (path == null) {
                return;
            }

            Bitmap bitmap = ImageUtil.getInstance(EditShipmentActivity.this).getBitmapFromFilePath(path, "001.jpg", 450, 450);
            ivLoadingPhotoPreview.setVisibility(View.VISIBLE);
            ivLoadingPhotoPreview.setImageBitmap(bitmap);
        } else if (resultCode == RESULT_OK && requestCode == ImagePicker.mPickImageRequestCode) {
            Bitmap bitmap = ImagePicker.getImageFromResult(AppController.getContext(), requestCode, resultCode, data);

            Uri tempUri = ImageUtil.getInstance(EditShipmentActivity.this).getImageUri(bitmap);
            File finalFile = new File(ImageUtil.getInstance(EditShipmentActivity.this).getRealPathFromURI(tempUri));

            controller.startCropImage(finalFile.getPath());
        } else if (resultCode == RESULT_OK && requestCode == Configuration.REQUEST_CODE_PLACES_INFO) {
            controller.setPlacesInfo(data.getStringExtra("place_info"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

//        unregisterReceiver(controller.broadcastRequestCarType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}