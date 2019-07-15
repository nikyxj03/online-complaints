package com.msm.onlinecomplaintapp.Users.UserActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.msm.onlinecomplaintapp.Common.ImageConverter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.BooleanListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class settingsactivity extends UserActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Toolbar toolbar;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button archivesbutton;
    private Button notificationsbutton;
    private Button mycomplaintsbutton;
    private Button querybutton;

    private TextView profileEmailText;
    private TextView profileNameText;
    private CircleImageView profilePhoto;

    private RelativeLayout repalinear;
    private LinearLayout acchlinear;
    private RelativeLayout otplinear;
    private TextInputEditText npedit;
    private TextInputEditText oldpedit;
    private TextInputEditText rnpedit;
    private EditText nameedit;
    private TextView emailidedit;
    private EditText phonenoedit;
    private Button repabutton;
    private Button scbutton;
    private Button rpbutton;
    private Button rpabackbutton;
    private Button otpresendbutton;
    private OtpView otpView;
    private Button otpbackbutton;
    private TextView otpcountdowntext;
    private CircleImageView profileimage;

    private CountDownTimer countDownTimer;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyPhoneNumberlistener;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthCredential phoneAuthCredential;
    private String phoneVerificationId;

    private int anaf=0;

    private Users cuUser;

    private FirebaseAuth vmauth;

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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_QUERY){
                                    queryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,queryintent);
                                }
                                else {
                                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                        mainintent.putExtra("key1", "logout");
                                        setResult(RESULT_OK, mainintent);
                                    }
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
            if(anaf==2){
                anaf=0;
                acchlinear.setVisibility(View.VISIBLE);
                otplinear.setVisibility(View.GONE);
            }
            else if(anaf==1)
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

        toolbar=findViewById(R.id.u_se_toolbar);
        setSupportActionBar(toolbar);

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(settingsactivity.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading..");

        vmauthu=FirebaseAuth.getInstance().getCurrentUser();

        profileEmailText=findViewById(R.id.user_profile_email);
        profileNameText=findViewById(R.id.user_profile_name);
        profilePhoto=findViewById(R.id.user_profile_photo);

        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        logoutbutton=findViewById(R.id.logoutbutton);
        archivesbutton=findViewById(R.id.archivebutton);
        querybutton=findViewById(R.id.myqueriesbutton);
        notificationsbutton=findViewById(R.id.notification_button);

        repalinear=findViewById(R.id.repalinear);
        acchlinear=findViewById(R.id.acchlinear);
        emailidedit=findViewById(R.id.emailidtext);
        nameedit=findViewById(R.id.nameedit);
        phonenoedit=findViewById(R.id.phonenoedit);
        oldpedit=findViewById(R.id.user_old_pswd_edit);
        npedit=findViewById(R.id.user_new_pswd_edit);
        rnpedit=findViewById(R.id.user_re_new_pswd_edit);
        repabutton=findViewById(R.id.user_settings_repabutton);
        rpbutton=findViewById(R.id.rpbutton);
        scbutton=findViewById(R.id.scbutton);
        rpabackbutton=findViewById(R.id.user_pwd_back_button);
        otpbackbutton=findViewById(R.id.user_settings_otpbackbutton);
        otpcountdowntext=findViewById(R.id.user_settings_otpchronometer);
        otplinear=findViewById(R.id.user_settings_otp_layout);
        otpresendbutton=findViewById(R.id.user_settings_resendotpbutton);
        otpView=findViewById(R.id.user_settings_otp_view);
        profileimage=findViewById(R.id.user_settings_profile_photo);

        setintents(this);

        vmauth=FirebaseAuth.getInstance();

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

        querybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(queryintent,REQUEST_CODE_QUERY);
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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_QUERY){
                                    queryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,queryintent);
                                }
                                else {
                                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                        mainintent.putExtra("key1", "logout");
                                        setResult(RESULT_OK, mainintent);
                                    }
                                }
                            }
                        }

                    }
                }
                settingsactivity.this.finish();
            }
        });

        GlobalApplication.databaseHelper.fetchUserData(getCurrentUserId(), new OnDataSFetchListener<Users>() {
            @Override
            public void onDataSFetch(Users users) {
                profileEmailText.setText(users.getEmail());
                profileNameText.setText(users.getFullname());
                if(users.getProfilePhoto()!=null)
                    Glide.with(settingsactivity.this).asBitmap().load(users.getProfilePhoto()).placeholder(R.mipmap.ananymous_person_round).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            profilePhoto.setImageBitmap(ImageConverter.getRoundedCornerBitmap(resource,100));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            }
        });

        countDownTimer=new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                otpcountdowntext.setText("OTP expires in "+String.valueOf((int)millisUntilFinished/1000)+" sec");
            }

            @Override
            public void onFinish() {
                otpcountdowntext.setVisibility(View.GONE);
                otpresendbutton.setVisibility(View.VISIBLE);
            }
        };


        scbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cupho;
                if(cuUser.getPhoneno()==null)
                    cupho="";
                else {
                    cupho=cuUser.getPhoneno();
                }

                if(!phonenoedit.getText().toString().equals(cupho)){
                    if(nameedit.getText().toString().length()!=0) {
                        if(phonenoedit.getText().toString().length()==10) {
                            showProgress("Verifying User..");
                            GlobalApplication.databaseHelper.checkifUserExistsByNumber(phonenoedit.getText().toString(), new BooleanListener() {
                                @Override
                                public void booleanResponse(boolean response) {
                                    hideProgress();
                                    if (response)
                                        Toast.makeText(settingsactivity.this, "The number is linked with another account", Toast.LENGTH_LONG).show();
                                    else {
                                        anaf = 2;
                                        acchlinear.setVisibility(View.GONE);
                                        otplinear.setVisibility(View.VISIBLE);
                                        showProgress("Loading..");
                                        sendOtp(null, phonenoedit.getText().toString());
                                    }
                                }
                            });
                        }
                        else if(phonenoedit.getText().toString().length()==0){
                            unlinkphone(new OnDataUpdatedListener() {
                                @Override
                                public void onDataUploaded(boolean success) {
                                    if(success)
                                    updateUser();
                                }
                            });
                        }
                        else {
                            phonenoedit.setError("Invalid Number");
                        }
                    }
                    else {
                        nameedit.setError("Invalid Name");
                    }
                }
                else if(!nameedit.getText().toString().equals(cuUser.getFullname())){
                    updateUser();
                }
                else {
                    Toast.makeText(settingsactivity.this,"No Changes",Toast.LENGTH_LONG).show();
                }
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

        otpbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                phoneAuthCredential=PhoneAuthProvider.getCredential(phoneVerificationId,otp);
                changeNumber(phoneAuthCredential, new BooleanListener() {
                    @Override
                    public void booleanResponse(boolean response) {
                        if(response){
                            if(cuUser.getPhoneno()==null || cuUser.getPhoneno().equals("")){
                                showProgress("Updating number");
                                addnumber(new OnDataUpdatedListener() {
                                    @Override
                                    public void onDataUploaded(boolean success) {
                                        hideProgress();
                                        if (success)
                                            updateUser();
                                    }
                                });
                            }
                            else {
                                showProgress("Updating Number");
                                removenumber(new OnDataUpdatedListener() {
                                    @Override
                                    public void onDataUploaded(boolean success) {
                                        if (success)
                                            addnumber(new OnDataUpdatedListener() {
                                                @Override
                                                public void onDataUploaded(boolean success) {
                                                    hideProgress();
                                                    if(success)
                                                        updateUser();
                                                }
                                            });
                                        else
                                            hideProgress();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });


        otpresendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(forceResendingToken,phonenoedit.getText().toString());
            }
        });

        verifyPhoneNumberlistener=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.i("login","1");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideProgress();
                Toast.makeText(settingsactivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                otpcountdowntext.setVisibility(View.VISIBLE);
                otpresendbutton.setVisibility(View.GONE);
                phoneVerificationId=s;
                settingsactivity.this.forceResendingToken=forceResendingToken;
                countDownTimer.start();
                hideProgress();
            }


        };

        acchlinear.setVisibility(View.VISIBLE);
        repalinear.setVisibility(View.GONE);

        GlobalApplication.databaseHelper.fetchUserData(getCurrentUserId(), new OnDataSFetchListener<Users>() {
            @Override
            public void onDataSFetch(Users users) {
                cuUser=users;
                emailidedit.setText(users.getEmail());
                nameedit.setText(users.getFullname());
                phonenoedit.setText(users.getPhoneno());
                Glide.with(settingsactivity.this).asBitmap().load(cuUser.getProfilePhoto()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        profileimage.setImageBitmap(ImageConverter.getRoundedCornerBitmap(resource,100));
                        hideProgress();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        hideProgress();
                    }
                });

            }
        });
    }

    public void sendOtp(PhoneAuthProvider.ForceResendingToken s,String phoneNumber){
        showProgress("Loading...");
        if(s==null)
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phoneNumber,60, TimeUnit.SECONDS,this,verifyPhoneNumberlistener);
        else
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phoneNumber,60, TimeUnit.SECONDS,this,verifyPhoneNumberlistener,s);
    }

    public void changeNumber(PhoneAuthCredential phoneAuthCredential,final BooleanListener booleanListener){
        vmauth.getCurrentUser().linkWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onBackPressed();
                if(task.isSuccessful()){
                    cuUser.setPhoneno(phonenoedit.getText().toString());
                    Toast.makeText(settingsactivity.this,"Successful",Toast.LENGTH_LONG).show();
                    booleanListener.booleanResponse(true);
                }
                else {
                    phonenoedit.setText(cuUser.getPhoneno());
                    Toast.makeText(settingsactivity.this,"Failed",Toast.LENGTH_LONG).show();
                    booleanListener.booleanResponse(false);
                }
            }
        });
    }

    public void updateUser(){
        showProgress("Updating...");

        cuUser.setFullname(nameedit.getText().toString());
        cuUser.setPhoneno(phonenoedit.getText().toString());
        GlobalApplication.databaseHelper.updateUserData(cuUser, new OnDataUpdatedListener() {
            @Override
            public void onDataUploaded(boolean success) {
                hideProgress();
                if (success)
                    Toast.makeText(settingsactivity.this, "Details Updated", Toast.LENGTH_LONG).show();
            }
        });
        vmauth=FirebaseAuth.getInstance();
    }

    public void unlinkphone(final OnDataUpdatedListener onDataUpdatedListener){
        showProgress("Removing Phone Number");
        vmauth.getCurrentUser().unlink(PhoneAuthProvider.PROVIDER_ID);
        removenumber(new OnDataUpdatedListener() {
            @Override
            public void onDataUploaded(boolean success) {
                hideProgress();
                onDataUpdatedListener.onDataUploaded(success);
            }
        });
    }
    public void removenumber(final OnDataUpdatedListener onDataUpdatedListener){
        GlobalApplication.databaseHelper.deleteUserNumber(cuUser.getPhoneno(), new OnDataUpdatedListener() {
            @Override
            public void onDataUploaded(boolean success) {
                onDataUpdatedListener.onDataUploaded(success);
            }
        });
    }

    public void addnumber(final OnDataUpdatedListener onDataUpdatedListener){
        GlobalApplication.databaseHelper.addUserNumber(phonenoedit.getText().toString(), new OnDataUpdatedListener() {
            @Override
            public void onDataUploaded(boolean success) {
                onDataUpdatedListener.onDataUploaded(success);
            }
        });
    }

}
