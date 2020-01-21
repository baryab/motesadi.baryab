package com.noavaran.system.vira.baryab.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.activities.CarriedShipmentDetailActivity;
import com.noavaran.system.vira.baryab.customviews.CustomRatingBar;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.GlobalUtils;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class DriverRatingDialog extends Dialog {
    private Context context;

    private CustomTextView tvTitle;
    private ImageView ivDriverPhoto;
    private CustomTextView tvDriverName;
    private CustomTextView tvDriverCarType;
    private CustomTextView tvPlateLicenseNumber;
    private CustomTextView tvPlateLicenseIranNumber;
    private CustomRatingBar rbDriverRating;
    private ImageView ivSignature;
    private CustomTextView tvSignatureOwner;
    private CustomTextView btnConfirm;

    private String draftId;
    private float userRate = 0;
    private JSONObject result;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        public abstract void onConfirm();
    }

    public DriverRatingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        findViews();
        initComponents();
        setViewsListeners();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_driver_rating);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void findViews() {
        tvTitle = (CustomTextView) findViewById(R.id.dgDriverRating_tvTitle);
        ivDriverPhoto = (ImageView) findViewById(R.id.dgDriverRating_ivDriverPhoto);
        tvDriverName = (CustomTextView) findViewById(R.id.dgDriverRating_tvDriverName);
        tvDriverCarType = (CustomTextView) findViewById(R.id.dgDriverRating_tvDriverCarType);
        tvPlateLicenseNumber = (CustomTextView) findViewById(R.id.dgDriverRating_tvPlateLicenseNumber);
        tvPlateLicenseIranNumber = (CustomTextView) findViewById(R.id.dgDriverRating_tvPlateLicenseIranNumber);
        rbDriverRating = (CustomRatingBar) findViewById(R.id.dgDriverRating_rbDriverRating);
        ivSignature = (ImageView) findViewById(R.id.dgDriverRating_ivSignature);
        tvSignatureOwner = (CustomTextView) findViewById(R.id.dgDriverRating_tvSignatureOwner);
        btnConfirm = (CustomTextView) findViewById(R.id.dgDriverRating_btnConfirm);
    }

    private void initComponents() {
        try {
            JSONObject data = result.optJSONObject("data");
            JSONObject dfo = data.optJSONObject("Dfo");
            JSONObject ufo = dfo.optJSONObject("Ufo");
            JSONObject tfo = data.optJSONObject("Tfo");
            JSONObject draft = data.optJSONObject("d");

            tvTitle.setText(tfo.optString("Type"));
            Picasso.with(context).load(Configuration.BASE_IMAGE_URL + ufo.optString("Pic")).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(ivDriverPhoto);
            tvDriverName.setText(ufo.optString("Name"));
            tvDriverCarType.setText(tfo.optString("Type").replace("/", " ، "));

            if (!GlobalUtils.IsNullOrEmpty(tfo.optString("plake"))) {
                String[] splitPelak = tfo.optString("plake").split(" ");
                tvPlateLicenseNumber.setText(splitPelak[0] + " " + splitPelak[1] + " " + splitPelak[2]);
                tvPlateLicenseIranNumber.setText(splitPelak[3]);
            } else {
                tvPlateLicenseNumber.setText("");
                tvPlateLicenseIranNumber.setText("");
            }

            Picasso.with(context).load(Configuration.BASE_IMAGE_URL + data.optString("sign")).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).into(ivSignature);
            tvSignatureOwner.setText(data.optString("Name"));

            draftId = draft.optString("Id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setViewsListeners() {
        btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (userRate > 0) {
                    doRate(draftId, userRate);
                } else {
                    ((CarriedShipmentDetailActivity) context).showToastWarning("لطفا امتیاز راننده را مشخص کنید");
                }
            }
        });

        rbDriverRating.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                userRate = score;
            }
        });
    }

    public void setData(final JSONObject result) {
        this.result = result;
    }

    private void doRate(String draftId, float rate) {
        new OkHttpHelper(context, Configuration.API_LOAD_RATE, OkHttpHelper.MEDIA_TYPE_JSON).setRateForLoad(draftId, rate, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                ((CarriedShipmentDetailActivity) context).showDialogProgress("در حال ارسال اطلاعات به سرور");
            }

            @Override
            public void onResponse(JSONObject result) {
                prepareData(result);
            }

            @Override
            public void onRequestReject(String message) {
                ((CarriedShipmentDetailActivity) context).showToastWarning(message);
                ((CarriedShipmentDetailActivity) context).dismissDialogProgress();

                dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                ((CarriedShipmentDetailActivity) context).showToastError(errorMessage);
                ((CarriedShipmentDetailActivity) context).dismissDialogProgress();

                dismiss();
            }

            @Override
            public void onNoInternetConnection() {
                ((CarriedShipmentDetailActivity) context).dismissDialogProgress();
            }

            @Override
            public void onFinish() {
                ((CarriedShipmentDetailActivity) context).dismissDialogProgress();
            }
        });
    }

    private void prepareData(final JSONObject result) {
        ((CarriedShipmentDetailActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        ((CarriedShipmentDetailActivity) context).showToastInfo(result.optString("message"));
                    } else {
                        ((CarriedShipmentDetailActivity) context).showToastError("خطا در ارسال اطلاعات به سرور");
                    }

                    onClickListener.onConfirm();
                    dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}