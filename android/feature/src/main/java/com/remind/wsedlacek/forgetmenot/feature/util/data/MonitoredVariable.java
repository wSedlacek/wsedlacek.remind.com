package com.remind.wsedlacek.forgetmenot.feature.util.data;

public class MonitoredVariable<Prototype> {
    private Prototype mMonitored;
    protected ChangeListener mListener;

    public MonitoredVariable(Prototype tMonitored) {
        this(tMonitored, null);
    }

    public MonitoredVariable(Prototype tMonitored, ChangeListener tListener) {
        mMonitored = tMonitored;
        if (tListener != null) setListener(tListener);
    }

    public Prototype get() {
        return mMonitored;
    }

    public void set(Prototype tMonitored) {
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
