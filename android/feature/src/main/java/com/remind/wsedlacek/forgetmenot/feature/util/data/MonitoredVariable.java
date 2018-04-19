package com.remind.wsedlacek.forgetmenot.feature.util.data;

public class MonitoredVariable<Prototype> {
    protected Prototype mData;
    protected ChangeListener mListener;

    public MonitoredVariable(Prototype tData) {
        this(tData, null);
    }

    public MonitoredVariable(Prototype tData, ChangeListener tListener) {
        if (tListener != null) setListener(tListener);
        mData = tData;
    }

    public Prototype get() {
        return mData;
    }
    public void set(Prototype tData) {
        if (mData != tData) {
            mData = tData;
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
