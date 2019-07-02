package com.msm.onlinecomplaintapp.LoginActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


public class DeptLoginFragment extends Fragment {

    private Context context;
    private Activity activity;

    private TextInputEditText deptloginemailedit;
    private TextInputEditText deptloginpswdedit;
    private Button deptloginbutton;

    private FirebaseAuth vmauth;

    private ProgressDialog progressDialog;

    private static DeptLoginFragment deptLoginFragment;

    private static final int RC_DEPARTMENT_HOME=21;

    private SharedPreferences loginpreferences;
    private SharedPreferences.Editor loginpreferenceseditor;

    public DeptLoginFragment() {
        // Required empty public constructor
    }

    public static DeptLoginFragment getInstance() {
        if(deptLoginFragment==null) {
            deptLoginFragment = new DeptLoginFragment();
        }
        return deptLoginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.department_login_fragment,container,false);
        deptloginemailedit=view.findViewById(R.id.dept_login_emaid_edit);
        deptloginpswdedit=view.findViewById(R.id.dept_login_password_edit);
        deptloginbutton=view.findViewById(R.id.dept_login_button);

        loginpreferences=getContext().getSharedPreferences("UserPreferences",MODE_PRIVATE);
        loginpreferenceseditor=loginpreferences.edit();

        vmauth=FirebaseAuth.getInstance();

        deptloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deptloginemailedit.getText().toString().length()>0){
                    if(deptloginpswdedit.getText().toString().length()>6){
                        showProgress("Signing In..");
                        vmauth.signInWithEmailAndPassword(deptloginemailedit.getText().toString(),deptloginpswdedit.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        hideProgress();
                                        if(task.isSuccessful()){
                                            loginpreferenceseditor.putInt("type",1).commit();
                                            deptloginemailedit.setText("");
                                            deptloginpswdedit.setText("");
                                            Intent intent=new Intent();
                                            intent.setClass(getContext(), department_home.class);
                                            startActivityForResult(intent,RC_DEPARTMENT_HOME);
                                        }
                                        else {
                                            Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                    else {
                        deptloginpswdedit.setError("Password size must be greater than 6");
                    }
                }
                else {
                    deptloginemailedit.setError("Email must not be empty");
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
