package com.msm.onlinecomplaintapp.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.msm.onlinecomplaintapp.LoginActivities.LoginActivity;
import com.msm.onlinecomplaintapp.R;

public class notificationsactivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NEWCOMP=5;
    private static final int REQUEST_CODE_HOMEPAGE=11;
    private static final int REQUEST_CODE_MYCOMPLAINT=12;
    private static final int REQUEST_CODE_SETTINGS=13;
    private static final int REQUEST_CODE_ARCHIVES=14;
    private static final int REQUEST_CODE_NOTIFICATION=15;
    private static final int REQUEST_CODE_MAIN=0;
    private static final int REQUEST_CODE_TCD=6;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Intent mainintent=new Intent();
    private Intent newcompintent=new Intent();
    private Intent mycomplaintintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent homeintent=new Intent();
    private Intent tcdintent=new Intent();
    private Intent notificationintent=new Intent();
    private Intent archiveintent=new Intent();

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifictationsactivity);
        Initialize();
        InitializeLogic();
    }

    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(notificationsactivity.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        archivebutton=findViewById(R.id.archivebutton);
        notificationbutton=findViewById(R.id.notification_button);
        logoutbutton=findViewById(R.id.logoutbutton);

        mainintent.setClass(this, LoginActivity.class);
        newcompintent.setClass(this,newcomplaint.class);
        settingsintent.setClass(this,settingsactivity.class);
        homeintent.setClass(this,homepage.class);
        mycomplaintintent.setClass(this,mycomplaints.class );
        notificationintent.setClass(this, notificationsactivity.class );
        archiveintent.setClass(this,userarchives.class );

        mycomplaintsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mycomplaintintent,REQUEST_CODE_MYCOMPLAINT);
            }
        });

        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS);
            }
        });

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(homeintent,REQUEST_CODE_HOMEPAGE);
            }
        });

        archivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES);
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_SETTINGS) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MYCOMPLAINT) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,mycomplaintintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_ARCHIVES){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent );
                        }
                        else{
                            if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_NOTIFICATION){
                                notificationintent.putExtra("ac","lo" );
                                setResult(RESULT_OK,notificationintent );
                            }
                            else{
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }
                        }
                    }
                }
                notificationsactivity.this.finish();
            }
        });
    }

    private void InitializeLogic(){

    }
}
