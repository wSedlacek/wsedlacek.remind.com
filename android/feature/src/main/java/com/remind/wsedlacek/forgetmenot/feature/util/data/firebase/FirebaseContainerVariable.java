package com.remind.wsedlacek.forgetmenot.feature.util.data.firebase;

import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;

public class FirebaseContainerVariable<Prototype> extends MonitoredVariable {
    private String mKey;
    private FirebaseContainer mContainer;

    public FirebaseContainerVariable(String tKey, FirebaseContainer tContainer) {
        this(tKey, tContainer, null);
    }

    public FirebaseContainerVariable(String tKey, FirebaseContainer tContainer, MonitoredVariable.ChangeListener sListener) {
        super(null, sListener);
        mKey = tKey;
        mContainer = tContainer;
        mContainer.register(tKey, this);
    }

    @Override
    public void notifyChange() {
        mContainer.set(mKey, this.get());
        if (mListener != null) mListener.onChange();
    }
}
