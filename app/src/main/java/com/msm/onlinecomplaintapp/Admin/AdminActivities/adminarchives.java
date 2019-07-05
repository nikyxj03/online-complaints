package com.msm.onlinecomplaintapp.Admin.AdminActivities;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.depttotcompdec;
import com.msm.onlinecomplaintapp.R;

import java.util.Calendar;

public class adminarchives extends AppCompatActivity {

    private static final int REQUEST_CODE_HOMEPAGE_A=31;
    private static final int REQUEST_CODE_SETTINGS_A=34;
    private static final int REQUEST_CODE_MAIN_A=0;
    private static final int REQUEST_CODE_REQPAGE_A=32;
    private static final int REQUEST_CODE_CREATEAC_A=33;
    private static final int REQUEST_CODE_ARCHIVES_A=35;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private PopupMenu popup;

    private Calendar calendar=Calendar.getInstance();

    private Intent adminhomeintent=new Intent();
    private Intent requestintent=new Intent();
    private Intent createacintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent mainintent=new Intent();
    private Intent tcdintent =new Intent();
    private Intent archiveintent=new Intent();

    private Button homebutton2;
    private Button logoutbutton2;
    private Button settingsbutton2;
    private Button myrequestbutton;
    private Button createaccountbutton;
    private Button archivebutton2;
    private Button aasortbutton;

    private FirebaseAuth vmauth;

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
        if(item.getItemId()==R.id.csortbutton){
            popup.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortbutton_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(true) {
            if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_REQPAGE_A) {
                requestintent.putExtra("ac", "lo");
                setResult(RESULT_OK,requestintent);
            } else {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_A) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                }
                else{
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_CREATEAC_A){
                        createacintent.putExtra("ac","lo");
                        setResult(RESULT_OK, createacintent);
                    }else {
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE_A) {
                            adminhomeintent.putExtra("ac", "lo");
                            setResult(RESULT_OK, adminhomeintent);
                        }else{
                            if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                            {
                                mainintent.putExtra("key1","logout");
                                setResult(RESULT_OK,mainintent);
                            }
                        }
                    }

                }
            }
            adminarchives.this.finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminarchives);
        Initialize();
        InitializeLogic();
    }
    private void Initialize(){

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vmauth=FirebaseAuth.getInstance();

        aasortbutton=findViewById(R.id.aasortbutton);
        homebutton2=findViewById(R.id.homebutton2);
        myrequestbutton=findViewById(R.id.myrequestsbutton);
        createaccountbutton=findViewById(R.id.createaccountbutton);
        settingsbutton2=findViewById(R.id.settingsbutton2);
        logoutbutton2=findViewById(R.id.logoutbutton2);
        archivebutton2=findViewById(R.id.archivebutton2);

        mainintent.setClass(adminarchives.this,adminsettings.class);
        adminhomeintent.setClass(adminarchives.this,admin_home.class);
        tcdintent.setClass(adminarchives.this,depttotcompdec.class);
        requestintent.setClass(adminarchives.this, adminrequests.class);
        createacintent.setClass(adminarchives.this,admincreateaccount.class );
        archiveintent.setClass(adminarchives.this,adminarchives.class);

        popup = new PopupMenu(this,aasortbutton);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sortmenu, popup.getMenu());

        settingsbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_A);
            }
        });

        createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(createacintent,REQUEST_CODE_CREATEAC_A );
            }
        });


        myrequestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(requestintent,REQUEST_CODE_REQPAGE_A);
            }
        });

        archivebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES_A);
            }
        });

        logoutbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_REQPAGE_A) {
                    requestintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,requestintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_A) {
                        settingsintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,settingsintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_CREATEAC_A){
                            createacintent.putExtra("ac","lo");
                            setResult(RESULT_OK, createacintent);
                        }else {
                            if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE_A) {
                                adminhomeintent.putExtra("ac", "lo");
                                setResult(RESULT_OK, adminhomeintent);
                            }else{
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }
                        }

                    }
                }
                adminarchives.this.finish();
            }
        });

    }

    private void InitializeLogic(){

    }
}
