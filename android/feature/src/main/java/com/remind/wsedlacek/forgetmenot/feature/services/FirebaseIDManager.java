package com.remind.wsedlacek.forgetmenot.feature.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.remind.wsedlacek.forgetmenot.feature.util.Debug;

public class FirebaseIDManager extends FirebaseInstanceIdService {
    private final static String TAG = "FirebaseIDManager";
    private static String sID = "default";
    private static Runnable sAction;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Debug.Log(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sID = refreshedToken;
        if (sAction != null) sAction.run();
    }

    public static String getID() {
        return sID;
    }

    public static void setListener(Runnable tAction) {
        sAction = tAction;
    }
}
