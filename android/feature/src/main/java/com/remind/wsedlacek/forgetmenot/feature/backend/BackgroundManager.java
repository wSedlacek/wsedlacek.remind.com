package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.remind.wsedlacek.forgetmenot.feature.R;

public class BackgroundManager {
    public static GradientDrawable mBackground;
    private static ChangeListener mListener;

    public static void init() {
        int[] colors = {Color.parseColor("#008000"), Color.parseColor("#ADFF2F")};
        mBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        mBackground.setCornerRadius(0f);
    }

    public static void setBackground(final Activity tActivity){
        mListener = new ChangeListener() {
            @Override
            public void onChange() {
                tActivity.findViewById(R.id.background).setBackground(mBackground);
            }
        };

        notifyChange();
    }
    public static void updateBackground(int tTime1, int tTime2) {
        notifyChange();
    }

    public static void notifyChange() {
        if (mListener != null) mListener.onChange();
    }
    public interface ChangeListener {
        void onChange();
    }
}
