package com.remind.wsedlacek.forgetmenot.feature;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataManager {
    private static String TAG = "DataManager";
    private static Context sContext;
    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private static String sID;

    private static String sConnectedName;
    private static String sEventName;
    private static String sTimeName;
    private static String sFreqName;

    private static String sConnectedData = "";
    private static String sEventData = "";
    private static String sTimeData = "";
    private static String sFreqData = "";

    public enum Data { ID, CONNECTED, EVENT, TIME, FREQ, NULL; }

    public static void init(final Context tContext) {
        sContext = tContext;

        sID = sContext.getResources().getString(R.string.default_id);
        sConnectedName = sContext.getResources().getString(R.string.connected_suffix);
        sEventName = sContext.getResources().getString(R.string.event_suffix);
        sTimeName = sContext.getResources().getString(R.string.time_suffix);
        sFreqName = sContext.getResources().getString(R.string.freq_suffix);

        fetchID();

        connectFirebase(Data.EVENT, null);
        connectFirebase(Data.TIME, null);
        connectFirebase(Data.FREQ, null);
        connectFirebase(Data.CONNECTED, new Runnable() {
            @Override
            public void run() {
                Intent tIntent;
                switch (sConnectedData) {
                    case "1" : tIntent = new Intent(sContext, Buzz.class); break;
                    default: tIntent = new Intent(sContext, Setup.class);
                }

                sContext.startActivity(tIntent);
            }
        });
    }

    private static int fetchID() {
        if (sID.equals("default")) {
            sID = Settings.Secure.getString(sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            sConnectedName = sID + sConnectedName;
            sEventName = sID + sEventName;
            sTimeName = sID + sTimeName;
            sFreqName = sID + sFreqName;
            return 0;
        } else {
            return -1;
        }
    }

    private static void connectFirebase(Data tDataType, Runnable tAction) {
        final String tFirebaseName = getDataName(tDataType);
        final Runnable tFirebaseAction = tAction;

        final DatabaseReference tRef = mDatabase.getReference(tFirebaseName);
        tRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tData = dataSnapshot.getValue(String.class);
                tData = tData == null ? "" : tData; //Correct for NULL
                setLocalData(getDataType(tFirebaseName), tData);
                if (tFirebaseAction != null) tFirebaseAction.run();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private static int setLocalData(Data tDataType, String tData) {
        switch (tDataType) {
            case CONNECTED: sConnectedData = tData; return 0;
            case EVENT: sEventData = tData; return 0;
            case TIME: sTimeData = tData; return 0;
            case FREQ: sFreqData = tData; return 0;
            default: return -1;
        }
    }

    public static void setData(Data tDataType, String tData) {
        mDatabase.getReference(DataManager.getDataName(tDataType)).setValue(tData);
    }

    public static String getDataName(Data tDataType) {
        switch (tDataType) {
            case ID: return sID;
            case CONNECTED: return sConnectedName;
            case EVENT: return sEventName;
            case TIME: return sTimeName;
            case FREQ: return sFreqName;
            default: return "";
        }
    }

    private static Data getDataType(String tDataName) {
        String tNoID = removeID(tDataName);
        if (tNoID.equals(removeID(sID))) return Data.ID;
        if (tNoID.equals(removeID(sConnectedName))) return Data.CONNECTED;
        if (tNoID.equals(removeID(sEventName))) return Data.EVENT;
        if (tNoID.equals(removeID(sTimeName))) return Data.TIME;
        if (tNoID.equals(removeID(sFreqName))) return Data.FREQ;
        return Data.NULL;
    }

    public static String getData(Data tDataType) {
        switch (tDataType) {
            case ID: return sID;
            case CONNECTED: return sConnectedData;
            case EVENT: return sEventData;
            case TIME: return sTimeData;
            case FREQ: return sFreqData;
            default: return "";
        }
    }

    private static String removeID(String tDataName) {
        String tNoID = tDataName.replace(sID, "");
        return tNoID.equals("") ? "default" : tNoID;
    }
}
