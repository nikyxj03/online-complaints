package com.msm.onlinecomplaintapp.Users.UserActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.msm.onlinecomplaintapp.Common.ImageConverter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.depttotcompdec;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.msm.onlinecomplaintapp.Users.UserAdapters.UCompListAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class userarchives extends UserActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Toolbar toolbar;

    private int uidpos=0;

    private TextView profileEmailText;
    private TextView profileNameText;
    private CircleImageView profilePhoto;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;
    private Button querybutton;
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
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_QUERY){
                                    queryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,queryintent);
                                }
                                else {
                                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                        mainintent.putExtra("key1", "logout");
                                        setResult(RESULT_OK, mainintent);
                                    }
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

        toolbar=findViewById(R.id.u_ua_toolbar);
        setSupportActionBar(toolbar);

        _drawer = findViewById(R.id._drawer);
        _toggle = new ActionBarDrawerToggle(this, _drawer, R.string.open, R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading...");

        profileEmailText=findViewById(R.id.user_profile_email);
        profileNameText=findViewById(R.id.user_profile_name);
        profilePhoto=findViewById(R.id.user_profile_photo);

        homebutton = findViewById(R.id.homebutton);
        mycomplaintsbutton = findViewById(R.id.mycomplaintsbutton);
        settingsbutton = findViewById(R.id.settingsbutton);
        archivebutton = findViewById(R.id.archivebutton);
        notificationbutton = findViewById(R.id.notification_button);
        querybutton=findViewById(R.id.myqueriesbutton);
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

        querybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(queryintent,REQUEST_CODE_QUERY);
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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_QUERY){
                                    queryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,queryintent);
                                }
                                else {
                                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                        mainintent.putExtra("key1", "logout");
                                        setResult(RESULT_OK, mainintent);
                                    }
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

        GlobalApplication.databaseHelper.fetchUserData(getCurrentUserId(), new OnDataSFetchListener<Users>() {
            @Override
            public void onDataSFetch(Users users) {
                profileEmailText.setText(users.getEmail());
                profileNameText.setText(users.getFullname());
                if(users.getProfilePhoto()!=null)
                    Glide.with(userarchives.this).asBitmap().load(users.getProfilePhoto()).placeholder(R.mipmap.ananymous_person_round).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            profilePhoto.setImageBitmap(ImageConverter.getRoundedCornerBitmap(resource,100));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            }
        });

        GlobalApplication.databaseHelper.getPublicArchivedComplaintsSS(new OnDataFetchListener<Complaint>() {
            @Override
            public void onDataFetched(List<Complaint> complaints) {
                hideProgress();
                if(complaints!=null) {
                    if(smf==1) {
                        ssarchivecomplaintlist = complaints;
                        uCompListAdapter.setList(ssarchivecomplaintlist);
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
                    }
                }
            }
        });

    }

}
