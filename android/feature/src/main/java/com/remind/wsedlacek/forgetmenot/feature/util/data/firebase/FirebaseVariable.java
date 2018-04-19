package com.remind.wsedlacek.forgetmenot.feature.util.data.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.data.MonitoredVariable;

public class FirebaseVariable<Prototype> extends MonitoredVariable {
    private String TAG = "FirebaseVariable";
    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference mDataRef;

    public FirebaseVariable(final String tDataName) {
        this(tDataName, null);
    }

    public FirebaseVariable(final String tDataName, ChangeListener tListener) {
        super(null);
        mListener = tListener;

        Debug.Log(TAG,  tDataName + " - Fetching Database...");
        mDataRef = mDatabase.getReference(tDataName);

        Debug.Log(TAG, tDataName + " - Adding Database Listener...");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tSnapshot) {
                Prototype tData = (Prototype) tSnapshot.getValue();
                set(tData);
            }

            @Override
            public void onCancelled(DatabaseError tError) {
                Log.e(TAG, tDataName + " - Failed to read value.", tError.toException());
            }
        });

        Debug.Log(TAG,  tDataName + " - Adding Local Variable Listener..." );
        setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                mDatabase.getReference(tDataName).setValue(mData);
                notifyChange();
            }
        });
    }
}
