package com.msm.onlinecomplaintapp.Services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.msm.onlinecomplaintapp.GlobalApplication;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            GlobalApplication.databaseHelper.updateNewRegsitrationToken(FirebaseAuth.getInstance().getCurrentUser().getUid(),s);
    }
}
