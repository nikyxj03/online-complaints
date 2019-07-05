package com.msm.onlinecomplaintapp.Users.UserActivities;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.msm.onlinecomplaintapp.Common.ImageConverter;
import com.msm.onlinecomplaintapp.Department.DepartmentAdapters.DeptPagerAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnSortChange;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.msm.onlinecomplaintapp.Users.UserAdapters.UCompPagerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class mycomplaints extends UserActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Toolbar toolbar;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;

    private TextView profileEmailText;
    private TextView profileNameText;
    private CircleImageView profilePhoto;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private OnSortChange onSortChange1;
    private OnSortChange onSortChange0;

    private UCompPagerAdapter uCompPagerAdapter;

    private int smf=0;
    private String uid="";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(requestCode!=REQUEST_CODE_TCD) {
            if (resultCode==RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_SETTINGS) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,homeintent);
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
                mycomplaints.this.finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.sortbutton_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId()==R.id.item1){
            smf=0;
            if(tabLayout.getSelectedTabPosition()==0){
                getSortListener_ZERO().onSortChanged(0);
            }else {
                getSortListener_ONE().onSortChanged(0);
            }
        }
        if(item.getItemId()==R.id.item2){
            smf=1;
            if(tabLayout.getSelectedTabPosition()==0){
                getSortListener_ZERO().onSortChanged(1);
            }else {
                getSortListener_ONE().onSortChanged(1);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomplaints);

        toolbar=findViewById(R.id.u_mc_toolbar);
        setSupportActionBar(toolbar);

        Initialize();
    }

    public void setSortListener_ZERO(Context context, OnSortChange onSortChange){
        Toast.makeText(context,"1234",Toast.LENGTH_LONG).show();
        this.onSortChange0=onSortChange;
    }

    public OnSortChange getSortListener_ZERO(){
        return onSortChange0;
    }

    public void setSortListener_ONE(Context context,OnSortChange onSortChange){
        this.onSortChange1=onSortChange;
    }

    public OnSortChange getSortListener_ONE(){
        return onSortChange1;
    }

    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(mycomplaints.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading...");

        uid=getCurrentUserId();

        profileEmailText=findViewById(R.id.user_profile_email);
        profileNameText=findViewById(R.id.user_profile_name);
        profilePhoto=findViewById(R.id.user_profile_photo);

        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        logoutbutton=findViewById(R.id.logoutbutton);
        archivebutton=findViewById(R.id.archivebutton);
        notificationbutton=findViewById(R.id.notification_button);

        viewPager=findViewById(R.id.user_complaints_pager);
        tabLayout=findViewById(R.id.user_complaints_tab);

        setintents(this);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(homeintent,REQUEST_CODE_HOMEPAGE);
            }
        });

        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS);
            }
        });

        mycomplaintsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mycomplaintintent,REQUEST_CODE_MYCOMPLAINT );
            }
        });

        notificationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(notificationintent,REQUEST_CODE_NOTIFICATION);
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
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,homeintent);
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
                mycomplaints.this.finish();
            }
        });

        GlobalApplication.databaseHelper.fetchUserData(getCurrentUserId(), new OnDataSFetchListener<Users>() {
            @Override
            public void onDataSFetch(Users users) {
                profileEmailText.setText(users.getEmail());
                profileNameText.setText(users.getFullname());
                if(users.getProfilePhoto()!=null)
                    Glide.with(mycomplaints.this).asBitmap().load(users.getProfilePhoto()).placeholder(R.mipmap.ananymous_person_round).into(new CustomTarget<Bitmap>() {
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

        tabLayout.addTab(tabLayout.newTab().setText("OPEN"));
        tabLayout.addTab(tabLayout.newTab().setText("CLOSED"));

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        uCompPagerAdapter=new UCompPagerAdapter(getSupportFragmentManager(),getCurrentUserId(),smf);
        viewPager.setAdapter(uCompPagerAdapter);

        hideProgress();

    }

}
