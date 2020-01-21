package com.noavaran.system.vira.baryab.utils;

import android.content.Context;
import android.text.InputFilter;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.noavaran.system.vira.baryab.R;

public class EditTextUtil {
    private static EditTextUtil editTextUtil;
    private Context context;

    public EditTextUtil(Context context) {
        this.context = context;
    }

    public static EditTextUtil getInstance(Context context) {
        if (editTextUtil == null)
            editTextUtil = new EditTextUtil(context);

        return editTextUtil;
    }

    public void setMaxNumberOfCharactersForEditText(EditText editText, int maxLength) {
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
    }

    public void setCursorAtTheEndOfTextInEditText(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    public void setRequestFocus(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        setCursorAtTheEndOfTextInEditText(editText);
    }

    public void showErrorAndShake(EditText editText, String message) {
        editText.requestFocus();
        shake(editText);
        showError(editText, message);
    }

    public void shake(EditText editText) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        editText.startAnimation(shake);
    }

    public void showError(EditText editText, String message) {
        editText.setError(message);
    }
}