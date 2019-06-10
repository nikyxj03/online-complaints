package com.msm.onlinecomplaintapp.AdminActivities;

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
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.DepartmentActivities.depttotcompdec;

import java.util.ArrayList;
import java.util.HashMap;

public class adminsettings extends AppCompatActivity {

    private static final int REQUEST_CODE_HOMEPAGE_A=31;
    private static final int REQUEST_CODE_SETTINGS_A=34;
    private static final int REQUEST_CODE_MAIN_A=0;
    private static final int REQUEST_CODE_TCD_A=7;
    private static final int REQUEST_CODE_REQPAGE_A=32;
    private static final int REQUEST_CODE_CREATEAC_A=33;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Intent adminhomeintent=new Intent();
    private Intent requestintent=new Intent();
    private Intent createacintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent mainintent=new Intent();
    private Intent tcdintent =new Intent();

    private Button homebutton2;
    private Button logoutbutton2;
    private Button settingsbutton2;
    private Button myrequestbutton;
    private Button createaccountbutton;

    private LinearLayout arepalinear;
    private LinearLayout aacchlinear;
    private EditText anpedit;
    private EditText aoldpedit;
    private EditText arnpedit;
    private EditText anameedit;
    private EditText aemailidedit;
    private EditText aphonenoedit;
    private Button arepabutton;
    private Button ascbutton;
    private Button arpbutton;

    private ArrayList<HashMap<String,Object>> adminuserdatalistmap =new ArrayList<>();
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
    private DatabaseReference aud = _database.getReference("adminuserdata");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(true) {
            if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_A) {
                adminhomeintent.putExtra("ac", "lo");
                setResult(RESULT_OK,adminhomeintent);
            } else {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_REQPAGE_A) {
                    requestintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,requestintent);
                }
                else{
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_CREATEAC_A){
                        createacintent.putExtra("ac","lo");
                        setResult(RESULT_OK, createacintent);
                    }else {
                        if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                        {
                            mainintent.putExtra("key1","logout");
                            setResult(RESULT_OK,mainintent);
                        }
                    }

                }
            }
            adminsettings.this.finish();
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
                aacchlinear.setVisibility(View.VISIBLE);
                arepalinear.setVisibility(View.GONE);
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
        setContentView(R.layout.activity_adminsettings);

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(adminsettings.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        homebutton2=findViewById(R.id.homebutton2);
        myrequestbutton=findViewById(R.id.myrequestsbutton);
        createaccountbutton=findViewById(R.id.createaccountbutton);
        settingsbutton2=findViewById(R.id.settingsbutton2);
        logoutbutton2=findViewById(R.id.logoutbutton2);

        mainintent.setClass(adminsettings.this,MainActivity.class);
        settingsintent.setClass(adminsettings.this,adminsettings.class);
        adminhomeintent.setClass(adminsettings.this,admin_home.class);
        tcdintent.setClass(adminsettings.this,depttotcompdec.class);
        requestintent.setClass(adminsettings.this, adminrequests.class);
        createacintent.setClass(adminsettings.this,admincreateaccount.class );

        createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(createacintent,REQUEST_CODE_CREATEAC_A );
            }
        });

        homebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(adminhomeintent,REQUEST_CODE_HOMEPAGE_A);
            }
        });

        myrequestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(requestintent,REQUEST_CODE_REQPAGE_A );
            }
        });

        logoutbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_A) {
                    adminhomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,adminhomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_REQPAGE_A) {
                        requestintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,requestintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_CREATEAC_A){
                            createacintent.putExtra("ac","lo");
                            setResult(RESULT_OK, createacintent);
                        }else {
                            if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                            {
                                mainintent.putExtra("key1","logout");
                                setResult(RESULT_OK,mainintent);
                            }
                        }

                    }
                }
                adminsettings.this.finish();
            }
        });

        ascbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempmap=new HashMap<>();
                tempmap.put("name",anameedit.getText().toString());
                tempmap.put("email",adminuserdatalistmap.get(udindex).get("email").toString());
                tempmap.put("uid",adminuserdatalistmap.get(udindex).get("uid").toString());
                aud.child(uid).updateChildren(tempmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(adminsettings.this,"Successfully updated" ,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        arpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arepalinear.setVisibility(View.VISIBLE);
                aacchlinear.setVisibility(View.GONE);
                anaf=1;
            }
        });

        arepabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aoldpedit.getText().toString().length() > 0) {
                    vmcredential = EmailAuthProvider.getCredential(vmauthu.getEmail(), aoldpedit.getText().toString());
                    vmauthu.reauthenticate(vmcredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (anpedit.getText().toString().equals(arnpedit.getText().toString())) {
                                    if (anpedit.getText().toString().length() >= 6) {
                                        vmauthu.updatePassword(anpedit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(adminsettings.this, "password updated", Toast.LENGTH_LONG).show();
                                                    arepalinear.setVisibility(View.GONE);
                                                    aacchlinear.setVisibility(View.VISIBLE);
                                                    anaf = 0;
                                                } else {
                                                    Toast.makeText(adminsettings.this, "password not updated", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(adminsettings.this, "new password size must be more than 6", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(adminsettings.this, "passwords do not match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(adminsettings.this, "Old password is not correct", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(adminsettings.this,"old password must not be empty",Toast.LENGTH_LONG).show();
                }
            }

        });

        uid=vmauthu.getUid();
        aacchlinear.setVisibility(View.VISIBLE);
        arepalinear.setVisibility(View.GONE);

        aemailidedit.setFocusable(false);
        aemailidedit.setFocusableInTouchMode(false);
        aemailidedit.setClickable(false);

        aud.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                adminuserdatalistmap= new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        adminuserdatalistmap.add(_map);
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }
                counter1=0;
                for(int i = 0; i<adminuserdatalistmap.size(); i++)
                {
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(adminuserdatalistmap.get(counter1).get("uid").toString())) {
                        udindex=counter1;
                    }
                    counter1++;
                }


                aemailidedit.setText(adminuserdatalistmap.get(udindex).get("email").toString());
                anameedit.setText(adminuserdatalistmap.get(udindex).get("name").toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
