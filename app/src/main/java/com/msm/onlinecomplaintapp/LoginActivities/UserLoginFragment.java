package com.msm.onlinecomplaintapp.LoginActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.BooleanListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Interfaces.OnTaskCompleteListener;
import com.msm.onlinecomplaintapp.Interfaces.PageLockListener;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.UserActivities.homepage;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


public class UserLoginFragment extends Fragment {

    private Context context;
    private Activity activity;
    private ProgressDialog progressDialog;

    private RelativeLayout userloginlayout;
    private RelativeLayout userphnolayout;
    private RelativeLayout userotplayout;
    private OtpView otpView;
    private SignInButton signInButton;
    private Button userphonenoskipbutton;
    private Button userphonenoconfirmbutton;
    private TextInputEditText userphonenoedit;
    private TextView userotpchronometer;
    private Button userresendotpbutton;
    private Button userotpbackbutton;
    private Button userotpskipbutton;
    private Button phoneSignInButton;
    private Button userphonenobackbutton;

    private CountDownTimer countDownTimer;

    private static final int REQUEST_CODE_HOMEPAGE=11;

    private final static int G_RC_SIGN_IN=1001;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount account;

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();
    private FirebaseUser user;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyPhoneNumberlistener;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthCredential phoneAuthCredential;

    private SharedPreferences loginpreferences;
    private SharedPreferences.Editor loginpreferenceseditor;

    private String phoneVerificationId;

    private Users curUser;

    private int loginType=0;

    public static UserLoginFragment userLoginFragment;
    public static PageLockListener mpageLockListener;

    public UserLoginFragment() {
        // Required empty public constructor
    }

    public static UserLoginFragment getInstance(PageLockListener pageLockListener) {
        if (userLoginFragment==null) {
            userLoginFragment = new UserLoginFragment();
            mpageLockListener=pageLockListener;
        }
        return userLoginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        activity=getActivity();
        loginpreferences=getContext().getSharedPreferences("UserPreferences",MODE_PRIVATE);
        loginpreferenceseditor=loginpreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_login_fragment,container,false);
        otpView = view.findViewById(R.id.otp_view);
        signInButton=view.findViewById(R.id.user_google_login_button);
        phoneSignInButton=view.findViewById(R.id.user_phone_login_button);
        userloginlayout=view.findViewById(R.id.user_login_layout);
        userphnolayout=view.findViewById(R.id.user_phone_number_layout);
        userotplayout=view.findViewById(R.id.user_login_otp_layout);
        userphonenoskipbutton=view.findViewById(R.id.user_phoneno_skip_button);
        userphonenobackbutton=view.findViewById(R.id.userphonenobackbutton);
        userphonenoconfirmbutton=view.findViewById(R.id.user_phoneno_confirm_button);
        userphonenoedit=view.findViewById(R.id.user_login_phoneno_edit);
        userotpbackbutton=view.findViewById(R.id.userotpbackbutton);
        userotpskipbutton=view.findViewById(R.id.userotpskipbutton);
        userotpchronometer=view.findViewById(R.id.userotpchronometer);
        userresendotpbutton=view.findViewById(R.id.userresendotpbutton);

        setupGoogleSignIn();

        vmauth.setLanguageCode("eng");

        countDownTimer=new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                userotpchronometer.setText("OTP expires in "+String.valueOf((int)millisUntilFinished/1000)+" sec");
            }

            @Override
            public void onFinish() {
                userotpchronometer.setVisibility(View.GONE);
                userresendotpbutton.setVisibility(View.VISIBLE);
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpageLockListener.onStateChanged(true);
                loginType=0;
                userphonenoskipbutton.setVisibility(View.VISIBLE);
                userphonenobackbutton.setVisibility(View.GONE);
                GoogleSignIn();
            }
        });

        userphonenoskipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType=1;
                userphonenoedit.setText("");
                userphonenoedit.clearFocus();
                showProgress("Signing In..");
                authAndLink(loginType, new BooleanListener() {
                    @Override
                    public void booleanResponse(boolean response) {
                        hideProgress();
                    }
                });
            }
        });

        userphonenobackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpageLockListener.onStateChanged(false);
                loginType=0;
                userloginlayout.setVisibility(View.VISIBLE);
                userphnolayout.setVisibility(View.GONE);
                userotplayout.setVisibility(View.GONE);
                userphonenoedit.setText("");
                userphonenoedit.clearFocus();
                hideKeyboard();
            }
        });

        phoneSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpageLockListener.onStateChanged(true);
                loginType=2;
                userloginlayout.setVisibility(View.GONE);
                userphnolayout.setVisibility(View.VISIBLE);
                userotplayout.setVisibility(View.GONE);
                userphonenoskipbutton.setVisibility(View.GONE);
                userphonenobackbutton.setVisibility(View.VISIBLE);
            }
        });

        userphonenoconfirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userphonenoedit.getText().toString().length()==10){
                    showProgress("Validating User..");
                    GlobalApplication.databaseHelper.checkifUserExistsByNumber(userphonenoedit.getText().toString(), new BooleanListener() {
                        @Override
                        public void booleanResponse(boolean response) {
                            hideProgress();
                            if (loginType == 0) {
                                if (response)
                                    Toast.makeText(context, "The number is linked with another account", Toast.LENGTH_LONG).show();
                                else
                                    sendOtp(null);
                            } else {
                                if (response)
                                    sendOtp(null);
                                else
                                    Toast.makeText(context, "The User does not exist. The User must register with the google account to login.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                otpView.clearFocus();
                showProgress("Signing In...");
                phoneAuthCredential=PhoneAuthProvider.getCredential(phoneVerificationId,otp);
                authAndLink(loginType, new BooleanListener() {
                    @Override
                    public void booleanResponse(boolean response) {
                       hideProgress();
                    }
                });
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
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                userphnolayout.setVisibility(View.GONE);
                userotplayout.setVisibility(View.VISIBLE);
                userotpchronometer.setVisibility(View.VISIBLE);
                userresendotpbutton.setVisibility(View.GONE);
                phoneVerificationId=s;
                countDownTimer.start();
                hideProgress();
            }


        };

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == G_RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
                showProgress("Loading..");
                authAndLink(loginType, new BooleanListener() {
                    @Override
                    public void booleanResponse(boolean response) {
                        if(response){
                            updateUI(user);
                        }
                        else {
                            userloginlayout.setVisibility(View.GONE);
                            userphnolayout.setVisibility(View.VISIBLE);
                            userotplayout.setVisibility(View.GONE);
                        }
                        hideProgress();
                    }
                });
            } catch (ApiException e) {
                mpageLockListener.onStateChanged(false);
                Log.w("gsi", "Google sign in failed", e);
            }
        }
    }

    public void GoogleSignIn(){
        Intent googlesigninintent=googleSignInClient.getSignInIntent();
        startActivityForResult(googlesigninintent,G_RC_SIGN_IN );
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, final OnTaskCompleteListener onTaskCompleteListener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        vmauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("gsi", "signInWithCredential:success");
                    user = task.getResult().getUser();
                } else {
                    Log.w("gsi", "signInWithCredential:failure", task.getException());
                    user=null;
                }
                onTaskCompleteListener.onTaskCompleted();
            }
        });
    }

    public void firebaseAuthWithPhone(PhoneAuthCredential phoneAuthCredential,final OnTaskCompleteListener onTaskCompleteListener){
        vmauth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("gsi", "signInWithCredential:success");
                    user = task.getResult().getUser();
                } else {
                    Log.w("gsi", "signInWithCredential:failure", task.getException());
                    user=null;
                }
                onTaskCompleteListener.onTaskCompleted();
            }
        });
    }

    public void sendOtp(PhoneAuthProvider.ForceResendingToken s){
        showProgress("Loading...");
        if(s==null)
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+userphonenoedit.getText().toString(),60, TimeUnit.SECONDS,(LoginActivity)activity,verifyPhoneNumberlistener);
        else
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+userphonenoedit.getText().toString(),60, TimeUnit.SECONDS,(LoginActivity)activity,verifyPhoneNumberlistener,s);
    }


    public void showProgress(String message) {
        hideProgress();
        progressDialog = new ProgressDialog(context);
        progressDialog.getWindow().getAttributes().windowAnimations=R.style.DialogSlide;
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void setupGoogleSignIn(){
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("1006518179317-taj9gl946jjoulg0ft4ofuosi8b8t61h.apps.googleusercontent.com").requestEmail().build();

        googleSignInClient=GoogleSignIn.getClient(context,gso);

    }

    public void authAndLink(final int loginType, final BooleanListener booleanListener){
        if(loginType==0) {
            if (user == null){
                firebaseAuthWithGoogle(account, new OnTaskCompleteListener() {
                    @Override
                    public void onTaskCompleted() {
                        GlobalApplication.databaseHelper.checkifUserExists(user.getUid(), new BooleanListener() {
                            @Override
                            public void booleanResponse(boolean response) {
                                booleanListener.booleanResponse(response);
                            }
                        });
                    }
                });
            }
            else{
                user.linkWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        createUser(loginType);
                        GlobalApplication.databaseHelper.updateUserData(curUser, new OnDataUpdatedListener() {
                            @Override
                            public void onDataUploaded(boolean success) {
                                if(success){
                                    GlobalApplication.databaseHelper.addUserNumber(userphonenoedit.getText().toString(), new OnDataUpdatedListener() {
                                        @Override
                                        public void onDataUploaded(boolean success) {
                                            if(success){
                                                updateUI(user);
                                                booleanListener.booleanResponse(true);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        }
        else if (loginType==1){
            createUser(loginType);
            GlobalApplication.databaseHelper.updateUserData(curUser, new OnDataUpdatedListener() {
                @Override
                public void onDataUploaded(boolean success) {
                    if(success){
                        updateUI(user);
                        booleanListener.booleanResponse(true);
                    }
                }
            });
        }
        else if(loginType==2){
            firebaseAuthWithPhone(phoneAuthCredential, new OnTaskCompleteListener() {
                @Override
                public void onTaskCompleted() {
                    updateUI(user);
                    booleanListener.booleanResponse(true);

                }
            });
        }
    }

    public void createUser(int loginType){
        curUser=new Users();
        curUser.setFullname(user.getDisplayName());
        curUser.setCat("student");
        curUser.setEmail(user.getEmail());
        curUser.setUenable("1");
        curUser.setUid(user.getUid());
        if(loginType==0)
            curUser.setPhoneno(userphonenoedit.getText().toString());
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void updateUI(FirebaseUser user){
        if(user!=null) {
            userloginlayout.setVisibility(View.VISIBLE);
            loginpreferenceseditor.putInt("type",0).commit();
            Toast.makeText(context,user.getEmail(),Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.setClass(getContext(), homepage.class);
            startActivityForResult(intent,REQUEST_CODE_HOMEPAGE);
        }
    }
}
