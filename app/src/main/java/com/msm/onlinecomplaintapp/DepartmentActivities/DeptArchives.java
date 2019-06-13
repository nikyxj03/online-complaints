package com.msm.onlinecomplaintapp.DepartmentActivities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.DepartmentAdapters.DeptArchivesPagerAdapter;
import com.msm.onlinecomplaintapp.DepartmentFragments.DeptComplaintsFragment;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.R;

public class DeptArchives extends DepartmentActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;
    private Button deptarchivbutton1;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewGroup viewGroup;

    private String did;

    private DeptArchivesPagerAdapter deptArchivesPagerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortbutton_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId()==R.id.item1){
            if(tabLayout.getSelectedTabPosition()==0){
                getSortListener_ZERO().onSortChanged(0);
            }else {
                getSortListener_ONE().onSortChanged(0);
            }

        }
        if(item.getItemId()==R.id.item2){
            if(tabLayout.getSelectedTabPosition()==0){
                getSortListener_ZERO().onSortChanged(1);
            }else {
                getSortListener_ONE().onSortChanged(1);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(_drawer.isDrawerOpen(GravityCompat.START))
        {
            _drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

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
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                        deptcomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,deptcomplaintintent);
                    }
                    else{
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D){
                                settingsintent.putExtra("ac","lo");
                                setResult(RESULT_OK,settingsintent);
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
                DeptArchives.this.finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_archives);

        setintents(DeptArchives.this);

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(DeptArchives.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading");

        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        archivebutton1=findViewById(R.id.archivebutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivbutton1=findViewById(R.id.deptarchivebutton1);

        viewPager=findViewById(R.id.dept_archives_pager);
        tabLayout=findViewById(R.id.dept_archives_tab);

        homebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(depthomeintent,REQUEST_CODE_HOMEPAGE_D);
            }
        });

        deptcomplaintsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptcomplaintintent,REQUEST_CODE_DeptCOMPLAINT_D);
            }
        });

        archivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES_D);
            }
        });

        settingsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_D);
            }
        });

        logoutbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_D) {
                    depthomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,depthomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                        deptcomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,deptcomplaintintent);
                    }
                    else{
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D){
                                settingsintent.putExtra("ac","lo");
                                setResult(RESULT_OK,settingsintent);
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
                DeptArchives.this.finish();
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Resolved"));
        tabLayout.addTab(tabLayout.newTab().setText("Ignored"));

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
                    deptArchivesPagerAdapter=new DeptArchivesPagerAdapter(getSupportFragmentManager(),did,0);
                    viewPager.setAdapter(deptArchivesPagerAdapter);
                    hideProgress();
                }
            }
        });


    }
}
