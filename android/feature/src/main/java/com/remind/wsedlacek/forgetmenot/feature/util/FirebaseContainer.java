package com.remind.wsedlacek.forgetmenot.feature.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseContainer {
    private String TAG = "FirebaseContainer";
    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public FirebaseContainer(String tName, String tData) {
        Debug.Log(TAG,  tName + " - Fetching Database... " + tName);
        final DatabaseReference tRef = mDatabase.getReference(tName);

        Map<String, Object> tDatabase = new HashMap<String, Object>();
        tDatabase.put(tData == null ? "" : tData, true);
        tRef.updateChildren(tDatabase);
    }
}
