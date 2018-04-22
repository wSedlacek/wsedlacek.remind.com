package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.util.Shift;
import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

import java.sql.Array;

public class BackgroundManager {
    private static String TAG = "BackgroundManager";

    public static GradientDrawable mBackground;
    private static View mActivityBackground;

    private static ChangeListener mListener;

    public static MonitoredVariable<Integer>[] mTop;
    public static MonitoredVariable<Integer>[] mBottom;

    public static void init() {
        int[] colors = {Color.parseColor("#008000"), Color.parseColor("#ADFF2F")};
        mBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        mBackground.setCornerRadius(0f);

        MonitoredVariable.ChangeListener tUpdateBackground = new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                Debug.Log(TAG, "UPDATING BACKGROUND");
                updateBackgroud();
            }
        };
        mTop = new MonitoredVariable[]{
                new MonitoredVariable<>(0, tUpdateBackground),
                new MonitoredVariable<>(0, tUpdateBackground),
                new MonitoredVariable<>(0, tUpdateBackground)
        };
        mBottom = new MonitoredVariable[]{
                new MonitoredVariable<>(0, tUpdateBackground),
                new MonitoredVariable<>(0, tUpdateBackground),
                new MonitoredVariable<>(0, tUpdateBackground)
        };
    }

    public static void setBackground(final Activity tActivity){
        mActivityBackground = tActivity.findViewById(R.id.background);
        mListener = new ChangeListener() {
            @Override
            public void onChange() {
                //TODO: !! FIX FREEZING !!
                mActivityBackground.setBackground(mBackground);
            }
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
        String[] tTAG = {"RED", "BLUE", "GREEN"};

        for (int i = 0; i < tBackground.length; i++) {
            if (useSlowShift)
                Shift.changeOverTime(tBackground[i].get(), Shift.calcPercDiff(tPercent, tBlue[i], tPast ? tRed[i] : tGreen[i]),
                        500, 100, tBackground[i], tTAG[i]);
            else
                tBackground[i].set(Shift.calcPercDiff(tPercent, tBlue[i], tPast ? tRed[i] : tGreen[i]));
        }
    }

    private static void updateBackgroud() {
        int tTop = Color.argb(255, mTop[0].get(), mTop[1].get(), mTop[2].get());
        int tBottom = Color.argb(255, mBottom[0].get(), mBottom[1].get(), mBottom[2].get());
        int[] colors = {tTop, tBottom};
        mBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        mBackground.setCornerRadius(0f);
        notifyChange();
    }

    public static void notifyChange() {
        if (mListener != null) mListener.onChange();
    }
    public interface ChangeListener {
        void onChange();
    }
}
