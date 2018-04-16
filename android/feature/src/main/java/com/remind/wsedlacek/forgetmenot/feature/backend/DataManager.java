package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.remind.wsedlacek.forgetmenot.feature.activities.Buzz;
import com.remind.wsedlacek.forgetmenot.feature.R;
import com.remind.wsedlacek.forgetmenot.feature.activities.Setup;
import com.remind.wsedlacek.forgetmenot.feature.util.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.FirebaseVariable;


public class DataManager {
    private static String TAG = "DataManager";

    private static String sID;
    private static String[] sNames;

    public static FirebaseVariable sNameData;
    public static FirebaseVariable sTimeData;
    public static FirebaseVariable sFreqData;
    public static FirebaseVariable sDateData;
    public static FirebaseVariable sConnectedData;

    public static void init(final Context tContext) {
        Debug.Log(TAG, "Initializing Variables...");
        sID = tContext.getResources().getString(R.string.default_id);
        sNames = new String[]{ tContext.getResources().getString(R.string.name_prefix), tContext.getResources().getString(R.string.time_prefix),
                               tContext.getResources().getString(R.string.freq_prefix), tContext.getResources().getString(R.string.date_prefix),
                               tContext.getResources().getString(R.string.connected_prefix)};

        fetchID(tContext);

        sNameData = new FirebaseVariable(sNames[0]);
        sTimeData = new FirebaseVariable(sNames[1]);
        sFreqData = new FirebaseVariable(sNames[2]);
        sDateData = new FirebaseVariable(sNames[3]);
        sConnectedData = new FirebaseVariable(sNames[4], new Runnable() {
            @Override
            public void run() {
                Intent tIntent;
                switch (sConnectedData.get()) {
                    case "1":
                        tIntent = new Intent(tContext, Buzz.class);
                        break;
                    default:
                        tIntent = new Intent(tContext, Setup.class);
                }

                tContext.startActivity(tIntent);
            }
        });
    }

    private static void fetchID(final Context tContext) {
        if (sID.equals("default")) {
            Debug.Log(TAG, "Fetching device UID...");
            sID = Settings.Secure.getString(tContext.getContentResolver(), Settings.Secure.ANDROID_ID);

            Debug.Log(TAG, "Apending variables with UID [" + sID + "]...");
            for(int i = 0; i < sNames.length; i++) {
                sNames[i] += sID;
            }
        }
    }
}
