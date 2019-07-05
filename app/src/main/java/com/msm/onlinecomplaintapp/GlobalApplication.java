package com.msm.onlinecomplaintapp;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.FirebaseApp;
import com.msm.onlinecomplaintapp.Common.CloudFunctionHelper;
import com.msm.onlinecomplaintapp.Common.DatabaseHelper;

public class GlobalApplication extends Application {

    public static DatabaseHelper databaseHelper;
    public static CloudFunctionHelper cloudFunctionHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        databaseHelper = DatabaseHelper.getInstance(this);
        databaseHelper.init();
        cloudFunctionHelper=CloudFunctionHelper.getInstance(this);
        cloudFunctionHelper.init();


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelName = "Complaints";
            String channelDescription = "Notifications on complaints status";
            String CHANEL_ID = "CS";

            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
