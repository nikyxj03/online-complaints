package com.msm.onlinecomplaintapp.AdminActivities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.msm.onlinecomplaintapp.LoginActivities.LoginActivity;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.DepartmentActivities.depttotcompdec;

public class admincreateaccount extends AppCompatActivity {

    private static final int REQUEST_CODE_HOMEPAGE_A=31;
    private static final int REQUEST_CODE_SETTINGS_A=34;
    private static final int REQUEST_CODE_MAIN_A=0;
    private static final int REQUEST_CODE_TCD_A=7;
    private static final int REQUEST_CODE_REQPAGE_A=32;
    private static final int REQUEST_CODE_CREATEAC_A=33;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Intent adminhomeintent=new Intent();
    private Intent requestintent=new Intent();
    private Intent createacintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent mainintent=new Intent();
    private Intent tcdintent =new Intent();

    private Button homebutton2;
    private Button logoutbutton2;
    private Button settingsbutton2;
    private Button myrequestbutton;
    private Button createaccountbutton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(true) {
            if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_A) {
                adminhomeintent.putExtra("ac", "lo");
                setResult(RESULT_OK,adminhomeintent);
            } else {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_A) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                }
                else{
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_REQPAGE_A){
                        requestintent.putExtra("ac","lo");
                        setResult(RESULT_OK, requestintent);
                    }else {
                        if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                        {
                            mainintent.putExtra("key1","logout");
                            setResult(RESULT_OK,mainintent);
                        }
                    }

                }
            }
            admincreateaccount.this.finish();
        }
    }


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
        setContentView(R.layout.activity_admincreateaccount);
        Initialize();
        InitializeLogic();
    }
    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(admincreateaccount.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        homebutton2=findViewById(R.id.homebutton2);
        myrequestbutton=findViewById(R.id.myrequestsbutton);
        createaccountbutton=findViewById(R.id.createaccountbutton);
        settingsbutton2=findViewById(R.id.settingsbutton2);
        logoutbutton2=findViewById(R.id.logoutbutton2);

        mainintent.setClass(admincreateaccount.this, LoginActivity.class);
        settingsintent.setClass(admincreateaccount.this,adminsettings.class);
        adminhomeintent.setClass(admincreateaccount.this,admin_home.class);
        tcdintent.setClass(admincreateaccount.this,depttotcompdec.class);
        requestintent.setClass(admincreateaccount.this, adminrequests.class);
        createacintent.setClass(admincreateaccount.this,admincreateaccount.class );

        settingsbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_A);
            }
        });

        homebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(adminhomeintent,REQUEST_CODE_HOMEPAGE_A);
            }
        });

        myrequestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(requestintent,REQUEST_CODE_REQPAGE_A);
            }
        });

        logoutbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_A) {
                    adminhomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,adminhomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_A) {
                        settingsintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,settingsintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_REQPAGE_A){
                            requestintent.putExtra("ac","lo");
                            setResult(RESULT_OK, requestintent);
                        }else {
                            if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                            {
                                mainintent.putExtra("key1","logout");
                                setResult(RESULT_OK,mainintent);
                            }
                        }

                    }
                }
                admincreateaccount.this.finish();
            }
        });
    }
    private void InitializeLogic(){

    }
}
