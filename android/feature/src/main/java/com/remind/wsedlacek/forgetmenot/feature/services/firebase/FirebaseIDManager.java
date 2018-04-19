package com.remind.wsedlacek.forgetmenot.feature.services.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIDManager extends FirebaseInstanceIdService {
    private final static String TAG = "FirebaseIDManager";
    private static String sID;
    private static ChangeListener sListener;

    @Override
    public void onTokenRefresh() {
        sID = FirebaseInstanceId.getInstance().getToken();
        notifyChange();
    }

    public static String getID() {
        return sID;
    }

    public static void notifyChange() {
        if (sListener != null) sListener.onChange();
    }
    public static void setListener(ChangeListener tListener) {
        sListener = tListener;
    }
    public interface ChangeListener {
        void onChange();
    }
}
