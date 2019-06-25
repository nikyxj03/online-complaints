package com.msm.onlinecomplaintapp.DepartmentActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.DepartmentAdapters.DeptPagerAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnSortChange;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class deptcomplaints extends DepartmentActivity {


    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button archivebutton1;
    private Button deptarchivesbutton1;
    private Button deptcomplaintsbutton1;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private DeptPagerAdapter deptPagerAdapter;

    private OnSortChange onSortChange1;
    private OnSortChange onSortChange0;


    private int smf=0;
    private String did="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(true) {
            if (resultCode==RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_D) {
                    depthomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,depthomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                        settingsintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,settingsintent);
                    }
                    else{
                        if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_ARCHIVES_D){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_D)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }

                        }

                    }
                }
                deptcomplaints.this.finish();
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
            onSortChange0.onSortChanged(smf);
        }
        if(item.getItemId()==R.id.item2){
            smf=1;
            onSortChange1.onSortChanged(smf);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptcomplaints);
        Initialize();
    }

    public void setSortListener_ZERO(Context context, OnSortChange onSortChange){
        this.onSortChange0=onSortChange;
    }

    public OnSortChange getSortListener_ZERO(){
        return onSortChange0;
    }

    public void setSortListener_ONE(Context context,OnSortChange onSortChange){
        Toast.makeText(context,"1234",Toast.LENGTH_LONG).show();
        this.onSortChange1=onSortChange;
    }

    public OnSortChange getSortListener_ONE(){
        return onSortChange1;
    }

    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(deptcomplaints.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading");

        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivesbutton1=findViewById(R.id.deptarchivebutton1);
        archivebutton1=findViewById(R.id.archivebutton1);

        viewPager=findViewById(R.id.dept_complaints_pager);
        tabLayout=findViewById(R.id.dept_complaints_tab);

        setintents(this);

        homebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(depthomeintent,REQUEST_CODE_HOMEPAGE_D);
            }
        });

        settingsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_D);
            }
        });

        archivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES_D );
            }
        });

        deptarchivesbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptarchiveintent,REQUEST_CODE_DEPTARCHIVES_D);
            }
        });

        logoutbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_D) {
                    depthomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,depthomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                        settingsintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,settingsintent);
                    }
                    else{
                        if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_ARCHIVES_D){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_D)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }

                        }

                    }
                }
                deptcomplaints.this.finish();
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Registered"));
        tabLayout.addTab(tabLayout.newTab().setText("Watching"));

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

        GlobalApplication.databaseHelper.fetchDeptUserData(getCurrentUserId(), new OnDataSFetchListener<DeptUsers>() {
            @Override
            public void onDataSFetch(DeptUsers deptUsers) {
                if(deptUsers!=null){
                    did=deptUsers.getDept();
                    deptPagerAdapter =new DeptPagerAdapter(getSupportFragmentManager(),did,0,0);
                    viewPager.setAdapter(deptPagerAdapter);
                }
                hideProgress();
            }
        });

    }
}
