package com.noavaran.system.vira.baryab.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomEditText;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

public class InputDialog extends Dialog {
    private Context context;
    private String title;
    private String editTextHint;

    private RelativeLayout rlToolbar;
    private CustomTextView tvTitle;
    private CustomEditText etUserInput;
    private CustomTextView btnConfirm;
    private CustomTextView btnCancel;

    private OnDialogButtonsClickListener onDialogButtonsClickListener;
    public interface OnDialogButtonsClickListener {
        public abstract void onConfirm(String enteredText);
        public abstract void onCancel();
    }

    public InputDialog(@NonNull Context context, String title, String editTextHint) {
        super(context);
        this.context = context;
        this.title = title;
        this.editTextHint = editTextHint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_input);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        findViews();
        initComponents();
        setViewsListeners();

        setTitle(title);
        setUserInputEditTextHint(editTextHint);
    }

    private void findViews() {
        rlToolbar = (RelativeLayout) findViewById(R.id.dgInput_rlToolbar);
        tvTitle = (CustomTextView) findViewById(R.id.dgInput_tvTitle);
        etUserInput = (CustomEditText) findViewById(R.id.dgInput_etUserInput);
        btnConfirm = (CustomTextView) findViewById(R.id.dgInput_btnConfirm);
        btnCancel = (CustomTextView) findViewById(R.id.dgInput_btnCancel);
    }

    private void initComponents() {

    }

    private void setViewsListeners() {
        btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onDialogButtonsClickListener.onConfirm(etUserInput.getText().toString());
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onDialogButtonsClickListener.onCancel();
                dismiss();
            }
        });
    }

    private void setTitle(String title) {
        if (title == null || title.isEmpty())
            rlToolbar.setVisibility(View.GONE);
        else
            tvTitle.setText(title);
    }

    private void setUserInputEditTextHint(String hint) {
        etUserInput.setHint(hint);
    }

    public void setOnDialogButtonsClickListener(OnDialogButtonsClickListener onDialogButtonsClickListener) {
        this.onDialogButtonsClickListener = onDialogButtonsClickListener;
    }
}
