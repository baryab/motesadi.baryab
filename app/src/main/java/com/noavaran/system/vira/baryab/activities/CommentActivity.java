package com.noavaran.system.vira.baryab.activities;

import android.os.Bundle;
import android.view.View;

import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.Configuration;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomButton;
import com.noavaran.system.vira.baryab.customviews.CustomEditText;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;
import com.noavaran.system.vira.baryab.utils.okhttp.OkHttpHelper;

import org.json.JSONObject;

public class CommentActivity extends BaseActivity {
    private CustomTextView btnBack;
    private CustomEditText etCommentTitle;
    private CustomEditText etCommentDesc;
    private CustomButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        findViews();
        initComponents();
        setViewsListeners();
    }

    private void findViews() {
        btnBack = (CustomTextView) findViewById(R.id.acComment_btnBack);
        etCommentTitle = (CustomEditText) findViewById(R.id.acComment_etCommentTitle);
        etCommentDesc = (CustomEditText) findViewById(R.id.acComment_etCommentDesc);
        btnLogin = (CustomButton) findViewById(R.id.acComment_btnLogin);
    }

    private void initComponents() {
    }

    private void setViewsListeners() {
        btnBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (etCommentTitle.getText().toString().isEmpty()) {
                    showMessageDialog("لطفا عنوان انتقادات و پیشنهادات خود را مشخص نمایید");
                } else if (etCommentDesc.getText().toString().isEmpty()) {
                    showMessageDialog("لطفا یک متن برای انتقادات و پیشنهادات خود وارد نمایید نمایید");
                } else {
                    sendCommentToServer(etCommentTitle.getText().toString(), etCommentDesc.getText().toString());
                }
            }
        });
    }

    private void sendCommentToServer(String subject, String body) {
        OkHttpHelper okHttpHelper = new OkHttpHelper(CommentActivity.this, Configuration.API_COMMENTS, OkHttpHelper.MEDIA_TYPE_JSON);
        okHttpHelper.sendUserComment(subject, body, new OkHttpHelper.OnCallback() {
            @Override
            public void onStart() {
                showDialogProgress();
            }

            @Override
            public void onResponse(JSONObject result) {
                dismissDialogProgress();
                loadComment(result);
            }

            @Override
            public void onRequestReject(String message) {
                showToastWarning(message);
                dismissDialogProgress();
            }

            @Override
            public void onFailure(String errorMessage) {
                showToastError(errorMessage);
                dismissDialogProgress();
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

    private void loadComment(final JSONObject result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (result.getBoolean("result")) {
                        showToastSuccess("نظر شما با موفقیت ارسال شد");
                        finish();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}