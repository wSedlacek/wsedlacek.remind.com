package com.remind.wsedlacek.forgetmenot.feature.util;

public class MonitoredVariable {
    private Object mMonitored;
    private ChangeListener mListener;

    public MonitoredVariable(Object tMonitored) {
        this(tMonitored, null);
    }

    public MonitoredVariable(Object tMonitored, ChangeListener tListener) {
        mMonitored = tMonitored;
        if (tListener != null) setListener(tListener);
    }

    public Object get() {
        return mMonitored;
    }

    public void set(Object tMonitored) {
        if (mMonitored != tMonitored) {
            mMonitored = tMonitored;
            notifyChange();
        }
    }

    public void setListener(ChangeListener tListener) {
        mListener = tListener;
    }
    public ChangeListener getListener() {
        return mListener;
    }
    public void notifyChange() {
        if (mListener != null) mListener.onChange();
    }
    public interface ChangeListener {
        void onChange();
    }
}
