package com.msm.onlinecomplaintapp.UserActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.LoginActivities.LoginActivity;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.DepartmentActivities.depttotcompdec;
import com.msm.onlinecomplaintapp.UserActivity;
import com.msm.onlinecomplaintapp.UserAdapters.UCompListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class userarchives extends UserActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private int uidpos=0;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;
    private ListView archivelistview1;

    private int smf=0;
    private String uid="";

    private UCompListAdapter uCompListAdapter;

    private List<Complaint> ssarchivecomplaintlist = new ArrayList<>();
    private List<Complaint> starchivecompaintlist=new ArrayList<>();

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
        if(item.getItemId()==R.id.item1){
            smf=0;
            if(starchivecompaintlist!=null)
                uCompListAdapter.setList(starchivecompaintlist);
        }
        if(item.getItemId()==R.id.item2){
            smf=1;
            if(ssarchivecomplaintlist!=null){
                uCompListAdapter.setList(ssarchivecomplaintlist);
            }
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
        if(requestCode!=REQUEST_CODE_NEWCOMP && requestCode!=REQUEST_CODE_TCD) {
            if (resultCode==RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_SETTINGS) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MYCOMPLAINT) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,mycomplaintintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_HOMEPAGE){
                            homeintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,homeintent);
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
                this.finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userarchives);
        Initialize();
    }

    private void Initialize() {
        _drawer = findViewById(R.id._drawer);
        _toggle = new ActionBarDrawerToggle(this, _drawer, R.string.open, R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading...");

        homebutton = findViewById(R.id.homebutton);
        mycomplaintsbutton = findViewById(R.id.mycomplaintsbutton);
        settingsbutton = findViewById(R.id.settingsbutton);
        archivebutton = findViewById(R.id.archivebutton);
        notificationbutton = findViewById(R.id.notification_button);
        logoutbutton = findViewById(R.id.logoutbutton);
        archivelistview1 = findViewById(R.id.archivelistview1);

        setintents(this);

        mycomplaintsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mycomplaintintent, REQUEST_CODE_MYCOMPLAINT);
            }
        });

        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent, REQUEST_CODE_SETTINGS);
            }
        });

        notificationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(notificationintent, REQUEST_CODE_NOTIFICATION);
            }
        });

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(homeintent, REQUEST_CODE_ARCHIVES);
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
                        if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_HOMEPAGE){
                            homeintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,homeintent);
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
                userarchives.this.finish();
            }
        });

        uCompListAdapter=new UCompListAdapter(this,R.layout.pchlistcustom,smf,PAGE_PUBLIC_ARCHIVES);
        archivelistview1.setAdapter(uCompListAdapter);

        GlobalApplication.databaseHelper.getPublicArchivedComplaintsSS(new OnDataFetchListener<Complaint>() {
            @Override
            public void onDataFetched(List<Complaint> complaints) {
                hideProgress();
                if(complaints!=null) {
                    if(smf==1) {
                        ssarchivecomplaintlist = complaints;
                        uCompListAdapter.setList(ssarchivecomplaintlist);
                        hideProgress();
                    }
                }
            }
        });

        GlobalApplication.databaseHelper.getPublicArchivedComplaintsST(new OnDataFetchListener<Complaint>() {
            @Override
            public void onDataFetched(List<Complaint> complaints) {
                hideProgress();
                if(complaints!=null) {
                    if(smf==0) {
                        starchivecompaintlist = complaints;
                        uCompListAdapter.setList(starchivecompaintlist);
                        hideProgress();
                    }
                }
            }
        });

    }

}
