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

    public MonitoredVariable mData;
    private Runnable mAction;

    public FirebaseVariable(final String tDataName) {
        this(tDataName, null);
    }

    public FirebaseVariable(final String tDataName, Runnable tAction) {
        Log.d(TAG,  tDataName + " - Initializing Local Variable...");
        mData = new MonitoredVariable("");
        mAction = tAction;

        Log.d(TAG,  tDataName + " - Fetching Database...");
        final DatabaseReference tRef = mDatabase.getReference(tDataName);

        Log.d(TAG, tDataName + " - Adding Database Listener...");
        tRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tData = dataSnapshot.getValue(String.class);
                tData = tData == null ? "" : tData; //Correct for NULL
                Log.d(TAG,  "Database Variable [" + tDataName + "] was changed to " + tData);
                mData.set(tData);
                if (mAction != null) mAction.run();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, tDataName + " - Failed to read value.", error.toException());
            }
        });

        Log.d(TAG,  tDataName + " - Adding Local Variable Listener..." );
        mData.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                String tData = (String)mData.get();
                Log.d(TAG,  "Local Variable [" + tDataName + "] was changed to " + tData);
                mDatabase.getReference(tDataName).setValue(tData);
            }
        });
    }

    public void setListener(Runnable tAction) {
        mAction = tAction;
    }

    public void set(String tData) {
        mData.set(tData);
    }

    public String get() {
        return (String)mData.get();
    }
}
