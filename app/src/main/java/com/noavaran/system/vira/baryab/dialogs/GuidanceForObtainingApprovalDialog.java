package com.noavaran.system.vira.baryab.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

public class GuidanceForObtainingApprovalDialog extends Dialog {
    private Context context;
    private CustomTextView btnCall;
    private CustomTextView btnClose;

    private OnDialogButtonsClickListener onDialogButtonsClickListener;
    public interface OnDialogButtonsClickListener {
        public abstract void onCall();
        public abstract void onClose();
    }

    public GuidanceForObtainingApprovalDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_guidance_for_obtaining_approval);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        findViews();
        initComponents();
        setViewsListeners();
    }

    private void findViews() {
        btnCall = (CustomTextView) findViewById(R.id.dgGuidanceForObtainingApproval_btnCall);
        btnClose = (CustomTextView) findViewById(R.id.dgGuidanceForObtainingApproval_btnClose);
    }

    private void initComponents() {

    }

    private void setViewsListeners() {
        btnCall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onDialogButtonsClickListener.onCall();
                dismiss();
            }
        });

        btnClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onDialogButtonsClickListener.onClose();
                dismiss();
            }
        });
    }

    public void setOnDialogButtonsClickListener(OnDialogButtonsClickListener onDialogButtonsClickListener) {
        this.onDialogButtonsClickListener = onDialogButtonsClickListener;
    }
}
