package com.msm.onlinecomplaintapp.Users.UserActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Models.UserQuery;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.msm.onlinecomplaintapp.Users.UserAdapters.UserQueryListAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserQueryActivity extends UserActivity {

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

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private UserQueryListAdapter userQueryListAdapter;

    @Override
    public void onBackPressed() {
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
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
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS){
                            settingsintent.putExtra("ac","lo");
                            setResult(RESULT_OK,settingsintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES){
                                archiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,archiveintent);
                            }
                            else {
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_NOTIFICATION){
                                    notificationintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,notificationintent);
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
                UserQueryActivity.this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_query);

        toolbar=findViewById(R.id.u_q_toolbar);
        setSupportActionBar(toolbar);

        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(UserQueryActivity.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileEmailText=findViewById(R.id.user_profile_email);
        profileNameText=findViewById(R.id.user_profile_name);
        profilePhoto=findViewById(R.id.user_profile_photo);

        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        archivesbutton=findViewById(R.id.archivebutton);
        notificationsbutton=findViewById(R.id.notification_button);
        logoutbutton=findViewById(R.id.logoutbutton);
        querybutton=findViewById(R.id.myqueriesbutton);

        recyclerView=findViewById(R.id.uq_listview);
        refreshLayout=findViewById(R.id.uq_refresh);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setintents(this);

        mycomplaintsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mycomplaintintent,REQUEST_CODE_MYCOMPLAINT);
            }
        });

        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS);
            }
        });

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(homeintent,REQUEST_CODE_HOMEPAGE);
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
                startActivityForResult(notificationintent, REQUEST_CODE_QUERY);
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_SETTINGS) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MYCOMPLAINT) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,mycomplaintintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_ARCHIVES){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent );
                        }
                        else{
                            if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_HOMEPAGE){
                                homeintent.putExtra("ac","lo" );
                                setResult(RESULT_OK,homeintent );
                            }
                            else{
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_NOTIFICATION){
                                    notificationintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,notificationintent);
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
                UserQueryActivity.this.finish();
            }
        });

        userQueryListAdapter=new UserQueryListAdapter(this);
        recyclerView.setAdapter(userQueryListAdapter);

        updateData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

    }

    public void updateData(){
        GlobalApplication.databaseHelper.getUserQueries(getCurrentUserId(), new OnDataFetchListener<UserQuery>() {
            @Override
            public void onDataFetched(List<UserQuery> userQueries) {
                userQueryListAdapter.setList(userQueries);
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
