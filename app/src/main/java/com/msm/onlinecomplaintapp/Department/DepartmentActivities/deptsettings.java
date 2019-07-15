package com.msm.onlinecomplaintapp.Department.DepartmentActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.msm.onlinecomplaintapp.Department.DepartmentActivity;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.R;

public class deptsettings extends DepartmentActivity {

    private Toolbar toolbar;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;
    private Button deptarchivbutton1;
    private Button deptquerybutton1;

    private LinearLayout drepalinear;
    private LinearLayout dacchlinear;
    private EditText dnpedit;
    private EditText doldpedit;
    private EditText drnpedit;
    private EditText dnameedit;
    private EditText demailidedit;
    private EditText dphonenoedit;
    private Button drepabutton;
    private Button dscbutton;
    private Button drpbutton;

    private int anaf=0;

    private AuthCredential vmcredential;
    private FirebaseUser vmauthu;

    private DeptUsers CudeptUsers;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_toggle.onOptionsItemSelected(item)) {
            return true;
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
            if(anaf==1)
            {
                anaf=0;
                dacchlinear.setVisibility(View.VISIBLE);
                drepalinear.setVisibility(View.GONE);
            }
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
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
                deptsettings.this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptsettings);

        toolbar=findViewById(R.id.d_ds_toolbar);
        setSupportActionBar(toolbar);

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(deptsettings.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading...");

        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        archivebutton1=findViewById(R.id.archivebutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivbutton1=findViewById(R.id.deptarchivebutton1);
        deptquerybutton1=findViewById(R.id.deptqueriesbutton1);

        drepalinear=findViewById(R.id.drepalinear);
        dacchlinear=findViewById(R.id.dacchlinear);
        demailidedit=findViewById(R.id.demailidedit);
        dnameedit=findViewById(R.id.dnameedit);
        dphonenoedit=findViewById(R.id.dphonenoedit);
        doldpedit=findViewById(R.id.doldpedit);
        dnpedit=findViewById(R.id.dnpedit);
        drnpedit=findViewById(R.id.drnpedit);
        drepabutton=findViewById(R.id.drepabutton);
        drpbutton=findViewById(R.id.drpbutton);
        dscbutton=findViewById(R.id.dscbutton);

        setintents(this);

        vmauthu=FirebaseAuth.getInstance().getCurrentUser();

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

        deptarchivbutton1.setOnClickListener(new View.OnClickListener() {
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
            public void onClick(View view) {
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
                deptsettings.this.finish();
            }
        });

        dscbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("Updating...");
                CudeptUsers.setFullname(dnameedit.getText().toString());
                CudeptUsers.setPhoneno(dphonenoedit.getText().toString());
                GlobalApplication.databaseHelper.updateDeptUserData(CudeptUsers, new OnDataUpdatedListener() {
                    @Override
                    public void onDataUploaded(boolean success) {
                        hideProgress();
                        if(success)
                            Toast.makeText(deptsettings.this,"Details Updated",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        drpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drepalinear.setVisibility(View.VISIBLE);
                dacchlinear.setVisibility(View.GONE);
                anaf=1;
            }
        });

        drepabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doldpedit.getText().toString().length() > 0) {
                    showProgress("Changing Password..");
                    vmcredential = EmailAuthProvider.getCredential(vmauthu.getEmail(), doldpedit.getText().toString());
                    vmauthu.reauthenticate(vmcredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (dnpedit.getText().toString().equals(drnpedit.getText().toString())) {
                                    if (dnpedit.getText().toString().length() >= 6) {
                                        vmauthu.updatePassword(dnpedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {
                                                hideProgress();
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(deptsettings.this, "password updated", Toast.LENGTH_LONG).show();
                                                    drepalinear.setVisibility(View.GONE);
                                                    dacchlinear.setVisibility(View.VISIBLE);
                                                    anaf = 0;
                                                } else {
                                                    Toast.makeText(deptsettings.this, "password not updated", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        hideProgress();
                                        Toast.makeText(deptsettings.this, "new password size must be more than 6", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    hideProgress();
                                    Toast.makeText(deptsettings.this, "passwords do not match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                hideProgress();
                                Toast.makeText(deptsettings.this, "Old password is not correct", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(deptsettings.this,"old password must not be empty",Toast.LENGTH_LONG).show();
                }
            }

        });

        dacchlinear.setVisibility(View.VISIBLE);
        drepalinear.setVisibility(View.GONE);

        demailidedit.setFocusable(false);
        demailidedit.setFocusableInTouchMode(false);
        demailidedit.setClickable(false);

        GlobalApplication.databaseHelper.fetchDeptUserData(getCurrentUserId(), new OnDataSFetchListener<DeptUsers>() {
            @Override
            public void onDataSFetch(DeptUsers users) {
                CudeptUsers=users;
                demailidedit.setText(users.getEmail());
                dnameedit.setText(users.getFullname());
                dphonenoedit.setText(users.getPhoneno());
                hideProgress();
            }
        });
    }
}
