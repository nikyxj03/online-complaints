package com.msm.onlinecomplaintapp.DepartmentActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.DepartmentAdapters.DAraayAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class deptarchives extends DepartmentActivity {

    private static final int REQUEST_CODE_HOMEPAGE_D = 21;
    private static final int REQUEST_CODE_DeptCOMPLAINT_D = 22;
    private static final int REQUEST_CODE_SETTINGS_D = 23;
    private static final int REQUEST_CODE_MAIN_D = 0;
    private static final int REQUEST_CODE_ARCHIVES_D = 24;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;


    private Intent mainintent = new Intent();
    private Intent deptcomplaintintent = new Intent();
    private Intent settingsintent = new Intent();
    private Intent tcdintent = new Intent();
    private Intent archiveintent = new Intent();

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;

    private ListView adminarchivelistview;

    private DAraayAdapter dAraayAdapter;

    private List<Complaint> archivecomplaintlist = new ArrayList<>();
    private List<String> deptlist = new ArrayList<>();
    private List<String> seldeptist = new ArrayList<>();

    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference acffb = _database.getReference("complaint");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
        if (resultCode == RESULT_OK) {
            if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                settingsintent.putExtra("ac", "lo");
                setResult(RESULT_OK, settingsintent);
            } else {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                    deptcomplaintintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, deptcomplaintintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES_D) {

                    } else {
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN_D) {
                            mainintent.putExtra("key1", "logout");
                            setResult(RESULT_OK, mainintent);
                        }
                    }

                }
            }
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptarchives);

        adminarchivelistview = findViewById(R.id.adminarchivelistview);

        GlobalApplication.databaseHelper.getPublicArchivedComplaints(new OnDataFetchListener<Complaint>() {
            @Override
            public void onDataFetched(List<Complaint> complaints) {
                archivecomplaintlist=new ArrayList<>();
                archivecomplaintlist=complaints;
                dAraayAdapter=new DAraayAdapter(deptarchives.this, R.layout.deptcompistcustom, archivecomplaintlist, 0);
                adminarchivelistview.setAdapter(dAraayAdapter);
            }
        });

       /* acffb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                archivecomplaintlist = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        Complaint complaint = new Complaint(_map);
                        archivecomplaintlist.add(complaint);
                        Log.i("abcde", complaint.getTitle());
                    }
                } catch (Exception _e) {
                    _e.printStackTrace();
                }

                dAraayAdapter=new DAraayAdapter(deptarchives.this, R.layout.deptcompistcustom, archivecomplaintlist, 0);
                adminarchivelistview.setAdapter(dAraayAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }

}
