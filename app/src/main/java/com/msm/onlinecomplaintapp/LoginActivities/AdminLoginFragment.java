package com.msm.onlinecomplaintapp.LoginActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.msm.onlinecomplaintapp.DepartmentActivities.department_home;
import com.msm.onlinecomplaintapp.R;

import static android.content.Context.MODE_PRIVATE;


public class AdminLoginFragment extends Fragment {

    private Context context;
    private Activity activity;

    private TextInputEditText adminloginemailedit;
    private TextInputEditText adminloginpswdedit;
    private Button adminloginbutton;

    private FirebaseAuth vmauth;

    private ProgressDialog progressDialog;

    private static AdminLoginFragment adminLoginFragment;

    private static final int RC_ADMIN_HOME=31;

    private SharedPreferences loginpreferences;
    private SharedPreferences.Editor loginpreferenceseditor;

    public AdminLoginFragment() {
        // Required empty public constructor
    }

    public static AdminLoginFragment getInstance() {
        if(adminLoginFragment==null) {
            adminLoginFragment = new AdminLoginFragment();
        }
        return adminLoginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_login_fragment,container,false);
        adminloginemailedit=view.findViewById(R.id.admin_login_emaid_edit);
        adminloginpswdedit=view.findViewById(R.id.admin_login_password_edit);
        adminloginbutton=view.findViewById(R.id.admin_login_button);

        vmauth=FirebaseAuth.getInstance();

        loginpreferences=getContext().getSharedPreferences("UserPreferences",MODE_PRIVATE);
        loginpreferenceseditor=loginpreferences.edit();

        adminloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminloginemailedit.getText().toString().length()>0){
                    if(adminloginpswdedit.getText().toString().length()>6){
                        showProgress("Signing In..");
                        vmauth.signInWithEmailAndPassword(adminloginemailedit.getText().toString(),adminloginpswdedit.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        hideProgress();
                                        if(task.isSuccessful()){
                                            loginpreferenceseditor.putInt("type",2).commit();
                                            adminloginemailedit.setText("");
                                            adminloginpswdedit.setText("");
                                            Intent intent=new Intent();
                                            intent.setClass(getContext(), department_home.class);
                                            startActivityForResult(intent,RC_ADMIN_HOME);
                                        }
                                        else {
                                            Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                    else {
                        adminloginpswdedit.setError("Password size must be greater than 6");
                    }
                }
                else {
                    adminloginemailedit.setError("Email must not be empty");
                }
            }
        });
        return view;
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


}
