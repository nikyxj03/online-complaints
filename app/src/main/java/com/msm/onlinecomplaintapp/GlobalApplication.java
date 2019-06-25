package com.msm.onlinecomplaintapp;


import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.msm.onlinecomplaintapp.Common.DatabaseHelper;

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
