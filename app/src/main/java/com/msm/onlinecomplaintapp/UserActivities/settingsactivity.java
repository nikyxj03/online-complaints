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
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.UserActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class settingsactivity extends UserActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button archivesbutton;
    private Button notificationsbutton;
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

    private int anaf=0;

    private Users cuUser;

    private AuthCredential vmcredential;
    private FirebaseUser vmauthu;

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
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_NOTIFICATION){
                            notificationintent.putExtra("ac","lo");
                            setResult(RESULT_OK,notificationintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES){
                                archiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,archiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }
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
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(settingsactivity.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading..");

        vmauthu=FirebaseAuth.getInstance().getCurrentUser();

        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        logoutbutton=findViewById(R.id.logoutbutton);
        archivesbutton=findViewById(R.id.archivebutton);
        notificationsbutton=findViewById(R.id.notification_button);

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

        setintents(this);

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

        archivesbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES);
            }
        });

        notificationsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(notificationintent,REQUEST_CODE_NOTIFICATION);
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
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_NOTIFICATION){
                            notificationintent.putExtra("ac","lo");
                            setResult(RESULT_OK,notificationintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES){
                                archiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,archiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }
                        }

                    }
                }
                settingsactivity.this.finish();
            }
        });

        scbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("Updating...");
                cuUser.setFullname(nameedit.getText().toString());
                cuUser.setPhoneno(phonenoedit.getText().toString());
                GlobalApplication.databaseHelper.updateUserData(cuUser, new OnDataUpdatedListener() {
                    @Override
                    public void onDataUploaded(boolean success) {
                        hideProgress();
                        if(success)
                            Toast.makeText(settingsactivity.this,"Details Updated",Toast.LENGTH_LONG).show();
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
                    showProgress("Changing Password...");
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
                                                hideProgress();
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
                                        hideProgress();
                                        Toast.makeText(settingsactivity.this, "new password size must be more than 6", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    hideProgress();
                                    Toast.makeText(settingsactivity.this, "passwords do not match", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                hideProgress();
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

        acchlinear.setVisibility(View.VISIBLE);
        repalinear.setVisibility(View.GONE);

        emailidedit.setFocusable(false);
        emailidedit.setFocusableInTouchMode(false);
        emailidedit.setClickable(false);

        GlobalApplication.databaseHelper.fetchUserData(getCurrentUserId(), new OnDataSFetchListener<Users>() {
            @Override
            public void onDataSFetch(Users users) {
                cuUser=users;
                emailidedit.setText(users.getEmail());
                nameedit.setText(users.getFullname());
                phonenoedit.setText(users.getPhoneno());
                hideProgress();
            }
        });
    }

}
