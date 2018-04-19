package com.remind.wsedlacek.forgetmenot.feature.util;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseContainer {
    private static String TAG = "FirebaseContainer";
    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference mDataRef;
    private MonitoredVariable mContainer;
    private ChangeListener mListener;

    public FirebaseContainer(final String tDataName) {
        this(tDataName, null);
    }

    public FirebaseContainer(final String tDataName, ChangeListener tListener) {
        mContainer = new MonitoredVariable(new HashMap<String, Object>());
        mListener = tListener;

        Debug.Log(TAG,  tDataName + " - Fetching Database...");
        mDataRef = mDatabase.getReference(tDataName);

        Debug.Log(TAG, tDataName + " - Adding Database Listener...");
        mDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot tSnapshot, String s) {
                get().put(tSnapshot.getKey(), tSnapshot.getValue());
                notifyChange(tSnapshot.getKey());
                Debug.Log(TAG, "Updated [" + tSnapshot.getKey() + "] to " + tSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot tSnapshot, String s) {
                get().put(tSnapshot.getKey(), tSnapshot.getValue());
                notifyChange(tSnapshot.getKey());
                Debug.Log(TAG, "Updated [" + tSnapshot.getKey() + "] to " + tSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        Debug.Log(TAG,  tDataName + " - Adding Local Variable Listener..." );
        mContainer.setListener(new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                mDataRef.updateChildren(get());
            }
        });
    }

    public static void add(String tName, String tData) {
        Debug.Log(TAG,  tName + " - Fetching Database... " + tName);
        final DatabaseReference tRef = mDatabase.getReference(tName);

        Map<String, Object> tContainer = new HashMap<String, Object>();
        tContainer.put(tData == null ? "" : tData, true);
        tRef.updateChildren(tContainer);
    }

    public Map<String, Object> get() {
        return (Map<String, Object>) mContainer.get();
    }

    public void set(Map<String, Object> tContainer) {
        mContainer.set(tContainer);
    }
    public void set(String tKey, Object tData) {
        get().put(tKey, tData);
        mContainer.notifyChange();
    }

    public void setListener(ChangeListener tListener) {
        mListener = tListener;
    }
    public ChangeListener getListener() {
        return mListener;
    }
    public void notifyChange(String tKey) {
        if (mListener != null) mListener.onChange(tKey);
    }
    public interface ChangeListener {
        void onChange(String tKey);
    }
}
