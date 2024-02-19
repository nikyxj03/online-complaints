package com.msm.onlinecomplaintapp.LoginActivities;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.*;
import android.content.*;

import java.util.*;

import android.view.View;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseUser;
import com.msm.onlinecomplaintapp.Admin.AdminActivities.AdminHomeDefault;
import com.msm.onlinecomplaintapp.Admin.AdminActivities.admin_home;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.department_home;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.PageLockListener;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivities.homepage;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_HOMEPAGE=11;
    private static final int RC_DEPARTMENT_HOME=21;
    private static final int RC_ADMIN_HOME=31;
    private static final int REQUEST_CODE_MAIN=0;

    private Intent mainintent=new Intent();
    private Intent homeintent=new Intent();
    private Intent depthomeintent=new Intent();
    private Intent adminhomeintent=new Intent();

    private RelativeLayout splashscreenlinear;
    private LinearLayout loginpagerlayout;
    private SegmentedButtonGroup user_type_sbg;
    private ViewPager login_pager;

    private LoginPagerAdapter loginPagerAdapter;


    private int utaflag=0;
    private int usertype=-1;

    private SharedPreferences loginpreferences;
    private SharedPreferences.Editor loginprefeditor;

    private HashMap<String,Object> tempmap = new HashMap<>();
    private FirebaseDatabase _firebase=FirebaseDatabase.getInstance();

    private FirebaseAuth vmauth;
    private FirebaseUser user;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_HOMEPAGE || requestCode==RC_ADMIN_HOME || requestCode==RC_DEPARTMENT_HOME)
        {
            if(resultCode==RESULT_OK)
            {
                Log.i("abcdef","1");
                utaflag=0;
                if(data.getStringExtra("key1").equals("logout")) {
                    Log.i("abcdef","1");
                    splashscreenlinear.setVisibility(View.GONE);
                    loginpagerlayout.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().signOut();
                    loginprefeditor.putInt("type",-1).commit();
                    vmauth.signOut();
                    vmauth=FirebaseAuth.getInstance();
                    user=null;
                    setlockmode(false);
                }
            }
            else
            {
                LoginActivity.this.finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        vmauth=FirebaseAuth.getInstance();
        user=vmauth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vmauth=FirebaseAuth.getInstance();
        user=vmauth.getCurrentUser();

        splashscreenlinear=findViewById(R.id.splashscreenlinear);
        loginpagerlayout=findViewById(R.id.loginpagerlayout);
        user_type_sbg=findViewById(R.id.user_type);
        login_pager=findViewById(R.id.login_pager);

        splashscreenlinear.setVisibility(View.VISIBLE);
        loginpagerlayout.setVisibility(View.GONE);

        loginpreferences=getSharedPreferences("UserPreferences",MODE_PRIVATE);
        loginprefeditor=loginpreferences.edit();
        usertype=loginpreferences.getInt("type",-1);

        loginPagerAdapter=new LoginPagerAdapter(getSupportFragmentManager(), LoginActivity.this, new PageLockListener() {
            @Override
            public void onStateChanged( final boolean mode) {
                setlockmode(mode);
            }
        });
        login_pager.setAdapter(loginPagerAdapter);

        user_type_sbg.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                login_pager.setCurrentItem(position);
            }
        });


        login_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                user_type_sbg.setPosition(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!hasInternetConnection()){
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("No Internet Connection");
                    alertDialog.setMessage("This app requires internet to run. Try again after connecting to internet");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Try Again",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(LoginActivity.this,LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    if(checkUserLoggedIn(user)){
                        if(usertype==0){
                            GlobalApplication.databaseHelper.updateRegistrationToken(user.getUid());
                            homeintent.setClass(LoginActivity.this,homepage.class);
                            startActivityForResult(homeintent,REQUEST_CODE_HOMEPAGE);
                        }
                        else if(usertype==1){
                            depthomeintent.setClass(LoginActivity.this,department_home.class);
                            startActivityForResult(depthomeintent,REQUEST_CODE_HOMEPAGE);
                        }
                        else if(usertype==2){
                            adminhomeintent.setClass(LoginActivity.this, AdminHomeDefault.class);
                            startActivityForResult(adminhomeintent,REQUEST_CODE_HOMEPAGE);
                        }
                    }
                    else {
                        splashscreenlinear.setVisibility(View.GONE);
                        loginpagerlayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        },500);

    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void setlockmode(final boolean mode){
        login_pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mode;
            }
        });
        if(mode){
            user_type_sbg.setVisibility(View.GONE);
        }
        else {
            user_type_sbg.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkUserLoggedIn(FirebaseUser user){
        if(user!=null && loginpreferences.getInt("type",-1)!=-1){
            return true;
        }
        else {
            return false;
        }
    }
}