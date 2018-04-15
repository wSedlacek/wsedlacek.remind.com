package com.remind.wsedlacek.forgetmenot.feature;

class MonitoredInteger {
    private int mMonitored;
    private ChangeListener mListener;

    public int get() {
        return mMonitored;
    }

    public void set(int tMonitored) {
        mMonitored = tMonitored;
        if (mListener != null) mListener.onChange();
    }

    public void setListener(ChangeListener tListener) {
        mListener = tListener;
    }

    public ChangeListener getListener() {
        return mListener;
    }


    public interface ChangeListener {
        void onChange();
    }
}
