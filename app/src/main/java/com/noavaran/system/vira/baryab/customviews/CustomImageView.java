package com.noavaran.system.vira.baryab.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.noavaran.system.vira.baryab.R;

public class CustomImageView extends AppCompatImageView {
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawableTint(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDrawableTint(context, attrs);
    }

    private void setDrawableTint(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        int drawableTint = a.getColor(R.styleable.CustomImageView_ivDrawableTint, 0xff000000);
        setDrawableTint(ctx, drawableTint);
        a.recycle();
    }

    public boolean setDrawableTint(Context ctx, int drawableTint) {
        setColorFilter(drawableTint, PorterDuff.Mode.SRC_ATOP);
        return true;
    }
}