package com.msm.onlinecomplaintapp.Department.DepartmentActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.msm.onlinecomplaintapp.Department.DepartmentActivity;
import com.msm.onlinecomplaintapp.Department.DepartmentAdapters.DCompListAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class department_home extends DepartmentActivity {

    private Toolbar toolbar;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;
    private Button deptarchivebutton1;
    private Button deptquerybutton1;
    private Button sortbutton1;
    private ListView complaintlistview1;

    private int smf=0;

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private List<Complaint> stcomplaintlistmap=new ArrayList<>();
    private List<Complaint> sscomplaintlistmap=new ArrayList<>();

    private DCompListAdapter dCompListAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
            if (resultCode==RESULT_OK) {
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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTQUERY_D){
                                    deptqueryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,deptqueryintent);
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
                department_home.this.finish();
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
        if(item.getItemId()==R.id.item1){
            smf=0;
            if(stcomplaintlistmap!=null)
                dCompListAdapter.setList(stcomplaintlistmap);
        }
        if(item.getItemId()==R.id.item2){
            smf=1;
            if(sscomplaintlistmap!=null)
                dCompListAdapter.setList(sscomplaintlistmap);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_home);

        toolbar=findViewById(R.id.d_hp_toolbar);
        setSupportActionBar(toolbar);

        showProgress("Loading..");
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vmauth=FirebaseAuth.getInstance();

        complaintlistview1=findViewById(R.id.complaintlistview1);
        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        archivebutton1=findViewById(R.id.archivebutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivebutton1=findViewById(R.id.deptarchivebutton1);
        deptquerybutton1=findViewById(R.id.deptqueriesbutton1);

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

        deptquerybutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptqueryintent,REQUEST_CODE_DEPTQUERY_D);
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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTQUERY_D){
                                    deptqueryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,deptqueryintent);
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
                department_home.this.finish();
            }
        });

        dCompListAdapter=new DCompListAdapter(department_home.this,R.layout.deptcompistcustom,smf,PAGE_HOME_D);
        complaintlistview1.setAdapter(dCompListAdapter);

        GlobalApplication.databaseHelper.getPublicComplaintsST(new OnDataFetchListener<Complaint>() {
            @Override
            public void onDataFetched(List<Complaint> complaints) {
                hideProgress();
                stcomplaintlistmap=complaints;
                if(stcomplaintlistmap!=null)
                    if(smf==0)
                        dCompListAdapter.setList(stcomplaintlistmap);
            }
        });

        GlobalApplication.databaseHelper.getPublicComplaintsSS(new OnDataFetchListener<Complaint>() {
            @Override
            public void onDataFetched(List<Complaint> complaints) {
                hideProgress();
                sscomplaintlistmap=complaints;
                if(sscomplaintlistmap!=null)
                    if(smf==1)
                        dCompListAdapter.setList(sscomplaintlistmap);
            }
        });

    }
}
