package com.msm.onlinecomplaintapp;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.FirebaseApp;

public class GlobalApplication extends Application {

    public static DatabaseHelper databaseHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        databaseHelper = DatabaseHelper.getInstance(this);
        databaseHelper.init();


    }
}
