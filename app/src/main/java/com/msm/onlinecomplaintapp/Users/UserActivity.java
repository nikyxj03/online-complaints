package com.msm.onlinecomplaintapp.Users;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.msm.onlinecomplaintapp.LoginActivities.LoginActivity;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivities.homepage;
import com.msm.onlinecomplaintapp.Users.UserActivities.mycomplaints;
import com.msm.onlinecomplaintapp.Users.UserActivities.notificationsactivity;
import com.msm.onlinecomplaintapp.Users.UserActivities.settingsactivity;
import com.msm.onlinecomplaintapp.Users.UserActivities.userarchives;

public class UserActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    protected static final int REQUEST_CODE_NEWCOMP=5;
    protected static final int REQUEST_CODE_HOMEPAGE=11;
    protected static final int REQUEST_CODE_MYCOMPLAINT=12;
    protected static final int REQUEST_CODE_SETTINGS=13;
    protected static final int REQUEST_CODE_ARCHIVES=14;
    protected static final int REQUEST_CODE_NOTIFICATION=15;
    protected static final int REQUEST_CODE_MAIN=0;
    protected static final int REQUEST_CODE_TCD=6;

    protected static int PAGE_HOME=1;
    protected static int PAGE_OPEN_COMPLAINTS=2;
    protected static int PAGE_CLOSED_COMPLAINTS=3;
    protected static int PAGE_PUBLIC_ARCHIVES=4;

    protected Intent mainintent=new Intent();
    protected Intent mycomplaintintent=new Intent();
    protected Intent settingsintent=new Intent();
    protected Intent homeintent=new Intent();
    protected Intent tcdintent=new Intent();
    protected Intent notificationintent=new Intent();
    protected Intent archiveintent=new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgress(String message) {
        hideProgress();
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().getAttributes().windowAnimations= R.style.DialogSlide;
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean checkInternetConnection() {
        boolean hasInternetConnection = hasInternetConnection();
        if (!hasInternetConnection) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"No Intent", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        return hasInternetConnection;
    }

    protected String getCurrentUserId() {
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mFirebaseUser != null) {
            return mFirebaseUser.getUid();
        }
        return null;
    }

    protected void setintents(Context context){
        homeintent=new Intent();
        mainintent=new Intent();
        settingsintent=new Intent();
        mycomplaintintent=new Intent();
        archiveintent=new Intent();
        notificationintent=new Intent();
        homeintent.setClass(context, homepage.class);
        mainintent.setClass(context, LoginActivity.class);
        settingsintent.setClass(context, settingsactivity.class);
        archiveintent.setClass(context, userarchives.class );
        mycomplaintintent.setClass(context, mycomplaints.class );
        notificationintent.setClass(context, notificationsactivity.class);
    }

    public static int getPageClosedComplaints() {
        return PAGE_CLOSED_COMPLAINTS;
    }

    public static int getPageHome() {
        return PAGE_HOME;
    }

    public static int getPageOpenComplaints() {
        return PAGE_OPEN_COMPLAINTS;
    }

    public static int getPagePublicArchives() {
        return PAGE_PUBLIC_ARCHIVES;
    }

}
