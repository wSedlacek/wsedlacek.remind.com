package com.remind.wsedlacek.forgetmenot.feature.backend;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.remind.wsedlacek.forgetmenot.feature.activities.Buzz;
import com.remind.wsedlacek.forgetmenot.feature.activities.Setup;
import com.remind.wsedlacek.forgetmenot.feature.services.FirebaseIDManager;
import com.remind.wsedlacek.forgetmenot.feature.util.Debug;
import com.remind.wsedlacek.forgetmenot.feature.util.FirebaseContainer;
import com.remind.wsedlacek.forgetmenot.feature.util.MonitoredVariable;

import java.util.Map;

public class DataManager {
    private static String TAG = "DataManager";
    private static String sID;

    private static FirebaseContainer sFirebaseContainer;

    public static MonitoredVariable sToken;
    public static MonitoredVariable sNameData;
    public static MonitoredVariable sTimeData;
    public static MonitoredVariable sFreqData;
    public static MonitoredVariable sDateData;
    public static MonitoredVariable sConnectedData;

    public static void init(final Context tContext) {
        Debug.Log(TAG, "Initializing Variables...");
        sID = Settings.Secure.getString(tContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        sNameData = new MonitoredVariable(null, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sFirebaseContainer.set("name", sNameData.get());
            }
        });

        sTimeData = new MonitoredVariable(null, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sFirebaseContainer.set("time", sTimeData.get());
            }
        });

        sFreqData = new MonitoredVariable(null, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sFirebaseContainer.set("freq", sFreqData.get());
            }
        });

        sDateData = new MonitoredVariable(null, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sFirebaseContainer.set("date", sDateData.get());
            }
        });

        sConnectedData = new MonitoredVariable(null, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sFirebaseContainer.set("connected", sConnectedData.get());

                Intent tIntent;
                switch ((String) sConnectedData.get()) {
                    case "1":
                        tIntent = new Intent(tContext, Buzz.class);
                        break;
                    default:
                        tIntent = new Intent(tContext, Setup.class);
                }

                tContext.startActivity(tIntent);
            }
        });

        sToken = new MonitoredVariable(null, new MonitoredVariable.ChangeListener() {
            @Override
            public void onChange() {
                sFirebaseContainer.set("token", sToken.get());
            }
        });

        FirebaseIDManager.setListener(new Runnable() {
            @Override
            public void run() {
                FirebaseContainer.add("IDs", FirebaseIDManager.getID());
                sToken.set(FirebaseIDManager.getID());
            }
        });

        sFirebaseContainer = new FirebaseContainer(sID, new FirebaseContainer.ChangeListener() {
            @Override
            public void onChange(String tKey) {
                Map<String, Object> tContainer = sFirebaseContainer.get();
                switch (tKey) {
                    case "name": sNameData.set(tContainer.get(tKey)); break;
                    case "time": sTimeData.set(tContainer.get(tKey)); break;
                    case "freq": sFreqData.set(tContainer.get(tKey)); break;
                    case "date": sDateData.set(tContainer.get(tKey)); break;
                    case "connected": sConnectedData.set(tContainer.get(tKey)); break;
                    case "token": sToken.set(tContainer.get(tKey)); break;
                }
            }
        });
    }
}
