package com.remind.wsedlacek.forgetmenot.feature.services.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

public class FirebaseIDManager extends FirebaseInstanceIdService {
    private final static String TAG = "FirebaseIDManager";
    private static String sID = "default";
    private static ChangeListener sListener;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Debug.Log(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sID = refreshedToken;
        if (sListener != null) sListener.onChange();
    }

    public static String getID() {
        return sID;
    }

    public static void setListener(ChangeListener tListener) {
        sListener = tListener;
    }
    public interface ChangeListener {
        void onChange();
    }
}
