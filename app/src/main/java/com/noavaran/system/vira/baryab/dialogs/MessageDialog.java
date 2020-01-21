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
import com.noavaran.system.vira.baryab.customviews.CustomTextView;

public class MessageDialog extends Dialog {
    private Context context;
    private String title;
    private String message;

    private RelativeLayout rlToolbar;
    private CustomTextView tvTitle;
    private CustomTextView tvMessage;

    private CustomTextView btnConfirm;

    private OnClickListener onClickListener;
    public interface OnClickListener {
        public abstract void onConfirm();
    }

    public MessageDialog(@NonNull Context context, String title, String message) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_message);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        findViews();
        initComponents();
        setViewsListeners();

        setTitle(title);
        setMessage(message);
    }

    private void findViews() {
        rlToolbar = (RelativeLayout) findViewById(R.id.dgMessage_rlToolbar);
        tvTitle = (CustomTextView) findViewById(R.id.dgMessage_tvTitle);
        tvMessage = (CustomTextView) findViewById(R.id.dgMessage_tvMessage);
        btnConfirm = (CustomTextView) findViewById(R.id.dgMessage_btnConfirm);
    }

    private void initComponents() {

    }

    private void setViewsListeners() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                if (onClickListener != null)
                    onClickListener.onConfirm();
            }
        });
    }

    private void setTitle(String title) {
        if (title == null || title.isEmpty())
            rlToolbar.setVisibility(View.GONE);
        else
            tvTitle.setText(title);
    }

    private void setMessage(String message) {
        tvMessage.setText(message);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
