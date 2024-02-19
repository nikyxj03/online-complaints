package com.msm.onlinecomplaintapp.Department.DepartmentActivities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.protobuf.ByteString;
import com.msm.onlinecomplaintapp.Department.DepartmentActivity;
import com.msm.onlinecomplaintapp.Department.DepartmentAdapters.DeptQueryListAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.Models.UserQuery;
import com.msm.onlinecomplaintapp.R;

import java.io.IOException;
import java.util.List;

public class DeptQueryActivity extends DepartmentActivity {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;
    private Button deptarchivebutton1;

    private DeptQueryListAdapter deptQueryListAdapter;

    private DeptUsers deptUsers;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
            settingsintent.putExtra("ac", "lo");
            setResult(RESULT_OK, settingsintent);
        } else {
            if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                deptcomplaintintent.putExtra("ac", "lo");
                setResult(RESULT_OK, deptcomplaintintent);
            } else {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES_D) {
                    archiveintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, archiveintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTARCHIVES_D) {
                        deptarchiveintent.putExtra("ac", "lo");
                        setResult(RESULT_OK, deptarchiveintent);
                    } else {
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE_D) {
                            depthomeintent.putExtra("ac", "lo");
                            setResult(RESULT_OK, depthomeintent);
                        } else {
                            if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN_D) {
                                mainintent.putExtra("key1", "logout");
                                setResult(RESULT_OK, mainintent);
                            }
                        }
                    }

                }

            }
        }
        DeptQueryActivity.this.finish();
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
        setContentView(R.layout.activity_dept_query);

        showProgress("Loading..");

        refreshLayout=findViewById(R.id.dq_refresh);
        recyclerView=findViewById(R.id.dq_listview);
        toolbar=findViewById(R.id.d_q_toolbar);

        setSupportActionBar(toolbar);

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        archivebutton1=findViewById(R.id.archivebutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivebutton1=findViewById(R.id.deptarchivebutton1);

        setintents(this);

        deptcomplaintsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptcomplaintintent, REQUEST_CODE_DeptCOMPLAINT_D);
            }
        });

        archivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES_D  );
            }
        });

        settingsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_D  );
            }
        });

        deptarchivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptarchiveintent,REQUEST_CODE_DEPTARCHIVES_D);
            }
        });

        homebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(depthomeintent,REQUEST_CODE_HOMEPAGE_D);
            }
        });


        logoutbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                        deptcomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK, deptcomplaintintent);
                    } else {
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES_D){
                            archiveintent.putExtra("ac","lo");
                            setResult(RESULT_OK,archiveintent);
                        }else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
                            }
                            else {
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE_D){
                                    depthomeintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,depthomeintent);
                                }
                                else {
                                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN_D) {
                                        mainintent.putExtra("key1", "logout");
                                        setResult(RESULT_OK, mainintent);
                                    }
                                }
                            }

                        }

                    }
                }
                DeptQueryActivity.this.finish();
            }
        });

        deptQueryListAdapter=new DeptQueryListAdapter(this);
        recyclerView.setAdapter(deptQueryListAdapter);

        GlobalApplication.databaseHelper.fetchDeptUserData(getCurrentUserId(), new OnDataSFetchListener<DeptUsers>() {
            @Override
            public void onDataSFetch(DeptUsers deptUsers) {
                DeptQueryActivity.this.deptUsers=deptUsers;
                updateData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

    }

    public void updateData(){
        GlobalApplication.databaseHelper.getDeptQueries(deptUsers.getDept(), new OnDataFetchListener<UserQuery>() {
            @Override
            public void onDataFetched(List<UserQuery> userQueries) {
                deptQueryListAdapter.setList(userQueries);
                Log.i("abcdef",String.valueOf(userQueries.size()));
                hideProgress();
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
