package com.noavaran.system.vira.baryab.utils;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class AnimationUtil {
    private static AnimationUtil animationUtil;
    private Context context;

    private AnimationUtil(Context context) {
        this.context = context;
    }

    public static AnimationUtil getInstance(Context context) {
        if (animationUtil == null)
            animationUtil = new AnimationUtil(context);

        return animationUtil;
    }

    /**
     * Make the TextView's text blinking
     *
     * @param textView
     * @param duration
     * @param startOffset
     * @param repeatCount
     *
     * @example AnimationUtil.getInstance(MainActivity.this).blinking(myTextView, 50, 20, Animation.INFINITE);
     */
    public void blinking(TextView textView, int duration, int startOffset, int repeatCount) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(duration);
        anim.setStartOffset(startOffset);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(repeatCount);
        textView.startAnimation(anim);
    }
}