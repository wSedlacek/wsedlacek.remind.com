package com.remind.wsedlacek.forgetmenot.feature.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.remind.wsedlacek.forgetmenot.feature.R;

public class Wobble {
    private static Animation getWobble(Context tContext) {
        return getWobble(tContext, -1);
    }

    private static Animation getWobble(Context tContext, long tDuration) {
        Animation tAnimation = AnimationUtils.loadAnimation(tContext, R.anim.wobble);
        int tRepeat = Math.round(tDuration / tAnimation.getDuration());
        if (tDuration != -1) tAnimation.setRepeatCount(tRepeat);
        return tAnimation;
    }

    public static void animateBuzz(View tView, Context tContext, boolean tAnimate) {
        if (tAnimate) {
            tView.startAnimation(getWobble(tContext));
        } else {
            tView.clearAnimation();
        }
    }

    public static void animateBuzz(View tView, Context tContext, long tTime) {
        tView.setAnimation(getWobble(tContext, tTime));
    }
}
