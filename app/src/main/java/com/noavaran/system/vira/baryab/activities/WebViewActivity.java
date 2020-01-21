package com.noavaran.system.vira.baryab.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.noavaran.system.vira.baryab.BaseActivity;
import com.noavaran.system.vira.baryab.R;
import com.noavaran.system.vira.baryab.customviews.CustomTextView;
import com.noavaran.system.vira.baryab.listeners.OnSingleClickListener;

public class WebViewActivity extends BaseActivity {
    private CustomTextView btnBack;
    private CustomTextView tvTitle;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        findViews();
        initComponents();
        setViewListeners();
        getIntents();
    }

    private void findViews() {
        btnBack = (CustomTextView) findViewById(R.id.acWebView_btnBack);
        tvTitle = (CustomTextView) findViewById(R.id.acWebView_tvTitle);
        webview = (WebView) findViewById(R.id.acWebView_webview);
    }

    private void initComponents() {
        initWebView();
    }

    private void setViewListeners(){
        btnBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                finish();
            }
        });
    }

    private void getIntents() {
        if (getIntent().getExtras() != null) {
            String title = getIntent().getExtras().getString("title");
            String url = getIntent().getExtras().getString("url");

            tvTitle.setText(title);
            webview.loadUrl(url);
        }
    }

    private void initWebView() {
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.setWebViewClient(new WebViewClient());
    }
}