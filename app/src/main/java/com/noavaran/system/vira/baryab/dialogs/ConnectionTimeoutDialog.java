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
import com.noavaran.system.vira.baryab.utils.DialerUtil;

public class ConnectionTimeoutDialog extends Dialog {
    private Context context;

    private CustomTextView btnContact;
    private CustomTextView btnCancel;

    public ConnectionTimeoutDialog(@NonNull Context context) {
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
        setContentView(R.layout.dialog_connection_timeout);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Window;
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void findViews() {
        btnContact = (CustomTextView) findViewById(R.id.dgSyncFailure_btnContact);
        btnCancel = (CustomTextView) findViewById(R.id.dgSyncFailure_btnCancel);
    }

    private void initComponents() {
    }

    private void setViewsListeners() {
        btnContact.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DialerUtil.getInstance(context).openTheDialerAppAndDoTheCallAutomatically("09305258595");
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dismiss();
            }
        });
    }
}