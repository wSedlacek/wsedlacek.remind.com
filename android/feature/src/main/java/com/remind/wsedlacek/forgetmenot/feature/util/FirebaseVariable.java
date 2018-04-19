package com.remind.wsedlacek.forgetmenot.feature.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseVariable {
    private String TAG = "FirebaseVariable";
    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference mDataRef;
    public MonitoredVariable mData;
    private ChangeListener mListener;

    public FirebaseVariable(final String tDataName) {
        this(tDataName, null);
    }

    public FirebaseVariable(final String tDataName, ChangeListener tListener) {
        Debug.Log(TAG,  tDataName + " - Initializing Local Variable...");
        mData = new MonitoredVariable(null);
        mListener = tListener;

        Debug.Log(TAG,  tDataName + " - Fetching Database...");
        mDataRef = mDatabase.getReference(tDataName);

        Debug.Log(TAG, tDataName + " - Adding Database Listener...");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object tData = dataSnapshot.getValue();
                mData.set(tData);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, tDataName + " - Failed to read value.", error.toException());
            }
        });

        Debug.Log(TAG,  tDataName + " - Adding Local Variable Listener..." );
        mData.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                Object tData = mData.get();
                mDatabase.getReference(tDataName).setValue(tData);
                notifyChange();
            }
        });
    }

    public void set(Object tData) {
        mData.set(tData);
    }
    public Object get() {
        return mData.get();
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
