package com.msm.onlinecomplaintapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.msm.onlinecomplaintapp.DepartmentActivities.DeptArchives;
import com.msm.onlinecomplaintapp.DepartmentActivities.DeptPublicArchives;
import com.msm.onlinecomplaintapp.DepartmentActivities.department_home;
import com.msm.onlinecomplaintapp.DepartmentActivities.deptcomplaints;
import com.msm.onlinecomplaintapp.DepartmentActivities.deptsettings;
import com.msm.onlinecomplaintapp.DepartmentActivities.depttotcompdec;
import com.msm.onlinecomplaintapp.Interfaces.OnSortChange;

public class DepartmentActivity extends AppCompatActivity  {

    private ProgressDialog progressDialog;
    private ActionBar actionBar;

    protected Intent mainintent = new Intent();
    protected Intent depthomeintent=new Intent();
    protected Intent deptcomplaintintent = new Intent();
    protected Intent settingsintent = new Intent();
    protected Intent tcdintent = new Intent();
    protected Intent archiveintent = new Intent();
    protected Intent deptarchiveintent=new Intent();

    protected static int PAGE_HOME_D=1;
    protected static int PAGE_REGISTERED_COMPLAINTS_D=2;
    protected static int PAGE_WATCHING_COMPLAINTS_D=3;
    protected static int PAGE_PUBLIC_ARCHIVES_D=4;
    protected static int PAGE_RESOLVED_COMPLAINTS=5;
    protected static int PAGE_IGNORED_COMPLAINTS=6;

    protected static final int REQUEST_CODE_HOMEPAGE_D = 21;
    protected static final int REQUEST_CODE_DeptCOMPLAINT_D = 22;
    protected static final int REQUEST_CODE_SETTINGS_D = 23;
    protected static final int REQUEST_CODE_MAIN_D = 0;
    protected static final int REQUEST_CODE_ARCHIVES_D = 24;
    protected static final int REQUEST_CODE_DEPTARCHIVES_D=25;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //actionBar=getSupportActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transperant)));
        //int titleId = getResources().getIdentifier()
        //TextView abTitle = (TextView) findViewById(titleId);
        //abTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void showProgress(String message) {
        hideProgress();
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().getAttributes().windowAnimations=R.style.DialogSlide;
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
        mainintent=new Intent();
        settingsintent=new Intent();
        depthomeintent=new Intent();
        tcdintent=new Intent();
        archiveintent=new Intent();
        deptcomplaintintent=new Intent();
        deptarchiveintent=new Intent();
        mainintent.setClass(context,MainActivity.class);
        settingsintent.setClass(context, deptsettings.class);
        depthomeintent.setClass(context, department_home.class);
        tcdintent.setClass(context, depttotcompdec.class);
        archiveintent.setClass(context, DeptPublicArchives.class );
        deptcomplaintintent.setClass(context, deptcomplaints.class );
        deptarchiveintent.setClass(context, DeptArchives.class);
    }

    public int getPageHomeD() {
        return PAGE_HOME_D;
    }

    public int getPagePublicArchivesD() {
        return PAGE_PUBLIC_ARCHIVES_D;
    }

    public int getPageRegisteredComplaintsD() {
        return PAGE_REGISTERED_COMPLAINTS_D;
    }

    public int getPageResolvedComplaints() {
        return PAGE_RESOLVED_COMPLAINTS;
    }

    public int getPageWatchingComplaintsD() {
        return PAGE_WATCHING_COMPLAINTS_D;
    }

    public int getPageIgnoredComplaints() {
        return PAGE_IGNORED_COMPLAINTS;
    }



}

