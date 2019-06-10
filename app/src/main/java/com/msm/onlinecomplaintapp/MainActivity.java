package com.msm.onlinecomplaintapp;

import android.app.*;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.*;
import android.content.*;

import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;
import com.msm.onlinecomplaintapp.AdminActivities.admin_home;
import com.msm.onlinecomplaintapp.DepartmentActivities.department_home;
import com.msm.onlinecomplaintapp.UserActivities.homepage;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_HOMEPAGE=11;
    private static final int RC_DEPARTMENT_HOME=21;
    private static final int RC_ADMIN_HOME=31;
    private static final int REQUEST_CODE_MAIN=0;

    private Intent mainintent=new Intent();
    private Intent homeintent=new Intent();
    private Intent depthomeintent=new Intent();
    private Intent adminhomeintent=new Intent();

    private static final long time = 600000;
    private Timer _timer = new Timer();
    private Timer _timer2 = new Timer();
    private TimerTask timer;
    private TimerTask timer2;

    private int utaflag=0;

    private SharedPreferences loginpreferences;
    private SharedPreferences.Editor loginprefeditor;

    private HashMap<String,Object> tempmap = new HashMap<>();
    private FirebaseDatabase _firebase=FirebaseDatabase.getInstance();

    private ArrayList<HashMap<String,Object>> udlistmap = new ArrayList<>();
    private ArrayList<HashMap<String,Object>> dudlistmap = new ArrayList<>();
    private ArrayList<HashMap<String,Object>> audlistmap = new ArrayList<>();

    private LinearLayout mainscreenlinear;
    private LinearLayout splashscreenlinear1;
    private LinearLayout loginsignuplinear;
    private LinearLayout loginlinear;
    private LinearLayout signuplinear;
    private LinearLayout verifylinear;
    private LinearLayout loginsignupimagelinear;
    private LinearLayout loginsignuploginlinear;
    private LinearLayout loginsignupsignuplinear;
    private LinearLayout signupimagelinear;
    private LinearLayout fullnamelinear;
    private LinearLayout emaillinear;
    private LinearLayout passwordlinear;
    private LinearLayout repasswordlinear;
    private LinearLayout phonelinear;
    private LinearLayout signupbuttonlinear;
    private LinearLayout loginimagelinear;
    private LinearLayout emailidlinear;
    private LinearLayout passwordtypelinear;
    private LinearLayout loginbuttonlinear;
    private ImageView splashimage;
    private ImageView loginsignuppagelogo;
    private ImageView signupimage;
    private ImageView loginimage;
    private TextView fullnametextview;
    private TextView emailtextview;
    private TextView passwordtextview;
    private TextView repasswordtextview;
    private TextView phonetextview;
    private TextView emailidtextview;
    private TextView passwordtypetextview;
    private EditText fullnameedit;
    private EditText emailedit;
    private EditText passwordedit;
    private EditText repasswordedit;
    private EditText phoneedit;
    private EditText emailidedit;
    private EditText passwordtypeedit;
    private EditText otpedit;
    private Button loginbutton;
    private Button signupbutton;
    private Button signupenterbutton;
    private Button loginenterbutton;
    private Button verifybutton;
    private Button resendbutton;
    private RadioGroup radiocat;
    private RadioButton studentrb;
    private RadioButton departmentrb;
    private RadioButton administratorrb;
    private Button reslinkbutton;
    private Button conbutton;

    private ProgressBar splashprogressbar;
    private FirebaseAuth vmauth;

    private String Authid="";
    private int rgno=0;

    private DatabaseReference sud=_firebase.getReference("userdata");
    private ChildEventListener _sud_child_listener;

    private DatabaseReference dud=_firebase.getReference("deptuserdata");
    private DatabaseReference aud=_firebase.getReference("adminuserdata");

    @Override
    public void onBackPressed() {
        if(loginsignupsignuplinear.getVisibility()==View.VISIBLE){
            splashscreenlinear1.setVisibility(View.GONE);
            loginsignupsignuplinear.setVisibility(View.GONE);
            loginsignuploginlinear.setVisibility(View.GONE);
            loginsignuplinear.setVisibility(View.VISIBLE);
        }else if(loginsignuploginlinear.getVisibility()==View.VISIBLE){
            splashscreenlinear1.setVisibility(View.GONE);
            loginsignupsignuplinear.setVisibility(View.GONE);
            loginsignuploginlinear.setVisibility(View.GONE);
            loginsignuplinear.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_HOMEPAGE || requestCode==RC_ADMIN_HOME || requestCode==RC_DEPARTMENT_HOME)
        {
            if(resultCode==RESULT_OK)
            {
                utaflag=0;
                if(data.getStringExtra("key1").equals("logout")) {
                    FirebaseAuth.getInstance().signOut();
                    loginsignuplinear.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                MainActivity.this.finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        vmauth=FirebaseAuth.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initializeLogic();

    }

    private void initialize() {
        loginpreferences=PreferenceManager.getDefaultSharedPreferences(this);
        loginprefeditor=loginpreferences.edit();

        splashscreenlinear1 = findViewById(R.id.splashscreenlinear1);
        loginsignuplinear = findViewById(R.id.loginsignuplinear);
        loginsignupsignuplinear=findViewById(R.id.loginsignupsignuplinear);
        loginlinear = findViewById(R.id.loginlinear);
        loginbutton = findViewById(R.id.loginbutton);
        signupbutton = findViewById(R.id.signupbutton);
        signuplinear = findViewById(R.id.signuplinear);
        fullnameedit = findViewById(R.id.fullnameedit);
        emailedit = findViewById(R.id.emailedit);
        loginenterbutton = findViewById(R.id.loginenterbutton);
        passwordedit = findViewById(R.id.passwordedit);
        repasswordedit = findViewById(R.id.repasswordedit);
        phoneedit = findViewById(R.id.phoneedit);
        emailidedit = findViewById(R.id.emailidedit);
        passwordtypeedit = findViewById(R.id.passwordtypeedit);
        signupenterbutton = findViewById(R.id.signupenterbutton);
        splashprogressbar=findViewById(R.id.splashprogressbar);
        splashimage= findViewById(R.id.splashimage);
        radiocat=findViewById(R.id.radiocategory);
        studentrb=findViewById(R.id.studentrb);
        departmentrb=findViewById(R.id.departmentrb);
        administratorrb=findViewById(R.id.administratorrb);
        verifylinear=findViewById(R.id.verifylinear);
        reslinkbutton=findViewById(R.id.reslinkbutton);
        conbutton=findViewById(R.id.conbutton);
        mainscreenlinear=findViewById(R.id.mainscreenlinear);

        mainintent.setClass(MainActivity.this,MainActivity.class);
        homeintent.setClass(MainActivity.this,homepage.class);
        depthomeintent.setClass(MainActivity.this,department_home.class );
        adminhomeintent.setClass(MainActivity.this, admin_home.class);

        vmauth=FirebaseAuth.getInstance();


        radiocat.check(R.id.studentrb);
        rgno=0;

        _sud_child_listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot _param1, @Nullable String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                sud.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        udlistmap=new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                udlistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot _param1, @Nullable String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                sud.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        udlistmap=new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                udlistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError _param1) {
                final int _errorCode=_param1.getCode();
                final String _errorMessage=_param1.getMessage();

            }
        };
        sud.addChildEventListener(_sud_child_listener);

        radiocat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int rgid=radiocat.getCheckedRadioButtonId();
                    if(rgid==R.id.studentrb){
                        loginsignupsignuplinear.setVisibility(View.VISIBLE);
                        rgno=0;
                    }
                    else if(rgid==R.id.departmentrb){
                        loginsignupsignuplinear.setVisibility(View.GONE);
                        rgno=1;
                    }
                    else if(rgid==R.id.administratorrb){
                        loginsignupsignuplinear.setVisibility(View.GONE);
                        rgno=2;
                    }
                }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        splashscreenlinear1.setVisibility(View.GONE);
                        loginsignuplinear.setVisibility(View.GONE);
                        loginlinear.setVisibility(View.VISIBLE);
                        signuplinear.setVisibility(View.GONE);
                        verifylinear.setVisibility(View.GONE);
                    }
                });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                splashscreenlinear1.setVisibility(View.GONE);
                loginsignuplinear.setVisibility(View.GONE);
                loginlinear.setVisibility(View.GONE);
                signuplinear.setVisibility(View.VISIBLE);
                verifylinear.setVisibility(View.GONE);
            }
        });
        loginenterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (studentrb.isChecked()) {
                    for (int i101 = 0; i101 < udlistmap.size(); i101++) {
                        if (emailidedit.getText().toString().equals(udlistmap.get(i101).get("email").toString())) {
                            utaflag = 1;
                        }
                    }
                } else if (departmentrb.isChecked()) {
                    for (int i101 = 0; i101 < dudlistmap.size(); i101++) {
                        if (emailidedit.getText().toString().equals(dudlistmap.get(i101).get("email").toString())) {
                            utaflag = 1;
                        }
                    }
                } else {
                    for (int i101 = 0; i101 < audlistmap.size(); i101++) {
                        if (emailidedit.getText().toString().equals(audlistmap.get(i101).get("email").toString())) {
                            utaflag = 1;
                        }
                    }
                }
                if (utaflag == 1) {
                    if (passwordtypeedit.getText().toString().length() > 0 && emailidedit.getText().toString().length() > 0) {
                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Loading");
                        progressDialog.show();
                        vmauth.signInWithEmailAndPassword(emailidedit.getText().toString(), passwordtypeedit.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final boolean _success = task.isSuccessful();
                                final String _errorMessage = task.getException() != null ? task.getException().getMessage() : "";
                                if (_success) {
                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    Authid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if (studentrb.isChecked()) {
                                        loginprefeditor.putString("ut", "s");
                                        loginprefeditor.commit();
                                        progressDialog.dismiss();
                                        startActivityForResult(homeintent, REQUEST_CODE_HOMEPAGE);
                                    } else if (departmentrb.isChecked()) {
                                        loginprefeditor.putString("ut", "d");
                                        loginprefeditor.commit();
                                        progressDialog.dismiss();
                                        startActivityForResult(depthomeintent, RC_DEPARTMENT_HOME);
                                    } else {
                                        loginprefeditor.putString("ut", "a");
                                        loginprefeditor.commit();
                                        progressDialog.dismiss();
                                        startActivityForResult(adminhomeintent, RC_ADMIN_HOME);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Snackbar.make(mainscreenlinear, _errorMessage, Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                else {
                    Snackbar.make(mainscreenlinear,"The user does not exist", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        signupenterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((passwordedit.getText().toString().equals(repasswordedit.getText().toString())) && (fullnameedit.getText().toString()!=null) && (emailedit.getText().toString()!=null)) {
                    final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this );
                    progressDialog.setMessage("Loading");
                    progressDialog.show();
                    vmauth.createUserWithEmailAndPassword(emailedit.getText().toString(),passwordedit.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            final boolean _success=task.isSuccessful();
                            final String _errorMessage=task.getException()!=null?task.getException().getMessage():"";
                            if(_success){
                                FirebaseUser user=vmauth.getCurrentUser();
                                tempmap=new HashMap<>();
                                tempmap.put("fullname",fullnameedit.getText().toString());
                                tempmap.put("phoneno",phoneedit.getText().toString());
                                tempmap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                tempmap.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                tempmap.put("cat","student");
                                tempmap.put("uenable",1);
                                sud.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(tempmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loginprefeditor.putString("ut","s");
                                        loginprefeditor.commit();
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                                        startActivityForResult(homeintent, REQUEST_CODE_HOMEPAGE);
                                    }
                            });
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this,_errorMessage,Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
            }
        });

        reslinkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vmauth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Verification link is sent to email",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        conbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vmauth.getCurrentUser().isEmailVerified()){
                    startActivityForResult(homeintent, REQUEST_CODE_HOMEPAGE);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Email is not verified",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void initializeLogic(){

        loginlinear.setVisibility(View.GONE);
        signuplinear.setVisibility(View.GONE);
        loginsignuplinear.setVisibility(View.GONE);
        verifylinear.setVisibility(View.GONE);

        //Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/smartshopping-c310d.appspot.com/o/Screenshot%20(17).png?alt=media&token=c1b1a776-6e60-4006-b72c-375841b8b474").fitCenter().into(splashimage);
        //centerCrop
        //fitCentre
        timer2=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sud.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                udlistmap=new ArrayList<>();
                                try {
                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                        HashMap<String, Object> _map = _data.getValue(_ind);
                                        udlistmap.add(_map);
                                    }
                                }
                                catch (Exception _e) {
                                    _e.printStackTrace();
                                }
                                dud.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dudlistmap=new ArrayList<>();
                                        try {
                                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                                HashMap<String, Object> _map = _data.getValue(_ind);
                                                dudlistmap.add(_map);
                                            }
                                        }
                                        catch (Exception _e) {
                                            _e.printStackTrace();
                                        }
                                        aud.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                audlistmap=new ArrayList<>();
                                                try {
                                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                                                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                                        HashMap<String, Object> _map = _data.getValue(_ind);
                                                        audlistmap.add(_map);
                                                    }
                                                }
                                                catch (Exception _e) {
                                                    _e.printStackTrace();
                                                }
                                                splashscreenlinear1.setVisibility(View.GONE);

                                                if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
                                                    if(loginpreferences.getString("ut","s" ).equals("s")) {
                                                        startActivityForResult(homeintent, REQUEST_CODE_HOMEPAGE);
                                                    }
                                                    else if(loginpreferences.getString("ut","s" ).equals("d")){
                                                        startActivityForResult(depthomeintent,RC_DEPARTMENT_HOME ,ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                                    }
                                                    else{
                                                        startActivityForResult(adminhomeintent,RC_ADMIN_HOME );
                                                    }
                                                }
                                                else{
                                                    splashscreenlinear1.setVisibility(View.GONE);
                                                    loginlinear.setVisibility(View.GONE);
                                                    signuplinear.setVisibility(View.GONE);
                                                    verifylinear.setVisibility(View.GONE);
                                                    loginsignuplinear.setVisibility(View.VISIBLE);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };
        _timer2.schedule(timer2, 1000);
        timer=new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        splashprogressbar.incrementProgressBy(5);
                    }
                });
            }
        };
        _timer.scheduleAtFixedRate(timer, 0, 600);

    }

}