package com.msm.onlinecomplaintapp.Users.UserActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.msm.onlinecomplaintapp.Common.CloudFunctionHelper;
import com.msm.onlinecomplaintapp.Common.RandomStringBuilder;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Interfaces.OnPosClicked;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.Models.UserQuery;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.msm.onlinecomplaintapp.Users.UserAdapters.UDeptListAdapter;

import java.util.List;

public class NewQueryActivity extends UserActivity {

    private Toolbar toolbar;
    private TextView deptText;
    private TextInputEditText questionText;
    private Button submitButton;

    private Departments selectedDepartment;
    private Users curUser;

    private List<Departments> departmentsList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            NewQueryActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_query);

        toolbar=findViewById(R.id.u_nq_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading..");

        deptText=findViewById(R.id.nq_depstext);
        questionText=findViewById(R.id.nq_question_edit);
        submitButton=findViewById(R.id.nq_submit_button);

        deptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NewQueryActivity.this,R.style.BottomSheetDialog);
                View view1 = bottomSheetDialog.getLayoutInflater().inflate(R.layout.deplistlayout, null);
                bottomSheetDialog.setContentView(view1);
                ListView listView = view1.findViewById(R.id.depsellist);
                TextView dlladmintext = view1.findViewById(R.id.dlldmintext);
                dlladmintext.setVisibility(View.GONE);
                int temppos = -1;
                if(selectedDepartment!=null) {
                    for (int i = 0; i < departmentsList.size(); i++) {
                        if (selectedDepartment.getDid().contains(departmentsList.get(i).getDid()))
                            temppos = i;
                    }
                }
                UDeptListAdapter uDeptListAdapter=new UDeptListAdapter(departmentsList,temppos,NewQueryActivity.this);
                uDeptListAdapter.dissmiss(new OnPosClicked() {
                    @Override
                    public void onSelected(int pos) {
                        selectedDepartment=departmentsList.get(pos);
                        bottomSheetDialog.dismiss();
                        deptText.setText(selectedDepartment.getName());
                    }
                });
                bottomSheetDialog.show();
                listView.setAdapter(uDeptListAdapter);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDepartment!=null && questionText.getText().length()>5){
                    showProgress("Sending Query..");
                    RandomStringBuilder randomStringBuilder=new RandomStringBuilder(NewQueryActivity.this,"qid");
                    randomStringBuilder.getRandomId(new OnDataSFetchListener<String>() {
                        @Override
                        public void onDataSFetch(final String s) {
                            if(s!=null) {
                                UserQuery userQuery = new UserQuery();
                                userQuery.setDeptName(selectedDepartment.getName());
                                userQuery.setDid(selectedDepartment.getDid());
                                userQuery.setQuestion(questionText.getText().toString());
                                userQuery.setTimestamp(Timestamp.now());
                                userQuery.setUid(curUser.getUid());
                                userQuery.setUserName(curUser.getFullname());
                                userQuery.setQid(s);
                                Toast.makeText(NewQueryActivity.this,s,Toast.LENGTH_LONG).show();
                                GlobalApplication.databaseHelper.updateQuery(userQuery, new OnDataUpdatedListener() {
                                    @Override
                                    public void onDataUploaded(boolean success) {
                                        hideProgress();
                                        if (success) {
                                            Toast.makeText(NewQueryActivity.this, "Success", Toast.LENGTH_LONG).show();
                                            NewQueryActivity.this.finish();
                                        }
                                        else
                                            Toast.makeText(NewQueryActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else
                                hideProgress();
                        }
                    });
                }
            }
        });

        GlobalApplication.databaseHelper.getDepartmentsList(new OnDataFetchListener<Departments>() {
            @Override
            public void onDataFetched(List<Departments> departments) {
                departmentsList=departments;
                GlobalApplication.databaseHelper.fetchUserData(getCurrentUserId(), new OnDataSFetchListener<Users>() {
                    @Override
                    public void onDataSFetch(Users users) {
                        curUser=users;
                        hideProgress();
                    }
                });
            }
        });

    }
}
