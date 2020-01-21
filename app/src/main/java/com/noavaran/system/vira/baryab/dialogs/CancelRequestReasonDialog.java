package com.noavaran.system.vira.baryab.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomEditText;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

public class CancelRequestReasonDialog extends Dialog {
    private Context context;
    private RadioGroup rgStatus;
    private RadioButton rbOption;
    private CustomEditText etDescription;
    private CustomTextView btnSend;
    private CustomTextView btnCancel;

    private OnDialogButtonsClickListener onDialogButtonsClickListener;

    public interface OnDialogButtonsClickListener {
        public abstract void onConfirm(String reason);

        public abstract void onCancel();
    }

    public CancelRequestReasonDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        findViews();
        initComponents();
        setViewsListeners();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_cancel_request_reason);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void findViews() {
        rgStatus = (RadioGroup) findViewById(R.id.dgCancelRequestReason_rgStatus);
        etDescription = (CustomEditText) findViewById(R.id.dgCancelRequestReason_etDescription);
        btnSend = (CustomTextView) findViewById(R.id.dgCancelRequestReason_btnSend);
        btnCancel = (CustomTextView) findViewById(R.id.dgCancelRequestReason_btnCancel);
    }

    private void initComponents() {

    }

    private void setViewsListeners() {
        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.dgCancelRequestReason_rbOption1:
                        etDescription.setVisibility(View.GONE);
                        break;
                    case R.id.dgCancelRequestReason_rbOption2:
                        etDescription.setVisibility(View.GONE);
                        break;
                    case R.id.dgCancelRequestReason_rbOption3:
                        etDescription.setVisibility(View.GONE);
                        break;
                    case R.id.dgCancelRequestReason_rbOption4:
                        etDescription.setVisibility(View.GONE);
                        break;
                    case R.id.dgCancelRequestReason_rbOption5:
                        etDescription.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnCancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onDialogButtonsClickListener.onCancel();
                dismiss();
            }
        });

        btnSend.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String reason = "";

                int selectedId = rgStatus.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.dgCancelRequestReason_rbOption1:
                        reason = "فاصله راننده تا مبدا زیاد است";
                        break;
                    case R.id.dgCancelRequestReason_rbOption2:
                        reason = "برخورد و رفتار بد راننده";
                        break;
                    case R.id.dgCancelRequestReason_rbOption3:
                        reason = "راننده تقاضای لغو نمود";
                        break;
                    case R.id.dgCancelRequestReason_rbOption4:
                        reason = "اطلاعات راننده با اطلاعات ثبتی مقایرت دارد";
                        break;
                    case R.id.dgCancelRequestReason_rbOption5:
                        reason = etDescription.getText().toString();
                        break;
                    default:
                        reason = "فاصله راننده تا مبدا زیاد است";
                        break;
                }

                onDialogButtonsClickListener.onConfirm(reason);
                dismiss();
            }
        });
    }

    public void setOnDialogButtonsClickListener(OnDialogButtonsClickListener onDialogButtonsClickListener) {
        this.onDialogButtonsClickListener = onDialogButtonsClickListener;
    }
}