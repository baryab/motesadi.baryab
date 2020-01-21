package com.noavaran.system.vira.baryab.customviews;

import android.content.Context;
import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noavaran.system.vira.baryab.R;

public class CustomTabLayout extends TabLayout {
    private Context context;
    private Typeface mTypeface;

    public CustomTabLayout(Context context) {
        super(context);
        this.context = context;

        init();
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), context.getString(R.string.baseFont));
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);

        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
            }
        }
    }
}