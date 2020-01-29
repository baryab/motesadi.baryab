package com.noavaran.system.vira.baryab.fragments.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;
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
import com.noavaran.system.vira.baryab.activities.ImageCropperActivity;
import com.noavaran.system.vira.baryab.activities.MainActivity;
import com.noavaran.system.vira.baryab.activities.controllers.MapController;
import com.noavaran.system.vira.baryab.adapters.LoadingFareTypeAdapter;
import com.noavaran.system.vira.baryab.adapters.ProvinceTypeAdapter;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.database.models.LoadingFareType;
import com.noavaran.system.vira.baryab.database.models.ProvinceType;
import com.noavaran.system.vira.baryab.database.models.TruckType;
import com.noavaran.system.vira.baryab.dialogs.CarTypeChooserDialog;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;
import com.noavaran.system.vira.baryab.fragments.delegates.NewLoadDelegate;
import com.noavaran.system.vira.baryab.fragments.models.NewLoadModel;
import com.noavaran.system.vira.baryab.info.LoadingFareTypeInfo;
import com.noavaran.system.vira.baryab.info.NewLoadInfo;
import com.noavaran.system.vira.baryab.info.PlacesInfo;
import com.noavaran.system.vira.baryab.info.TruckTypeInfo;
import com.noavaran.system.vira.baryab.utils.DateUtil;
import com.noavaran.system.vira.baryab.utils.ImageUtil;
import com.noavaran.system.vira.baryab.utils.PersianDateUtil;
import com.noavaran.system.vira.baryab.utils.PersianUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.noavaran.system.vira.baryab.utils.persiandatepicker.Listener;
import com.noavaran.system.vira.baryab.utils.persiandatepicker.PersianDatePickerDialog;
import com.noavaran.system.vira.baryab.utils.persiandatepicker.util.PersianCalendar;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewLoadController implements NewLoadDelegate.Controller {


    private final FragmentActivity fragmentActivity;
    private final NewLoadDelegate.View view;
    private NewLoadModel model;
    private CarTypeChooserDialog carTypeChooserDialog;
    private List<PlacesInfo> listPlacesInfos;
    private String strPlacesInfo;
    private TruckTypeInfo truckTypeInfo;
    private ProvinceTypeAdapter provinceTypeAdapter;
    private List<ProvinceType> listprovinceType;
    private LoadingFareTypeAdapter loadingFareTypeAdapter;
    private List<LoadingFareType> listLoadingFareType;
    private List<LoadingFareTypeInfo> listLoadingFareTypeInfo;
    private ProvinceType provinceType;
    private int loadingFareTypeId = 1;
    private PersianDatePickerDialog persianDatePicker;
    private Typeface iranSansTypeface;
    private PersianCalendar initDate;
    private boolean isShipmentGoingRound;
    private boolean refundAfterArrival;
    private boolean havingWellTent;
    private List<LoadingFareType> loadingFareType;


    public NewLoadController(FragmentActivity fragmentActivity, NewLoadDelegate.View view) {
        this.fragmentActivity = fragmentActivity;
        this.view = view;
        this.model = new NewLoadModel();

        initComponents();
        initRecyclerView();
    }

    private void initComponents() {
        iranSansTypeface = Typeface.createFromAsset(fragmentActivity.getAssets(), fragmentActivity.getString(R.string.baseFont));
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

    private void initRecyclerView() {
        listprovinceType = ProvinceType.listAll(ProvinceType.class);
        for (int i = 0; i < listprovinceType.size(); i++) {

        }

        listLoadingFareType = LoadingFareType.listAll(LoadingFareType.class);
        listLoadingFareTypeInfo = new ArrayList<>();

        for (int i = 0; i < listLoadingFareType.size(); i++) {
            listLoadingFareTypeInfo.add(new LoadingFareTypeInfo(listLoadingFareType.get(i).getLoadingFareTypeId(), listLoadingFareType.get(i).getName()));
        }

        loadingFareType = LoadingFareType.listAll(LoadingFareType.class);

        loadingFareTypeAdapter = new LoadingFareTypeAdapter(listLoadingFareTypeInfo);
        loadingFareTypeAdapter.setOnItemClickListener(new LoadingFareTypeAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                loadingFareTypeId = listLoadingFareType.get(position).getLoadingFareTypeId();

                view.getTextViewLoadingFareTypeHint().setText("(" + loadingFareType.get(position).getName() + ")");

                if (loadingFareType.get(1).getLoadingFareTypeId() == loadingFareTypeId) {
                    view.hideLoadingFareView();
                } else {
                    view.showLoadingFareView();
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragmentActivity.getApplicationContext());
        view.getLoadingFareTypeRecyclerView().setLayoutManager(mLayoutManager);
        view.getLoadingFareTypeRecyclerView().addItemDecoration(new DividerItemDecoration(fragmentActivity, 0));
        view.getLoadingFareTypeRecyclerView().setItemAnimator(new DefaultItemAnimator());
        view.getLoadingFareTypeRecyclerView().setAdapter(loadingFareTypeAdapter);
    }

    public void initPersianDatePickerDialog(int minYeay, int maxYear) {
        persianDatePicker = new PersianDatePickerDialog(fragmentActivity)
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

    public CarTypeChooserDialog getCarTypeChooserDialog() {
        return carTypeChooserDialog;
    }

    public PersianDatePickerDialog getPersianDatePicker() {
        return persianDatePicker;
    }

    public void startCropImage(String filePath) {
        Intent intent = new Intent(AppController.getContext(), ImageCropperActivity.class);
        intent.putExtra(ImageCropperActivity.IMAGE_PATH, filePath);
        intent.putExtra(ImageCropperActivity.SCALE, true);
        intent.putExtra(ImageCropperActivity.ASPECT_X, 3);
        intent.putExtra(ImageCropperActivity.ASPECT_Y, 3);

        fragmentActivity.startActivityForResult(intent, Configuration.REQUEST_CODE_CROP_IMAGE);
    }

    public void setPlacesInfo(String strPlacesInfo) {
        this.strPlacesInfo = strPlacesInfo;

        try {
            ObjectMapper mapper = new ObjectMapper();
            listPlacesInfos = mapper.readValue(this.strPlacesInfo, TypeFactory.defaultInstance().constructCollectionType(List.class, PlacesInfo.class));
            view.setLocationHint("مبدا : " + listPlacesInfos.get(0).getAddressInfo().getProvince() + " " +
                    listPlacesInfos.get(0).getAddressInfo().getCity() + "   مقصد : " + listPlacesInfos.get(listPlacesInfos.size() - 1).getAddressInfo().getProvince() +
                    " " + listPlacesInfos.get(listPlacesInfos.size() - 1).getAddressInfo().getCity() + "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getPlaceInfo() {
        return this.strPlacesInfo;
    }

    @Override
    public void save() {
        if (listPlacesInfos == null || listPlacesInfos.size() <= 0) {
            showMessageDialog("لطفا محل بار گیری و تخلیه خود را مشخص کنید");
        } else if (truckTypeInfo == null) {
            showMessageDialog("لطفا نوع ماشین خود را مشخص کنید");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && view.getEditTextCarLengthFrom().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداقل طول ماشین را مشخص کنید");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && view.getEditTextCarLengthTo().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداکثر طول ماشین را مشخص کنید");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEditTextCarLengthFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarLengthFrom().getText().toString()) < truckTypeInfo.getMinLength() || Float.parseFloat(view.getEditTextCarLengthFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarLengthFrom().getText().toString()) > truckTypeInfo.getMaxLength()) {
            showMessageDialog("حداقل طول ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEditTextCarLengthTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarLengthTo().getText().toString()) < truckTypeInfo.getMinLength() || Float.parseFloat(view.getEditTextCarLengthTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarLengthTo().getText().toString()) > truckTypeInfo.getMaxLength()) {
            showMessageDialog("حداکثر طول ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarLengthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEditTextCarLengthTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarLengthTo().getText().toString()) < Float.parseFloat(view.getEditTextCarLengthFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarLengthFrom().getText().toString())) {
            showMessageDialog("حداکثر طول ماشین نمی تواند کوچکتر از حداقل طول ماشین باشد لطفا مقدار وارد شده را اصلاح نمایید.");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && view.getEditTextCarWidthFrom().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداقل عرض ماشین را مشخص کنید");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && view.getEditTextCarWidthTo().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداکثر عرض ماشین را مشخص کنید");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEditTextCarWidthFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarWidthFrom().getText().toString()) < truckTypeInfo.getMinWidth() || Float.parseFloat(view.getEditTextCarWidthFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarWidthFrom().getText().toString()) > truckTypeInfo.getMaxWidth()) {
            showMessageDialog("حداقل عرض ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEditTextCarWidthTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarWidthTo().getText().toString()) < truckTypeInfo.getMinWidth() || Float.parseFloat(view.getEditTextCarWidthTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarWidthTo().getText().toString()) > truckTypeInfo.getMaxWidth()) {
            showMessageDialog("حداکثر عرض ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarWidthView().getVisibility() == View.VISIBLE) && Float.parseFloat(view.getEditTextCarWidthTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarWidthTo().getText().toString()) < Float.parseFloat(view.getEditTextCarWidthFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarWidthFrom().getText().toString())) {
            showMessageDialog("حداکثر عرض ماشین نمی تواند کوچکتر از حداقل عرض ماشین باشد لطفا مقدار وارد شده را اصلاح نمایید.");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && view.getEditTextCarHeightFrom().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداقل عرض ماشین را مشخص کنید");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && view.getEditTextCarHeightTo().getText().toString().isEmpty()) {
            showMessageDialog("لطفا حداکثر عرض ماشین را مشخص کنید");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && (Float.parseFloat(view.getEditTextCarHeightFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarHeightFrom().getText().toString()) < truckTypeInfo.getMinHeight() || Float.parseFloat(view.getEditTextCarHeightFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarHeightFrom().getText().toString()) > truckTypeInfo.getMaxHeight())) {
            showMessageDialog("حداقل ارتفاع ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && (Float.parseFloat(view.getEditTextCarHeightTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarHeightTo().getText().toString()) < truckTypeInfo.getMinHeight() || Float.parseFloat(view.getEditTextCarHeightTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarHeightTo().getText().toString()) > truckTypeInfo.getMaxHeight())) {
            showMessageDialog("حداکثر ارتفاع ماشین وارد شده نامعتبر است، لطفا بین محدوده پیشنهاد شده وارد نمایید.");
        } else if ((view.getCarHeightView().getVisibility() == View.VISIBLE) && (Float.parseFloat(view.getEditTextCarHeightTo().getText().toString().isEmpty() ? "0" : view.getEditTextCarHeightTo().getText().toString()) < Float.parseFloat(view.getEditTextCarHeightFrom().getText().toString().isEmpty() ? "0" : view.getEditTextCarHeightFrom().getText().toString()))) {
            showMessageDialog("حداکثر ارتفاع ماشین نمی تواند کوچکتر از حداقل ارتفاع ماشین باشد لطفا مقدار وارد شده را اصلاح نمایید.");
        } else if (view.getEditTextLoadingType().getText().toString().isEmpty()) {
            showMessageDialog("لطفا نوع بار خود را مشخص نمایید");
        } else if (view.getEditTextLoadingWeight().getText().toString().isEmpty()) {
            showMessageDialog("وزن بار خود را تعیین نمایید");
        } else if (loadingFareTypeId == -1) {
            showMessageDialog("لطفا نوع کرایه بار خود را انتخاب نمایید");
        } else if (loadingFareTypeId != 2 && view.getEditTextLoadingFare().getText().toString().isEmpty()) {
            showMessageDialog("لطفا کرایه خود را برای این بار تعریف کنید");
        } else if (view.getEditTextLoadingTimeHint().getText().toString().isEmpty()) {
            showMessageDialog("لطفا زمان بارگیری خود را ذکر نمایید");
        }
//        else if (view.getImagViewLoadingPhotoPreview().getDrawable() == null) {
//            showMessageDialog("لطفا یک عکس برای بار خود انتخاب کنید");
//        }
        else if (PersianDateUtil.getDateFromPersianDateString(view.getEditTextLoadingTimeHint().getText().toString()).before(DateUtil.getStartOfDay(new Date()))) {
            showMessageDialog("زمان بارگیری وارد شده نامعتبر است، لطفا زمان بارگیری خود را از تاریخ امروز به بعد مشخص نمایید");
        } else if (!view.getTextViewDetermineDischargeTimeHint().getText().toString().isEmpty() && PersianDateUtil.getDateFromPersianDateString(view.getTextViewDetermineDischargeTimeHint().getText().toString()).before(DateUtil.getStartOfDay(new Date()))) {
            showMessageDialog("زمان تخلیه بار وارد شده نامعتبر است، لطفا زمان درست را وارد نمایید");
        } else if (view.getTextViewDetermineExpirationTimeHint().getText().toString().isEmpty()) {
            showMessageDialog("لطفا زمان انقضا خود را ذکر نمایید");
        } else if (PersianDateUtil.getDateFromPersianDateString(view.getTextViewDetermineExpirationTimeHint().getText().toString()).before(DateUtil.getStartOfDay(new Date()))) {
            showMessageDialog("زمان انقضا وارد شده نامعتبر است، لطفا زمان درست را وارد نمایید");
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
                String[] splitTruckType = view.getCarChooseHint().getText().toString().replace(" ، ", "/").split("/");
                String strTruckType = splitTruckType.length == 1 ? splitTruckType[0] : splitTruckType.length == 2 ? splitTruckType[0] + "/" + splitTruckType[1] : splitTruckType[0] + "/" +
                        splitTruckType[1] + "/" + splitTruckType[2];
                List<TruckType> listTruckType = Select.from(TruckType.class).where(Condition.prop("full_name").eq(strTruckType.replace(" ، ", "/"))).list();
                int truckTypeID = listTruckType.get(0).getTruckTypeId();
                float carLengthFrom = view.getEditTextCarLengthFrom().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEditTextCarLengthFrom().getText().toString());
                float carLengthTo = view.getEditTextCarLengthTo().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEditTextCarLengthTo().getText().toString());
                float carWidthFrom = view.getEditTextCarWidthFrom().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEditTextCarWidthFrom().getText().toString());
                float carWidthTo = view.getEditTextCarWidthTo().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEditTextCarWidthTo().getText().toString());
                float carHeightFrom = view.getCarHeightView().getVisibility() == View.GONE ? 0 : view.getEditTextCarHeightFrom().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEditTextCarHeightFrom().getText().toString());
                float carHeightTo = view.getCarHeightView().getVisibility() == View.GONE ? 0 : view.getEditTextCarHeightTo().getText().toString().isEmpty() ? 0 : Float.parseFloat(view.getEditTextCarHeightTo().getText().toString());
                String loadingType = view.getEditTextLoadingType().getText().toString();
                float loadingWeight = Float.parseFloat(PersianUtil.persianDigitToEnglish(view.getEditTextLoadingWeight().getText().toString().replaceAll(",", "").replaceAll("٬", "")));
                int loadingFare = loadingFareTypeId == 2 ? 0 : Integer.parseInt(PersianUtil.persianDigitToEnglish(view.getEditTextLoadingFare().getText().toString().replaceAll(",", "").replaceAll("٬", "")));
                long loadingDate = PersianDateUtil.getDateFromPersianDateString(view.getEditTextLoadingTimeHint().getText().toString()).getTime();
                String description = view.getEditTextDescription().getText().toString();

                String loadingPhoto = "";
                if (view.getImagViewLoadingPhotoPreview().getDrawable() != null) {
                    Bitmap bitmap = ImageUtil.getInstance(fragmentActivity).getBitmapFromDrawable(view.getImagViewLoadingPhotoPreview().getDrawable());
                    loadingPhoto = ImageUtil.getInstance(fragmentActivity).encodeImage(bitmap);
                } else {
                    loadingPhoto = "";
                }

                int warehouseSpending = view.getEditTextSpendingWarehouse().getText().toString().isEmpty() ? 0 : Integer.parseInt(PersianUtil.persianDigitToEnglish(view.getEditTextSpendingWarehouse().getText().toString().replaceAll(",", "").replaceAll("٬", "")));
                long dischargeTime = view.getTextViewDetermineDischargeTimeHint().getText().toString().isEmpty() ? 0 : PersianDateUtil.getDateFromPersianDateString(view.getTextViewDetermineDischargeTimeHint().getText().toString()).getTime();
                long expireTime = view.getTextViewDetermineExpirationTimeHint().getText().toString().isEmpty() ? 0 : PersianDateUtil.getDateFromPersianDateString(view.getTextViewDetermineExpirationTimeHint().getText().toString()).getTime();

                NewLoadInfo newLoadInfo = new NewLoadInfo("", listOrigins, listDestination, truckTypeID, carLengthFrom, carLengthTo, carWidthFrom, carWidthTo, carHeightFrom, carHeightTo, loadingType, loadingWeight, loadingFareTypeId, loadingFare, loadingDate, description, loadingPhoto, warehouseSpending, dischargeTime, expireTime, isShipmentGoingRound(), isRefundAfterArrival(), isHavingWellTent());

                ObjectMapper mapper = new ObjectMapper();
                String jsonInString = mapper.writeValueAsString(newLoadInfo);

                OkHttpHelper okHttpHelper = new OkHttpHelper(fragmentActivity, Configuration.API_NEW_LOADING, OkHttpHelper.MEDIA_TYPE_JSON);
                okHttpHelper.doNewLoading(jsonInString, new OkHttpHelper.OnCallback() {
                    @Override
                    public void onStart() {
                        ((MainActivity) fragmentActivity).showDialogProgress("در حال ارسال اطلاعات به سرور");
                    }

                    @Override
                    public void onResponse(JSONObject result) {
                        loadNewLoading(result);
                    }

                    @Override
                    public void onRequestReject(String message) {
                        ((MainActivity) fragmentActivity).showToastWarning(message);
                        ((MainActivity) fragmentActivity).dismissDialogProgress();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        ((MainActivity) fragmentActivity).showToastError(errorMessage);
                        ((MainActivity) fragmentActivity).dismissDialogProgress();
                    }

                    @Override
                    public void onNoInternetConnection() {
                        ((MainActivity) fragmentActivity).dismissDialogProgress();
                    }

                    @Override
                    public void onFinish() {
                        ((MainActivity) fragmentActivity).dismissDialogProgress();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadNewLoading(final JSONObject result) {
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        view.removeCroppedImage();
                        ((MainActivity) fragmentActivity).showToastSuccess(result.getString("message"));
                        clearAllPageStuffs();
                    } else {
                        ((MainActivity) fragmentActivity).showToastError(result.getString("message"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void clearAllPageStuffs() {
        listPlacesInfos = null;
        strPlacesInfo = "";
        truckTypeInfo = null;
        loadingFareTypeId = 1;
        setRefundAfterArrival(false);
        setShipmentGoingRound(false);
        setHavingWellTent(false);

        view.getLocationHint().setText("");
        view.getLocationHint().setVisibility(View.GONE);
        view.getCarChooseHint().setText("");
        view.getCarChooseHint().setVisibility(View.GONE);
        view.getEditTextCarLengthFrom().setText("");
        view.getEditTextCarLengthTo().setText("");
        view.getEditTextCarWidthFrom().setText("");
        view.getEditTextCarWidthTo().setText("");
        view.getEditTextCarHeightFrom().setText("");
        view.getEditTextCarHeightTo().setText("");
        view.getEditTextLoadingType().setText("");
        view.getEditTextLoadingWeight().setText("");
        view.getEditTextLoadingFare().setText("");
        view.getEditTextLoadingTimeHint().setText(PersianDateUtil.getPersianDate(new Date().getTime()));
        view.getEditTextDescription().setText("");
        view.getImagViewLoadingPhotoPreview().setVisibility(View.GONE);
        view.getImagViewLoadingPhotoPreview().setImageDrawable(null);
        view.getEditTextSpendingWarehouse().setText("");
        view.getTextViewDetermineDischargeTimeHint().setText("");
        view.getTextViewDetermineExpirationTimeHint().setText(PersianDateUtil.getPersianDate(new Date().getTime() + 3 * 24 * 60 * 60 * 1000));
        view.getSwithchButtonPaymentIsPaidAfterLoadingArriveToDestination().setChecked(false);
        view.getSwithchButtonLoadingIsGoingRound().setChecked(false);
        view.getSwithchButtonHavingIntactTent().setChecked(false);

        loadingFareTypeAdapter.mSelectedItem = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.getLoadingFareTypeRecyclerView().findViewHolderForAdapterPosition(0) != null) {
                    view.getLoadingFareTypeRecyclerView().findViewHolderForAdapterPosition(0).itemView.performClick();
                    view.getTextViewLoadingFareTypeHint().setText("(" + loadingFareType.get(0).getName() + ")");
                    view.showLoadingFareView();
                    loadingFareTypeAdapter.notifyDataSetChanged();
                }
            }
        }, 1);

        view.collapseAllExpandableLayout();

        view.getSvContainer().scrollTo(0, 0);
    }

    public void showMessageDialog(String message) {
        final MessageDialog messageDialog = new MessageDialog(fragmentActivity, fragmentActivity.getString(R.string.app_name), message);
        messageDialog.setOnClickListener(new MessageDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                messageDialog.dismiss();
            }
        });
        messageDialog.show();
    }

    public boolean isShipmentGoingRound() {
        return isShipmentGoingRound;
    }

    public void setShipmentGoingRound(boolean shipmentGoingRound) {
        isShipmentGoingRound = shipmentGoingRound;
    }

    public boolean isRefundAfterArrival() {
        return refundAfterArrival;
    }

    public void setRefundAfterArrival(boolean refundAfterArrival) {
        this.refundAfterArrival = refundAfterArrival;
    }

    public boolean isHavingWellTent() {
        return havingWellTent;
    }

    public void setHavingWellTent(boolean havingWellTent) {
        this.havingWellTent = havingWellTent;
    }

    public BroadcastReceiver broadcastRequestCarType = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String carType = intent.getStringExtra("car_type");

                ObjectMapper mapper = new ObjectMapper();
                truckTypeInfo = mapper.readValue(carType, TruckTypeInfo.class);

                // set truck full name
                String[] splitFullname = truckTypeInfo.getFullName().split("/");
                if (splitFullname.length == 2)
                    view.setCarChooserHint(splitFullname[0] + " ، " + splitFullname[1]);
                else if (splitFullname.length == 3)
                    view.setCarChooserHint(splitFullname[0] + " ، " + splitFullname[1] + " ، " + splitFullname[2]);

                if (truckTypeInfo.getMinLength() > 0 && truckTypeInfo.getMaxLength() > 0) {
                    view.getCarLengthView().setVisibility(View.VISIBLE);
                    view.setCarLengthHeaderLabel("بازه از " + truckTypeInfo.getMinLength() + " تا " + truckTypeInfo.getMaxLength() + " متر");
                    view.getEditTextCarLengthFrom().setText(truckTypeInfo.getMinLength() + "");
                    view.getEditTextCarLengthTo().setText(truckTypeInfo.getMaxLength() + "");
                } else
                    view.getCarLengthView().setVisibility(View.GONE);

                if (truckTypeInfo.getMinWidth() > 0 && truckTypeInfo.getMaxWidth() > 0) {
                    view.getCarWidthView().setVisibility(View.VISIBLE);
                    view.setCarWidthHeaderLabel("بازه از " + truckTypeInfo.getMinWidth() + " تا " + truckTypeInfo.getMaxWidth() + " متر");
                    view.getEditTextCarWidthFrom().setText(truckTypeInfo.getMinWidth() + "");
                    view.getEditTextCarWidthTo().setText(truckTypeInfo.getMaxWidth() + "");
                } else
                    view.getCarWidthView().setVisibility(View.GONE);

                if (truckTypeInfo.getMinHeight() > 0 && truckTypeInfo.getMaxHeight() > 0) {
                    view.getCarHeightView().setVisibility(View.VISIBLE);
                    view.setCarHeightHeaderLabel("بازه از " + truckTypeInfo.getMinHeight() + " تا " + truckTypeInfo.getMaxHeight() + " متر");
                    view.getEditTextCarHeightFrom().setText(truckTypeInfo.getMinHeight() + "");
                    view.getEditTextCarHeightTo().setText(truckTypeInfo.getMaxHeight() + "");
                } else
                    view.getCarHeightView().setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
}
