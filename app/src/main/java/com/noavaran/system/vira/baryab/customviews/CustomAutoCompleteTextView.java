package com.noavaran.system.vira.baryab.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

import com.noavaran.system.vira.baryab.R;

import java.util.HashMap;

public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomAutoCompleteTextView);
        String customFont = a.getString(R.styleable.CustomAutoCompleteTextView_cactvCustomFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        setTypeface(tf);
        return true;
    }
}