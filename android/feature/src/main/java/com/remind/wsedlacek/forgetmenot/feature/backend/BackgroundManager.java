package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.View;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.util.Shift;
import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;

import java.util.Random;

public class BackgroundManager {
    private static String TAG = "BackgroundManager";

    private static GradientDrawable mBackground;

    private static ChangeListener mListener;

    public static MonitoredVariable<Integer>[] mTop;
    public static MonitoredVariable<Integer>[] mBottom;
    private static MonitoredVariable<Boolean> mAnimate;

    private static Random mRNG = new Random();

    private static Handler mAnimationLoop;
    private static Runnable mAnimation;

    public static void init() {
        MonitoredVariable.ChangeListener tUpdateBackground = new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() { updateBackgroud();
            }
        };
        mTop = new MonitoredVariable[]{
                new MonitoredVariable<>(mRNG.nextInt(255), tUpdateBackground),
                new MonitoredVariable<>(mRNG.nextInt(255), tUpdateBackground),
                new MonitoredVariable<>(mRNG.nextInt(255), tUpdateBackground)
        };
        mBottom = new MonitoredVariable[]{
                new MonitoredVariable<>(mRNG.nextInt(255), tUpdateBackground),
                new MonitoredVariable<>(mRNG.nextInt(255), tUpdateBackground),
                new MonitoredVariable<>(mRNG.nextInt(255), tUpdateBackground)
        };

        mAnimationLoop = new Handler();
        mAnimation = new Runnable() {
            @Override
            public void run() {
                for (MonitoredVariable<Integer>[] tBackground: new MonitoredVariable[][] {mTop, mBottom}) {
                    for (MonitoredVariable<Integer> tColor : tBackground) {
                        int tRandomColor = mRNG.nextInt(255);
                        Shift.changeOverTime(tColor, tRandomColor, 5000, 80);
                    }
                }
                if(mAnimate.get()) {
                    mAnimationLoop.postDelayed(mAnimation, 5000);
                }
            }
        };

        mAnimate = new MonitoredVariable<>(false, new MonitoredVariable.ChangeListener()    {
            @Override
            public void onChange() {
                if (mAnimate.get()) { mAnimationLoop.postDelayed(mAnimation, 0); }
                else { Shift.cancelShift(); mAnimationLoop.removeCallbacks(mAnimation); }
            }
        });
    }

    public static void setBackground(final Activity tActivity){
        final View tActivityBackground = tActivity.findViewById(R.id.background);
        mListener = new ChangeListener() {
            @Override
            public void onChange() { tActivityBackground.setBackground(mBackground); }
        };

        notifyChange();
    }
    public static void updateBackground(float tPercent, boolean tPast, MonitoredVariable<Integer>[] tBackground) {
        updateBackground(tPercent, tPast, tBackground, false);
    }
    public static void updateBackground(float tPercent, boolean tPast, MonitoredVariable<Integer>[] tBackground, boolean useSlowShift) {
        int[] tRed = {183, 28, 28};
        int[] tBlue = {13, 71, 161};
        int[] tGreen = {27, 94, 32};

        for (int i = 0; i < tBackground.length; i++) {
            if (useSlowShift)
                Shift.changeOverTime(tBackground[i], Shift.calcPercDiff(tPercent, tBlue[i], tPast ? tRed[i] : tGreen[i]),
                        500, 100);
            else
                tBackground[i].set(Shift.calcPercDiff(tPercent, tBlue[i], tPast ? tRed[i] : tGreen[i]));
        }
    }
    private static void updateBackgroud() {
        int tTop = Color.argb(255, mTop[0].get(), mTop[1].get(), mTop[2].get());
        int tBottom = Color.argb(255, mBottom[0].get(), mBottom[1].get(), mBottom[2].get());
        mBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{tTop, tBottom});
        mBackground.setCornerRadius(0f);
        notifyChange();
    }
    public static void animateBackground(boolean tAnimate) {
        mAnimate.set(tAnimate);
    }

    public static void notifyChange() {
        if (mListener != null) mListener.onChange();
    }
    public interface ChangeListener {
        void onChange();
    }
}
