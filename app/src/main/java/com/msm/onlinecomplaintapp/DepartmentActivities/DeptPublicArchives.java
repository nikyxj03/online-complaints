package com.msm.onlinecomplaintapp.DepartmentActivities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.DepartmentAdapters.DCompListAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.List;

public class DeptPublicArchives extends DepartmentActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;
    private Button deptarchivebutton1;

    private ListView adminarchivelistview;

    private DCompListAdapter dCompListAdapter;

    private List<Complaint> archivecomplaintlist = new ArrayList<>();
    private List<String> deptlist = new ArrayList<>();
    private List<String> seldeptist = new ArrayList<>();

    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference acffb = _database.getReference("complaint");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
        if (resultCode == RESULT_OK) {
            if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                settingsintent.putExtra("ac", "lo");
                setResult(RESULT_OK, settingsintent);
            } else {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                    deptcomplaintintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, deptcomplaintintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE_D) {
                        depthomeintent.putExtra("ac","lo");
                        setResult(RESULT_OK,depthomeintent);
                    } else {
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTARCHIVES_D){
                            deptarchiveintent.putExtra("ac","lo");
                            setResult(RESULT_OK,deptarchiveintent);
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
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_dept_archives);

        showProgress("Loading");

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);  
        archivebutton1=findViewById(R.id.archivebutton1);
        deptarchivebutton1=findViewById(R.id.deptarchivebutton1);
        adminarchivelistview = findViewById(R.id.deptarchivelistview);

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

        deptcomplaintsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptcomplaintintent,REQUEST_CODE_DeptCOMPLAINT_D );
            }
        });

        deptarchivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptarchiveintent,REQUEST_CODE_DEPTARCHIVES_D);
            }
        });

        logoutbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                        deptcomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK, deptcomplaintintent);
                    } else {
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE_D) {
                            depthomeintent.putExtra("ac","lo");
                            setResult(RESULT_OK,depthomeintent);
                        } else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
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
                DeptPublicArchives.this.finish();
            }
        });

        dCompListAdapter = new DCompListAdapter(DeptPublicArchives.this, R.layout.deptcompistcustom, 0,PAGE_PUBLIC_ARCHIVES_D);
        adminarchivelistview.setAdapter(dCompListAdapter);

        GlobalApplication.databaseHelper.getPublicArchivedComplaints(new OnDataFetchListener<Complaint>() {
            @Override
            public void onDataFetched(List<Complaint> complaints) {
                if(complaints!=null) {
                    archivecomplaintlist = new ArrayList<>();
                    archivecomplaintlist = complaints;
                    dCompListAdapter.setList(archivecomplaintlist);
                    hideProgress();
                }
            }
        });

       /* acffb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                archivecomplaintlist = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        Complaint complaint = new Complaint(_map);
                        archivecomplaintlist.add(complaint);
                        Log.i("abcde", complaint.getTitle());
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }

                dCompListAdapter=new DCompListAdapter(DeptPublicArchives.this, R.layout.deptcompistcustom, archivecomplaintlist, 0);
                deptarchivelistview.setAdapter(dCompListAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

}
