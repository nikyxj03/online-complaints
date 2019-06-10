package com.msm.onlinecomplaintapp.UserActivities;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class settingsactivity extends AppCompatActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private static final int REQUEST_CODE_NEWCOMP=5;
    private static final int REQUEST_CODE_HOMEPAGE=11;
    private static final int REQUEST_CODE_MYCOMPLAINT=12;
    private static final int REQUEST_CODE_SETTINGS=13;
    private static final int REQUEST_CODE_MAIN=0;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;

    private LinearLayout repalinear;
    private LinearLayout acchlinear;
    private EditText npedit;
    private EditText oldpedit;
    private EditText rnpedit;
    private EditText nameedit;
    private EditText emailidedit;
    private EditText phonenoedit;
    private Button repabutton;
    private Button scbutton;
    private Button rpbutton;

    private ArrayList<HashMap<String,Object>> userdatalistmap =new ArrayList<>();
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

    private FirebaseAuth vmauth;
    private FirebaseAuth.AuthStateListener vmauthlistner;
    private AuthCredential vmcredential;
    private FirebaseUser vmauthu;

    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference ud = _database.getReference("userdata");
    private ChildEventListener _ud_child_listener;

    private Intent mainintent=new Intent();
    private Intent newcompintent=new Intent();
    private Intent mycomplaintintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent homeintent=new Intent();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(requestCode!=REQUEST_CODE_NEWCOMP) {
            if (resultCode==RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,homeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MYCOMPLAINT) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,mycomplaintintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN)
                        {
                            mainintent.putExtra("key1","logout");
                            setResult(RESULT_OK,mainintent);
                        }
                    }
                }
                settingsactivity.this.finish();
            }
        }
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
                acchlinear.setVisibility(View.VISIBLE);
                repalinear.setVisibility(View.GONE);
            }
            else {
                super.onBackPressed();
            }
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
        setContentView(R.layout.activity_settingsactivity);
        Initialize();
        InitializeLogic();
    }
    private void Initialize(){

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(settingsactivity.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vmauthu=FirebaseAuth.getInstance().getCurrentUser();

        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        logoutbutton=findViewById(R.id.logoutbutton);

        repalinear=findViewById(R.id.repalinear);
        acchlinear=findViewById(R.id.acchlinear);
        emailidedit=findViewById(R.id.emailidedit);
        nameedit=findViewById(R.id.nameedit);
        phonenoedit=findViewById(R.id.phonenoedit);
        oldpedit=findViewById(R.id.oldpedit);
        npedit=findViewById(R.id.npedit);
        rnpedit=findViewById(R.id.rnpedit);
        repabutton=findViewById(R.id.repabutton);
        rpbutton=findViewById(R.id.rpbutton);
        scbutton=findViewById(R.id.scbutton);

        mainintent.setClass(settingsactivity.this,MainActivity.class);
        mycomplaintintent.setClass(settingsactivity.this,mycomplaints.class);
        settingsintent.setClass(settingsactivity.this,settingsactivity.class);
        homeintent.setClass(settingsactivity.this,homepage.class);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(homeintent,REQUEST_CODE_HOMEPAGE);
            }
        });

        mycomplaintsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mycomplaintintent,REQUEST_CODE_MYCOMPLAINT);
            }
        });


        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,homeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MYCOMPLAINT) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,mycomplaintintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN)
                        {
                            mainintent.putExtra("key1","logout");
                            setResult(RESULT_OK,mainintent);
                        }
                    }
                }
                settingsactivity.this.finish();
            }
        });

        scbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempmap=new HashMap<>();
                tempmap.put("fullname",nameedit.getText().toString());
                tempmap.put("phoneno",phonenoedit.getText().toString());
                tempmap.put("email",userdatalistmap.get(udindex).get("email").toString());
                tempmap.put("uid",userdatalistmap.get(udindex).get("uid").toString());
                tempmap.put("uenable",userdatalistmap.get(udindex).get("uid").toString());
                tempmap.put("cat","student" );
                ud.child(uid).updateChildren(tempmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(settingsactivity.this,"Successfully updated" ,Toast.LENGTH_LONG ).show();
                    }
                });
            }
        });

        rpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repalinear.setVisibility(View.VISIBLE);
                acchlinear.setVisibility(View.GONE);
                anaf=1;
            }
        });

        repabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldpedit.getText().toString().length() > 0) {
                    vmcredential = EmailAuthProvider.getCredential(vmauthu.getEmail(), oldpedit.getText().toString());
                    vmauthu.reauthenticate(vmcredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (npedit.getText().toString().equals(rnpedit.getText().toString())) {
                                    if (npedit.getText().toString().length() >= 6) {
                                        vmauthu.updatePassword(npedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(settingsactivity.this, "password updated", Toast.LENGTH_LONG).show();
                                                    repalinear.setVisibility(View.GONE);
                                                    acchlinear.setVisibility(View.VISIBLE);
                                                    anaf = 0;
                                                } else {
                                                    Toast.makeText(settingsactivity.this, "password not updated", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(settingsactivity.this, "new password size must be more than 6", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(settingsactivity.this, "passwords do not match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(settingsactivity.this, "Old password is not correct", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(settingsactivity.this,"old password must not be empty",Toast.LENGTH_LONG).show();
                }
            }

        });

    }
    private void InitializeLogic(){
        uid=vmauthu.getUid();
        acchlinear.setVisibility(View.VISIBLE);
        repalinear.setVisibility(View.GONE);

        emailidedit.setFocusable(false);
        emailidedit.setFocusableInTouchMode(false);
        emailidedit.setClickable(false);

        ud.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                userdatalistmap= new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        userdatalistmap.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                counter1=0;
                for(int i = 0; i< userdatalistmap.size(); i++)
                {
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(userdatalistmap.get(counter1).get("uid").toString())) {
                        udindex=counter1;
                    }
                    counter1++;
                }


                emailidedit.setText(userdatalistmap.get(udindex).get("email").toString());
                nameedit.setText(userdatalistmap.get(udindex).get("fullname").toString());
                phonenoedit.setText(userdatalistmap.get(udindex).get("phoneno").toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
