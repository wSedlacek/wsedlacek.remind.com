package com.remind.wsedlacek.forgetmenot.feature.util;

public class MonitoredVariable {
    private Object mMonitored;
    private ChangeListener mListener;

    public MonitoredVariable(Object tMonitored) {
        mMonitored = tMonitored;
    }

    public Object get() {
        return mMonitored;
    }

    public void set(Object tMonitored) {
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
