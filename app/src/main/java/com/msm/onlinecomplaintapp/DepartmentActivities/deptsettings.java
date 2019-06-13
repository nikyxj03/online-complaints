package com.msm.onlinecomplaintapp.DepartmentActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class deptsettings extends DepartmentActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;
    private Button deptarchivbutton1;

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

    private ArrayList<HashMap<String,Object>> deptuserdatalistmap =new ArrayList<>();
    private ArrayList<HashMap<String,Object>> templistmap =new ArrayList<>();
    private ArrayList<HashMap<String,Object>> templistmap1 =new ArrayList<>();
    private HashMap<String,Object> tempmap = new HashMap<>();

    private int udindex=0;
    private int counter1=0;
    private int i14=0;
    private int i15=0;
    private int i16=0;
    private int anaf=0;
    private String uid="";

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener vmauthlistner;
    private AuthCredential vmcredential;
    private FirebaseUser vmauthu;

    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference dud = _database.getReference("deptuserdata");


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
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTARCHIVES_D){
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
                deptsettings.this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptsettings);

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(deptsettings.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        archivebutton1=findViewById(R.id.archivebutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivbutton1=findViewById(R.id.deptarchivebutton1);

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

        vmauth=FirebaseAuth.getInstance();
        vmauthu=vmauth.getCurrentUser();

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
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_D)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
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
                tempmap=new HashMap<>();
                tempmap.put("fullname",dnameedit.getText().toString());
                tempmap.put("phoneno",dphonenoedit.getText().toString());
                tempmap.put("email",deptuserdatalistmap.get(udindex).get("email").toString());
                tempmap.put("uid",deptuserdatalistmap.get(udindex).get("uid").toString());
                dud.child(uid).updateChildren(tempmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(deptsettings.this, "Updated Successfully",Toast.LENGTH_LONG ).show();
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
                    vmcredential = EmailAuthProvider.getCredential(vmauthu.getEmail(), doldpedit.getText().toString());
                    vmauthu.reauthenticate(vmcredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (dnpedit.getText().toString().equals(drnpedit.getText().toString())) {
                                    if (dnpedit.getText().toString().length() >= 6) {
                                        vmauthu.updatePassword(dnpedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
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
                                        Toast.makeText(deptsettings.this, "new password size must be more than 6", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(deptsettings.this, "passwords do not match", Toast.LENGTH_LONG).show();
                                }
                            } else {
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

        uid=vmauth.getCurrentUser().getUid();
        dacchlinear.setVisibility(View.VISIBLE);
        drepalinear.setVisibility(View.GONE);

        demailidedit.setFocusable(false);
        demailidedit.setFocusableInTouchMode(false);
        demailidedit.setClickable(false);

        dud.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                deptuserdatalistmap= new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        deptuserdatalistmap.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                counter1=0;
                for(int i = 0; i<deptuserdatalistmap.size(); i++)
                {
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(deptuserdatalistmap.get(counter1).get("uid").toString())) {
                        udindex=counter1;
                    }
                    counter1++;
                }


                demailidedit.setText(deptuserdatalistmap.get(udindex).get("email").toString());
                dnameedit.setText(deptuserdatalistmap.get(udindex).get("fullname").toString());
                dphonenoedit.setText(deptuserdatalistmap.get(udindex).get("phoneno").toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
