package com.noavaran.system.vira.baryab.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import com.noavaran.system.vira.baryab.AppController;
import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomCircleImageView;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.ImagePicker;
import com.noavaran.system.vira.baryab.utils.ImageUtil;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;

public class ProfileActivity extends BaseActivity {
    private CustomCircleImageView ivProfileImage;
    private CustomTextView tvFullName;
    private RatingBar rbUserRating;
    private CustomTextView btnAddNewPhoto;
    private CustomTextView etNationalCode;
    private CustomTextView etMobileNumber;
    private CustomTextView etCompanyName;
    private CustomTextView etCompanyChairMan;
    private CustomTextView etCompanyRegisterationNumber;
    private CustomTextView etCompanyPhoneNumber;
    private CustomTextView etCompanyAddress;
    private CustomTextView btnSave;

    private boolean canUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViews();
        initComponents();
        setViewsListeners();
        getProfileFromServer();
    }

    private void findViews() {
        ivProfileImage = (CustomCircleImageView) findViewById(R.id.acProfile_ivProfileImage);
        tvFullName = (CustomTextView) findViewById(R.id.acProfile_tvFullName);
        rbUserRating = (RatingBar) findViewById(R.id.acProfile_rbUserRating);
        btnAddNewPhoto = (CustomTextView) findViewById(R.id.acProfile_btnAddNewPhoto);
        etNationalCode = (CustomTextView) findViewById(R.id.acProfile_etNationalCode);
        etMobileNumber = (CustomTextView) findViewById(R.id.acProfile_etMobileNumber);
        etCompanyName = (CustomTextView) findViewById(R.id.acProfile_etCompanyName);
        etCompanyChairMan = (CustomTextView) findViewById(R.id.acProfile_etCompanyChairMan);
        etCompanyRegisterationNumber = (CustomTextView) findViewById(R.id.acProfile_etCompanyRegisterationNumber);
        etCompanyPhoneNumber = (CustomTextView) findViewById(R.id.acProfile_etCompanyPhoneNumber);
        etCompanyAddress = (CustomTextView) findViewById(R.id.acProfile_etCompanyAddress);
        btnSave = (CustomTextView) findViewById(R.id.acProfile_btnSave);
    }

    private void initComponents() {

    }

    private void setViewsListeners() {
        btnAddNewPhoto.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ImagePicker.pickImage(ProfileActivity.this, "لطفا عکس پروفایل خود را مشخص نمایید");
            }
        });

        btnSave.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (canUpload) {
                    uploadPictureToServer();
                } else {
                    showMessageDialog("هیچ عکسی جهت تغییرات و ارسال آن به سرور توسط شما تعیین نگردیده است.");
                }
            }
        });
    }

    private void getProfileFromServer() {
        OkHttpHelper okHttpHelper = new OkHttpHelper(ProfileActivity.this, Configuration.API_PROFILE, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.getProfile(new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                loadProfile(result);
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

    private void loadProfile(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject data = result.optJSONObject("data");
                    JSONObject ufo = data.optJSONObject("Ufo");
                    JSONObject comfo = data.optJSONObject("comfo");

                    tvFullName.setText(ufo.optString("name"));
                    rbUserRating.setRating(BigDecimal.valueOf(data.optDouble("rate")).floatValue());
                    etNationalCode.setText(ufo.optString("NationCode"));
                    etMobileNumber.setText(ufo.optString("Mobile"));
                    Picasso.with(ProfileActivity.this).load(Configuration.BASE_IMAGE_URL + ufo.optString("Picpath").replace("~", "")).placeholder(R.drawable.ic_no_image_found).error(R.drawable.ic_no_image_found).memoryPolicy(MemoryPolicy.NO_CACHE ).networkPolicy(NetworkPolicy.NO_CACHE).into(ivProfileImage);
                    etCompanyName.setText(comfo.optString("comName"));
                    etCompanyChairMan.setText(comfo.optString("comModir"));
                    etCompanyRegisterationNumber.setText(comfo.optString("comRegister"));
                    etCompanyPhoneNumber.setText(comfo.optString("comTel"));
                    etCompanyAddress.setText(comfo.optString("comAddress"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void uploadPictureToServer() {
        Bitmap bitmap = ((BitmapDrawable) ivProfileImage.getDrawable()).getBitmap();
        String strEncodPicture = ImageUtil.getInstance(ProfileActivity.this).encodeImage(bitmap);

        OkHttpHelper okHttpHelper = new OkHttpHelper(ProfileActivity.this, Configuration.API_UPLOAD_PROFILE_PICTURE, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.uploadProfilePicture(strEncodPicture, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                dismissDialogProgress();
                loadUploadPicture(result);
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

    private void loadUploadPicture(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    canUpload = false;

                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ProfileActivity.this.RESULT_OK && requestCode == ImagePicker.mPickImageRequestCode) {
            Bitmap bitmap = ImagePicker.getImageFromResult(AppController.getContext(), requestCode, resultCode, data);

            Uri tempUri = ImageUtil.getInstance(ProfileActivity.this).getImageUri(bitmap);
            File finalFile = new File(ImageUtil.getInstance(ProfileActivity.this).getRealPathFromURI(tempUri));

            startCropImage(finalFile.getPath());
        } else if (resultCode == ProfileActivity.this.RESULT_OK && requestCode == Configuration.REQUEST_CODE_CROP_IMAGE) {
            String path = data.getStringExtra(ImageCropperActivity.IMAGE_PATH);
            if (path == null) {
                return;
            }

            Bitmap bitmap = ImageUtil.getInstance(ProfileActivity.this).getBitmapFromFilePath(path, "001.jpg", 64, 64);
            ivProfileImage.setImageBitmap(bitmap);
            canUpload = true;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startCropImage(String filePath) {
        Intent intent = new Intent(AppController.getContext(), ImageCropperActivity.class);
        intent.putExtra(ImageCropperActivity.IMAGE_PATH, filePath);
        intent.putExtra(ImageCropperActivity.SCALE, true);
        intent.putExtra(ImageCropperActivity.ASPECT_X, 3);
        intent.putExtra(ImageCropperActivity.ASPECT_Y, 3);

        startActivityForResult(intent, Configuration.REQUEST_CODE_CROP_IMAGE);
    }
}