package com.noavaran.system.vira.baryab;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.noavaran.system.vira.baryab.customviews.CustomToast;
import com.noavaran.system.vira.baryab.dialogs.MessageDialog;

public class BaseActivity extends AppCompatActivity {
    private String TAG;
    private View view;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initStrictModeThreadPolicy();
//        hideStatusbar();
        hideActionBar();
        initNavigationBarColor();
        initStatusBarColor();
        initDialogProgress();
    }

    private void initStrictModeThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void hideStatusbar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void hideActionBar() {
        getSupportActionBar().hide();
    }

    // if phone is running Android API version above Lollipop
    // set navigation bar color
    private void initNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    // if phone is running Android API version above Lollipop
    // set status bar color
    private void initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void initDialogProgress() {
        view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
        TextView txt = (TextView) view.findViewById(R.id.txt);
        txt.setText("در حال دریافت اطلاعات از سرور");
        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setCancelable(false);
    }

    public void showDialogProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing()) {
                    pDialog.show();
                    pDialog.setContentView(view);
                }
            }
        });
    }

    public void showDialogProgress(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view = getLayoutInflater().inflate(R.layout.dialog_progress, null);
                TextView txt = (TextView) view.findViewById(R.id.txt);
                txt.setText(message);
                showDialogProgress();
            }
        });
    }

    public void dismissDialogProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing()) {
                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                }
            }
        });
    }

    public void showMessageDialog(String message) {
        showMessageDialog(getString(R.string.app_name), message);
    }

    public void showMessageDialog(String title, String message) {
        final MessageDialog messageDialog = new MessageDialog(BaseActivity.this, title, message);
        messageDialog.setOnClickListener(new MessageDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                messageDialog.dismiss();
            }
        });
        messageDialog.show();
    }

    public void showToastInfo(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomToast.makeText(BaseActivity.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_INFO);
            }
        });
    }

    public void showToastSuccess(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomToast.makeText(BaseActivity.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_SUCCESS);
            }
        });
    }

    public void showToastWarning(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomToast.makeText(BaseActivity.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_WARNING);
            }
        });
    }

    public void showToastError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomToast.makeText(BaseActivity.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_ERROR);
            }
        });
    }

    public void showSnackbar(final View view, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
