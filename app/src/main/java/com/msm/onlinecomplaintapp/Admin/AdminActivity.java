package com.msm.onlinecomplaintapp.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Models.AdminUser;
import com.msm.onlinecomplaintapp.R;

public class AdminActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private AdminUser adminUser=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        GlobalApplication.databaseHelper.fetchAdminUserData(getCurrentUserId(), new OnDataSFetchListener<AdminUser>() {
            @Override
            public void onDataSFetch(AdminUser adminUser) {
                AdminActivity.this.adminUser=adminUser;
            }
        });
        //actionBar=getSupportActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transperant)));
        //int titleId = getResources().getIdentifier()
        //TextView abTitle = (TextView) findViewById(titleId);
        //abTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
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

    }

    protected AdminUser getAdminUser(){
        return adminUser;
    }
}

