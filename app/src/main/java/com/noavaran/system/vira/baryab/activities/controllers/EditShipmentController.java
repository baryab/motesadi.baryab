package com.noavaran.system.vira.baryab.activities.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.noavaran.system.vira.baryab.AppController;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.EditShipmentActivity;
import com.noavaran.system.vira.baryab.activities.ImageCropperActivity;
import com.noavaran.system.vira.baryab.activities.delegates.EditShipmentDelegate;
import com.noavaran.system.vira.baryab.activities.models.EditShipmentModel;
import com.noavaran.system.vira.baryab.adapters.MoneyTypeAdapter;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.database.models.LoadingFareType;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.dialogs.CarTypeChooserDialog;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;
import com.noavaran.system.vira.baryab.enums.ShipmentEditTypeEnum;
import com.noavaran.system.vira.baryab.info.MoneyTypeInfo;
import com.noavaran.system.vira.baryab.info.NewLoadInfo;
import com.noavaran.system.vira.baryab.info.PlacesInfo;
import com.noavaran.system.vira.baryab.info.TruckTypeInfo;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.ImageUtil;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.noavaran.system.vira.baryab.utils.okhttp.response.ShipmentDetailResponse;
import com.noavaran.system.vira.baryab.utils.persiandatepicker.Listener;
import com.noavaran.system.vira.baryab.utils.persiandatepicker.PersianDatePickerDialog;
import com.noavaran.system.vira.baryab.utils.persiandatepicker.util.PersianCalendar;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditShipmentController implements EditShipmentDelegate.Controller {
    private EditShipmentActivity activity;
    private EditShipmentDelegate.View view;
    private EditShipmentModel model;

    private CarTypeChooserDialog carTypeChooserDialog;

    private PersianDatePickerDialog persianDatePicker;
    private Typeface iranSansTypeface;
    private PersianCalendar initDate;

    private List<LoadingFareType> listLoadingFareType;
    private List<MoneyTypeInfo> listLoadingFareTypeInfo;
    private MoneyTypeAdapter moneyTypeAdapter;

    private List<PlacesInfo> listPlacesInfos;
    private TruckTypeInfo truckTypeInfo;

    private ShipmentDetailResponse shipmentData;
    private int type;

    private int loadingFareTypeId;

    private String strPlacesInfo;
    private String truckTypeFullNameHelper;

    public EditShipmentController(EditShipmentActivity activity, EditShipmentDelegate.View view) {
        this.activity = activity;
        this.view = view;

        model = new EditShipmentModel();

        initComponents();
    }

    private void initComponents() {
        iranSansTypeface = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.baseFont));
        carTypeChooserDialog = new CarTypeChooserDialog();

        initPersianCalendar();
    }

    private void initPersianCalendar() {
        String strPersianDate = PersianDateUtil.getPersianDate(new Date().getTime());
        String[] strSplitPersianDate = strPersianDate.split("/");
        int year = Integer.parseInt(strSplitPersianDate[0]);
        int month = Integer.parseInt(strSplitPersianDate[1]);
        int day = Integer.parseInt(strSplitPersianDate[2]);

        initDate = new PersianCalendar();
        initDate.setPersianDate(year, month, day);

        initPersianDatePickerDialog(year, year);
    }

    public void initPersianDatePickerDialog(int minYeay, int maxYear) {
        persianDatePicker = new PersianDatePickerDialog(activity)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setInitDate(initDate)
                .setMaxYear(maxYear)
                .setMinYear(minYeay)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(iranSansTypeface);
    }

    public PersianDatePickerDialog getPersianDatePicker() {
        return persianDatePicker;
    }

    public void showPersianDatePickerDialog(final CustomTextView field) {
        getPersianDatePicker().show();
        getPersianDatePicker().setListener(new Listener() {
            @Override
            public void onDateSelected(PersianCalendar persianCalendar) {
                field.setText(persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay());
            }

            @Override
            public void onDisimised() {

            }
        });
    }

    public void startCropImage(String filePath) {
        Intent intent = new Intent(AppController.getContext(), ImageCropperActivity.class);
        intent.putExtra(ImageCropperActivity.IMAGE_PATH, filePath);
        intent.putExtra(ImageCropperActivity.SCALE, true);
        intent.putExtra(ImageCropperActivity.ASPECT_X, 3);
        intent.putExtra(ImageCropperActivity.ASPECT_Y, 3);

        activity.startActivityForResult(intent, Configuration.REQUEST_CODE_CROP_IMAGE);
    }

    public CarTypeChooserDialog getCarTypeChooserDialog() {
        return carTypeChooserDialog;
    }

    @Override
    public void setShipmentData(ShipmentDetailResponse shipmentData) {
        this.shipmentData = shipmentData;

        initPageViews();
    }

    @Override
    public ShipmentDetailResponse getShipmentData() {
        return this.shipmentData;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setTruckTypeInfo(TruckTypeInfo truckTypeInfo) {
        this.truckTypeInfo = truckTypeInfo;
    }

    @Override
    public void save() {
        if (listPlacesInfos == null || listPlacesInfos.size() <= 0) {
            showMessageDialog("لطفا محل بار گیری و تخلیه خود را مشخص کنید");
        } else if (truckTypeInfo == null) {
            showMessageDialog("لطفا نوع ماشین خود را مشخص کنید");
        }  else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && view.getEtCarLengthFrom().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداقل طول ماشین را مشخص کنید");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && view.getEtCarLengthTo().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداکثر طول ماشین را مشخص کنید");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEtCarLengthFrom().getText().toString().isEmpty() ? "0" : view.getEtCarLengthFrom().getText().toString()) < truckTypeInfo.getMinLength() || Float.parseFloat(view.getEtCarLengthFrom().getText().toString().isEmpty() ? "0" : view.getEtCarLengthFrom().getText().toString()) > truckTypeInfo.getMaxLength()) {
            showMessageDialog("حداقل طول ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEtCarLengthTo().getText().toString().isEmpty() ? "0" : view.getEtCarLengthTo().getText().toString()) < truckTypeInfo.getMinLength() || Float.parseFloat(view.getEtCarLengthTo().getText().toString().isEmpty() ? "0" : view.getEtCarLengthTo().getText().toString()) > truckTypeInfo.getMaxLength()) {
            showMessageDialog("حداکثر طول ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEtCarLengthTo().getText().toString().isEmpty() ? "0" : view.getEtCarLengthTo().getText().toString()) < Float.parseFloat(view.getEtCarLengthFrom().getText().toString().isEmpty() ? "0" : view.getEtCarLengthFrom().getText().toString())) {
            showMessageDialog("حداکثر طول ماشین نمی تواند کوچکتر از حداقل طول ماشین باشد لطفا مقدار وارد شده را اصلاح نمایید.");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && view.getEtCarWidthFrom().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداقل عرض ماشین را مشخص کنید");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && view.getEtCarWidthTo().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداکثر عرض ماشین را مشخص کنید");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEtCarWidthFrom().getText().toString().isEmpty() ? "0" : view.getEtCarWidthFrom().getText().toString()) < truckTypeInfo.getMinWidth() || Float.parseFloat(view.getEtCarWidthFrom().getText().toString().isEmpty() ? "0" : view.getEtCarWidthFrom().getText().toString()) > truckTypeInfo.getMaxWidth()) {
            showMessageDialog("حداقل عرض ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEtCarWidthTo().getText().toString().isEmpty() ? "0" : view.getEtCarWidthTo().getText().toString()) < truckTypeInfo.getMinWidth() || Float.parseFloat(view.getEtCarWidthTo().getText().toString().isEmpty() ? "0" : view.getEtCarWidthTo().getText().toString()) > truckTypeInfo.getMaxWidth()) {
            showMessageDialog("حداکثر عرض ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEtCarWidthTo().getText().toString().isEmpty() ? "0" : view.getEtCarWidthTo().getText().toString()) < Float.parseFloat(view.getEtCarWidthFrom().getText().toString().isEmpty() ? "0" : view.getEtCarWidthFrom().getText().toString())) {
            showMessageDialog("حداکثر عرض ماشین نمی تواند کوچکتر از حداقل عرض ماشین باشد لطفا مقدار وارد شده را اصلاح نمایید.");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && view.getEtCarHeightFrom().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداقل عرض ماشین را مشخص کنید");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && view.getEtCarHeightTo().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداکثر عرض ماشین را مشخص کنید");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && (Float.parseFloat(view.getEtCarHeightFrom().getText().toString().isEmpty() ? "0" : view.getEtCarHeightFrom().getText().toString()) < truckTypeInfo.getMinHeight() || Float.parseFloat(view.getEtCarHeightFrom().getText().toString().isEmpty() ? "0" : view.getEtCarHeightFrom().getText().toString()) > truckTypeInfo.getMaxHeight())) {
            showMessageDialog("حداقل ارتفاع ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && (Float.parseFloat(view.getEtCarHeightTo().getText().toString().isEmpty() ? "0" : view.getEtCarHeightTo().getText().toString()) < truckTypeInfo.getMinHeight() || Float.parseFloat(view.getEtCarHeightTo().getText().toString().isEmpty() ? "0" : view.getEtCarHeightTo().getText().toString()) > truckTypeInfo.getMaxHeight())) {
            showMessageDialog("حداکثر ارتفاع ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && (Float.parseFloat(view.getEtCarHeightTo().getText().toString().isEmpty() ? "0" : view.getEtCarHeightTo().getText().toString()) < Float.parseFloat(view.getEtCarHeightFrom().getText().toString().isEmpty() ? "0" : view.getEtCarHeightFrom().getText().toString()))) {
            showMessageDialog("حداکثر ارتفاع ماشین نمی تواند کوچکتر از حداقل ارتفاع ماشین باشد لطفا مقدار وارد شده را اصلاح نمایید.");
        } else if (view.getEtLoadingType().getText().toString().isEmpty()) {
            showMessageDialog("لطفا نوع بار خود را مشخص نمایید");
        } else if (view.getEtLoadingWeight().getText().toString().isEmpty()) {
            showMessageDialog("وزن بار خود را تعیین نمایید");
        } else if (loadingFareTypeId == 0) {
            showMessageDialog("لطفا نوع کرایه بار خود را انتخاب نمایید");
        } else if (view.getEtLoadingFare().getText().toString().isEmpty()) {
            showMessageDialog("لطفا کرایه خود را برای این بار تعریف کنید");
        } else if (view.getTvLoadingTimeHint().getText().toString().isEmpty()) {
            showMessageDialog("لطفا زمان بارگیری خود را ذکر نمایید");
        } else if (view.getIvLoadingPhotoPreview().getDrawable() == null) {
            showMessageDialog("لطفا یک عکس برای بار خود انتخاب کنید");
        } else {
            List<PlacesInfo> listOrigins = new ArrayList<>();
            List<PlacesInfo> listDestination = new ArrayList<>();
            for (PlacesInfo placesInfo : listPlacesInfos) {
                if (placesInfo.getPlaceType() == MapController.KEY_PLACE_TYPE_ORIGIN)
                    listOrigins.add(placesInfo);
                else if (placesInfo.getPlaceType() == MapController.KEY_PLACE_TYPE_DESTINATION)
                    listDestination.add(placesInfo);
            }

            try {
                String[] splitTruckType = view.getTvCarChooseHint().getText().toString().replace(" , ", "/").split("/");
                String strTruckType = splitTruckType.length == 1 ? splitTruckType[0] : splitTruckType.length == 2 ? splitTruckType[1] + "/" + splitTruckType[0] : splitTruckType[2] + "/" + splitTruckType[1] + "/" + splitTruckType[0];
                List<TruckType> listTruckType = Select.from(TruckType.class).where(Condition.prop("full_name").eq(truckTypeFullNameHelper)).list();
                int truckTypeID = listTruckType.get(0).getTruckTypeId();
                float carLengthFrom = view.getEtCarLengthFrom().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEtCarLengthFrom().getText().toString());
                float carLengthTo = view.getEtCarLengthTo().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEtCarLengthTo().getText().toString());
                float carWidthFrom = view.getEtCarWidthFrom().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEtCarWidthFrom().getText().toString());
                float carWidthTo = view.getEtCarWidthTo().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEtCarWidthTo().getText().toString());
                float carHeightFrom = view.getEtCarHeightFrom().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEtCarHeightFrom().getText().toString());
                float carHeightTo = view.getEtCarHeightTo().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEtCarHeightTo().getText().toString());
                String loadingType = view.getEtLoadingType().getText().toString();
                float loadingWeight = Float.parseFloat(view.getEtLoadingWeight().getText().toString());
                int loadingFare = Integer.parseInt(view.getEtLoadingFare().getText().toString().replaceAll(",", ""));
                long loadingDate = PersianDateUtil.getDateFromPersianDateString(view.getTvLoadingTimeHint().getText().toString()).getTime();
                String description = view.getEtّDescription().getText().toString();

                Bitmap bitmap = ImageUtil.getInstance(activity).getBitmapFromDrawable(view.getIvLoadingPhotoPreview().getDrawable());
                String loadingPhoto = ImageUtil.getInstance(activity).encodeImage(bitmap);

                int warehouseSpending = view.getEtSpendingWarehouse().getText().toString().isEmpty() ? 0 : Integer.parseInt(view.getEtSpendingWarehouse().getText().toString().replace(",", ""));
                long dischargeTime = view.getTvDetermineDischargeTimeHint().getText().toString().isEmpty() ? 0 : PersianDateUtil.getDateFromPersianDateString(view.getTvDetermineDischargeTimeHint().getText().toString()).getTime();
                long expireTime = view.getTvDetermineExpirationTimeHint().getText().toString().isEmpty() ? 0 : PersianDateUtil.getDateFromPersianDateString(view.getTvDetermineExpirationTimeHint().getText().toString()).getTime();

                boolean isShipmentGoingRound = view.getBtnLoadingIsGoingRound().isChecked();
                boolean isRefundAfterArrival = view.getBtnPaymentIsPaidAfterLoadingArriveToDestination().isChecked();
                boolean isHavingWellTent = view.getBtnHavingIntactTent().isChecked();

                NewLoadInfo newLoadInfo = new NewLoadInfo(getShipmentData().getDraftId(), listOrigins, listDestination, truckTypeID, carLengthFrom, carLengthTo, carWidthFrom, carWidthTo, carHeightFrom, carHeightTo, loadingType, loadingWeight, loadingFareTypeId, loadingFare, loadingDate, description, loadingPhoto, warehouseSpending, dischargeTime, expireTime, isShipmentGoingRound, isRefundAfterArrival, isHavingWellTent);

                ObjectMapper mapper = new ObjectMapper();
                String jsonInString = mapper.writeValueAsString(newLoadInfo);

                OkHttpHelper okHttpHelper = new OkHttpHelper(activity, Configuration.API_EDIT_LOADING, OkHttpHelper.MEDIA_TYPE_JSON);
                okHttpHelper.doEditLoading(shipmentData.getDraftId(), type, jsonInString, new OkHttpHelper.OnCallback() {
                    @Override
                    public void onStart() {
                        activity.showDialogProgress("در حال ارسال اطلاعات به سرور");
                    }

                    @Override
                    public void onResponse(JSONObject result) {
                        loadEditLoading(result);
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadEditLoading(final JSONObject result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        Intent intent = new Intent("MyLoadingDetailRequest");
                        intent.putExtra("doGettingDataAgain", true);
                        intent.setAction("com.noavaran.system.vira.baryab.REQUEST_LOADINGDETAIL");
                        activity.sendBroadcast(intent);

                        activity.showToastSuccess(result.getString("message"));
                        activity.finish();
                    } else {
                        activity.showToastError(result.getString("message"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void showMessageDialog(String message) {
        final MessageDialog messageDialog = new MessageDialog(activity, activity.getString(R.string.app_name), message);
        messageDialog.setOnClickListener(new MessageDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                messageDialog.dismiss();
            }
        });
        messageDialog.show();
    }

    private void initPageViews() {
        if (view.getTvLocationHint().getVisibility() == View.GONE)
            view.getTvLocationHint().setVisibility(View.VISIBLE);
        if (view.getTvCarChooseHint().getVisibility() == View.GONE)
            view.getTvCarChooseHint().setVisibility(View.VISIBLE);
        if (shipmentData.getTruckMinHeight() == 0 && shipmentData.getTruckMaxHeight() == 0)
            view.getElCarHeight().setVisibility(View.GONE);
        if (view.getIvLoadingPhotoPreview().getVisibility() == View.GONE)
            view.getIvLoadingPhotoPreview().setVisibility(View.VISIBLE);

        List<TruckType> listTruckType = TruckType.findWithQuery(TruckType.class, "Select * from " + TruckType.SUGAR + " where truck_type_id = ?", String.valueOf(shipmentData.getTruckTypeId()));
        truckTypeFullNameHelper = listTruckType.get(0).getFullName();

        view.getTvLocationHint().setText("مبدا : " + shipmentData.getListOriginPlacesInfo().get(0).getAddressInfo().getCity() + " ، مقصد : " + shipmentData.getListDestinationPlacesInfo().get(shipmentData.getListDestinationPlacesInfo().size() - 1).getAddressInfo().getCity() + "");
        view.getTvCarChooseHint().setText(truckTypeFullNameHelper.replace("/", " ، "));
        view.getTvCarLengthHeaderLabel().setText("بازه از " + listTruckType.get(0).getMinLength() + " متر تا " + listTruckType.get(0).getMaxLength() + " متر");
        view.getEtCarLengthFrom().setText(shipmentData.getTruckMinLength() + "");
        view.getEtCarLengthTo().setText(shipmentData.getTruckMaxLength() + "");
        view.getTvCarWidthHeaderLabel().setText("بازه از " + listTruckType.get(0).getMinWidth() + " متر تا " + listTruckType.get(0).getMaxWidth() + " متر");
        view.getEtCarWidthFrom().setText(shipmentData.getTruckMinWidth() + "");
        view.getEtCarWidthTo().setText(shipmentData.getTruckMaxWidth() + "");
        view.getTvCarHeightHeaderLabel().setText("بازه از " + listTruckType.get(0).getMinHeight() + " متر تا " + listTruckType.get(0).getMaxHeight() + " متر");
        view.getEtCarHeightFrom().setText(shipmentData.getTruckMinHeight() + "");
        view.getEtCarHeightTo().setText(shipmentData.getTruckMaxHeight() + "");
        view.getEtLoadingType().setText(shipmentData.getShipmentType() + "");
        view.getEtLoadingWeight().setText(shipmentData.getWeight() + "");

        loadingFareTypeId = shipmentData.getMoneyTypeId();
        initLoadingFareTypeRecyclerView(shipmentData.getMoneyTypeId());

        if (shipmentData.getMoneyTypeId() == 2) {
            view.getEtLoadingFare().setVisibility(View.GONE);
            activity.findViewById(R.id.acEditShipment_tvLoadingFare).setVisibility(View.GONE);
            activity.findViewById(R.id.acEditShipment_tvLoadingFareHint).setVisibility(View.GONE);
            view.getEtLoadingFare().setText(shipmentData.getMoney() + "");
        } else {
            view.getEtLoadingFare().setVisibility(View.VISIBLE);
            activity.findViewById(R.id.acEditShipment_tvLoadingFare).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.acEditShipment_tvLoadingFareHint).setVisibility(View.VISIBLE);
            view.getEtLoadingFare().setText(shipmentData.getMoney() + "");
        }
        view.getTvLoadingTimeHint().setText(getType() == ShipmentEditTypeEnum.requestAgain.getValue() ? PersianDateUtil.getPersianDate(shipmentData.getLoadingDate()) : PersianDateUtil.getPersianDate(shipmentData.getLoadDate()));
        view.getEtّDescription().setText(shipmentData.getDesc());
        Picasso.with(activity).load(Configuration.BASE_IMAGE_URL + shipmentData.getPicPath()).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(view.getIvLoadingPhotoPreview());
        view.getEtSpendingWarehouse().setText(shipmentData.getStoreCost() + "");
        view.getTvDetermineDischargeTimeHint().setText(getType() == ShipmentEditTypeEnum.requestAgain.getValue() ? GlobalUtils.IsNullOrEmpty(shipmentData.getDischargeDate()) ? "" : PersianDateUtil.getPersianDate(shipmentData.getDischargeDate()) : shipmentData.getDisChDate() > 0 ? PersianDateUtil.getPersianDate(shipmentData.getDisChDate()) : "");
        view.getTvDetermineExpirationTimeHint().setText(getType() == ShipmentEditTypeEnum.requestAgain.getValue() ? PersianDateUtil.getPersianDate(shipmentData.getExpireDate()) : PersianDateUtil.getPersianDate(shipmentData.getExpDate()));
        view.getBtnPaymentIsPaidAfterLoadingArriveToDestination().setChecked(shipmentData.isAfterRecive());
        view.getBtnLoadingIsGoingRound().setChecked(shipmentData.isGoback());
        view.getBtnHavingIntactTent().setChecked(shipmentData.isHasTent());
    }

    private void initLoadingFareTypeRecyclerView(int id) {
        listLoadingFareType = LoadingFareType.listAll(LoadingFareType.class);
        listLoadingFareTypeInfo = new ArrayList<>();

        for (int i = 0; i < listLoadingFareType.size(); i++) {
            if (listLoadingFareType.get(i).getLoadingFareTypeId() == id) {
                listLoadingFareTypeInfo.add(new MoneyTypeInfo(listLoadingFareType.get(i).getLoadingFareTypeId(), listLoadingFareType.get(i).getName(), true));
                view.getTvLoadingFareTypeHint().setText("(" + listLoadingFareType.get(i).getName() + ")");
            } else
                listLoadingFareTypeInfo.add(new MoneyTypeInfo(listLoadingFareType.get(i).getLoadingFareTypeId(), listLoadingFareType.get(i).getName(), false));
        }

        if (listLoadingFareTypeInfo.get(0).isSelected())
            view.setLoadingFareViewVisiablity(View.GONE);

        moneyTypeAdapter = new MoneyTypeAdapter(listLoadingFareTypeInfo);
        moneyTypeAdapter.setOnItemClickListener(new MoneyTypeAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                loadingFareTypeId = listLoadingFareType.get(position).getLoadingFareTypeId();

                if (position == 0) {
                    listLoadingFareTypeInfo.get(0).setSelected(true);
                    listLoadingFareTypeInfo.get(1).setSelected(false);
                    listLoadingFareTypeInfo.get(2).setSelected(false);
                    view.setLoadingFareViewVisiablity(View.VISIBLE);
                } else if (position == 1) {
                    listLoadingFareTypeInfo.get(0).setSelected(false);
                    listLoadingFareTypeInfo.get(1).setSelected(true);
                    listLoadingFareTypeInfo.get(2).setSelected(false);
                    view.setLoadingFareViewVisiablity(View.GONE);
                } else if (position == 2) {
                    listLoadingFareTypeInfo.get(0).setSelected(false);
                    listLoadingFareTypeInfo.get(1).setSelected(false);
                    listLoadingFareTypeInfo.get(2).setSelected(true);
                    view.setLoadingFareViewVisiablity(View.VISIBLE);
                }

                List<LoadingFareType> loadingFareType = LoadingFareType.listAll(LoadingFareType.class);
                view.getTvLoadingFareTypeHint().setText("(" + loadingFareType.get(position).getName() + ")");
                view.getEtLoadingFare().setText("");
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        view.getLoadingFareTypeRecyclerView().setLayoutManager(mLayoutManager);
        view.getLoadingFareTypeRecyclerView().addItemDecoration(new DividerItemDecoration(activity, 0));
        view.getLoadingFareTypeRecyclerView().setItemAnimator(new DefaultItemAnimator());
        view.getLoadingFareTypeRecyclerView().setAdapter(moneyTypeAdapter);
    }

    public void setPlacesInfo(String strPlacesInfo) {
        this.strPlacesInfo = strPlacesInfo;

        try {
            ObjectMapper mapper = new ObjectMapper();
            listPlacesInfos = mapper.readValue(this.strPlacesInfo, TypeFactory.defaultInstance().constructCollectionType(List.class, PlacesInfo.class));
            view.getTvLocationHint().setText("مبدا : " + listPlacesInfos.get(0).getAddressInfo().getCity() + "   مقصد : " + listPlacesInfos.get(listPlacesInfos.size() - 1).getAddressInfo().getCity() + "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getPlaceInfo() {
        return this.strPlacesInfo;
    }

    public BroadcastReceiver broadcastRequestCarType = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String carType = intent.getStringExtra("car_type");

                ObjectMapper mapper = new ObjectMapper();
                truckTypeInfo = mapper.readValue(carType, TruckTypeInfo.class);

                // set truck full name
                truckTypeFullNameHelper = truckTypeInfo.getFullName();
                String[] splitFullname = truckTypeFullNameHelper.split("/");
                if (splitFullname.length == 2)
                    view.setCarChooserHint(splitFullname[0] + " ، " + splitFullname[1]);
                else if (splitFullname.length == 3)
                    view.setCarChooserHint(splitFullname[0] + " ، " + splitFullname[1] + " ، " + splitFullname[2]);

                if (truckTypeInfo.getMinLength() > 0 && truckTypeInfo.getMaxLength() > 0) {
                    view.getCarLengthView().setVisibility(View.VISIBLE);
                    view.setCarLengthHeaderLabel("بازه از " + truckTypeInfo.getMinLength() + " تا " + truckTypeInfo.getMaxLength() + " متر");
                    view.getEtCarLengthFrom().setText(truckTypeInfo.getMinLength() + "");
                    view.getEtCarLengthTo().setText(truckTypeInfo.getMaxLength() + "");
                } else
                    view.getCarLengthView().setVisibility(View.GONE);

                if (truckTypeInfo.getMinWidth() > 0 && truckTypeInfo.getMaxWidth() > 0) {
                    view.getCarWidthView().setVisibility(View.VISIBLE);
                    view.setCarWidthHeaderLabel("بازه از " + truckTypeInfo.getMinWidth() + " تا " + truckTypeInfo.getMaxWidth() + " متر");
                    view.getEtCarWidthFrom().setText(truckTypeInfo.getMinWidth() + "");
                    view.getEtCarWidthTo().setText(truckTypeInfo.getMaxWidth() + "");
                } else
                    view.getCarWidthView().setVisibility(View.GONE);

                if (truckTypeInfo.getMinHeight() > 0 && truckTypeInfo.getMaxHeight() > 0) {
                    view.getCarHeightView().setVisibility(View.VISIBLE);
                    view.setCarHeightHeaderLabel("بازه از " + truckTypeInfo.getMinHeight() + " تا " + truckTypeInfo.getMaxHeight() + " متر");
                    view.getEtCarHeightFrom().setText(truckTypeInfo.getMinHeight() + "");
                    view.getEtCarHeightTo().setText(truckTypeInfo.getMaxHeight() + "");
                } else
                    view.getCarHeightView().setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
}
