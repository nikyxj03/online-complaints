package com.msm.onlinecomplaintapp.DepartmentActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.DepartmentAdapters.DeptPagerAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnSortChange;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class deptcomplaints extends DepartmentActivity {


    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    PopupMenu popup;

    private Calendar calendar=Calendar.getInstance();

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button archivebutton1;
    private Button deptarchivesbutton1;
    private Button deptcomplaintsbutton1;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private DeptPagerAdapter deptPagerAdapter;

    private Button rdreplybutton;
    private EditText rdreplyedit;

    private ListView depsellist;

    private Button dardeletebutton;
    private Button darblockbutton;
    private Button dararchivebutton;

    private OnSortChange onSortChange1;
    private OnSortChange onSortChange0;

    private ArrayList<HashMap<String,Object>> udlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> deptdatalistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> deptcomplaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> compreplylistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> tempmap2=new HashMap<>();
    private ArrayList<String> sortcidlist=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> deptlistmap=new ArrayList<>();
    private ArrayList<String> compdeptlist=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> adminrequestlistmap=new ArrayList<>();

    private int smf=0;
    private String uid="";
    private String did="";
    private String tempdepttext="";
    private String temprid="";

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference cffb=_database.getReference("complaint");
    private DatabaseReference dudfb=_database.getReference("deptuserdata");
    private DatabaseReference crfb=_database.getReference("compreply");
    private DatabaseReference dfb=_database.getReference("department");
    private DatabaseReference arfb=_database.getReference("adminrequests");

    private FirebaseStorage vmfbs=FirebaseStorage.getInstance();
    private StorageReference compimgfbs=vmfbs.getReference();
    private StorageReference storageReference;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(true) {
            if (resultCode==RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_D) {
                    depthomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,depthomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                        settingsintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,settingsintent);
                    }
                    else{
                        if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_ARCHIVES_D){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_D)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }

                        }

                    }
                }
                deptcomplaints.this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.sortbutton_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId()==R.id.item1){
            smf=0;
            onSortChange0.onSortChanged(smf);
        }
        if(item.getItemId()==R.id.item2){
            smf=1;
            onSortChange1.onSortChanged(smf);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptcomplaints);
        Initialize();
        InitializeLogic();
    }

    public void setSortListener_ZERO(Context context, OnSortChange onSortChange){
        this.onSortChange0=onSortChange;
    }

    public OnSortChange getSortListener_ZERO(){
        return onSortChange0;
    }

    public void setSortListener_ONE(Context context,OnSortChange onSortChange){
        Toast.makeText(context,"1234",Toast.LENGTH_LONG).show();
        this.onSortChange1=onSortChange;
    }

    public OnSortChange getSortListener_ONE(){
        return onSortChange1;
    }

    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(deptcomplaints.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading");

        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivesbutton1=findViewById(R.id.deptarchivebutton1);
        archivebutton1=findViewById(R.id.archivebutton1);

        viewPager=findViewById(R.id.dept_complaints_pager);
        tabLayout=findViewById(R.id.dept_complaints_tab);

        setintents(this);

        homebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(depthomeintent,REQUEST_CODE_HOMEPAGE_D);
            }
        });

        settingsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_D);
            }
        });

        archivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES_D );
            }
        });

        deptarchivesbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptarchiveintent,REQUEST_CODE_DEPTARCHIVES_D);
            }
        });

        logoutbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_D) {
                    depthomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,depthomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                        settingsintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,settingsintent);
                    }
                    else{
                        if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_ARCHIVES_D){
                            archiveintent.putExtra("ac","lo" );
                            setResult(RESULT_OK,archiveintent);
                        }
                        else {
                            if(getIntent().getIntExtra("pp",0)==REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_D)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }

                        }

                    }
                }
                deptcomplaints.this.finish();
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Registered"));
        tabLayout.addTab(tabLayout.newTab().setText("Watching"));

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        GlobalApplication.databaseHelper.fetchDeptUserData(getCurrentUserId(), new OnDataSFetchListener<DeptUsers>() {
            @Override
            public void onDataSFetch(DeptUsers deptUsers) {
                if(deptUsers!=null){
                    did=deptUsers.getDept();
                    deptPagerAdapter =new DeptPagerAdapter(getSupportFragmentManager(),did,0,0);
                    viewPager.setAdapter(deptPagerAdapter);
                }
                hideProgress();
            }
        });

        ChildEventListener crfb_listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                crfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        compreplylistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                compreplylistmap.add(_map);
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
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                crfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        compreplylistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                compreplylistmap.add(_map);
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
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                crfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        compreplylistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                compreplylistmap.add(_map);
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
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        crfb.addChildEventListener(crfb_listener);

        ChildEventListener arfb_listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adminrequestlistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                adminrequestlistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        int flag1=0;
                        do {
                            flag1=0;
                            for (int i1 = 0; i1 < adminrequestlistmap.size(); i1++) {
                                if (adminrequestlistmap.get(i1).get("rid").toString().equals(temprid)) {
                                    flag1 = 1;
                                }
                            }
                            if(flag1==1){
                                temprid=randomAlphaNumeric(8);
                            }
                        }while(flag1==1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adminrequestlistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                adminrequestlistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        int flag1=0;
                        do {
                            flag1=0;
                            for (int i1 = 0; i1 < adminrequestlistmap.size(); i1++) {
                                if (adminrequestlistmap.get(i1).get("rid").toString().equals(temprid)) {
                                    flag1 = 1;
                                }
                            }
                            if(flag1==1){
                                temprid=randomAlphaNumeric(8);
                            }
                        }while(flag1==1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adminrequestlistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                adminrequestlistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        int flag1=0;
                        do {
                            flag1=0;
                            for (int i1 = 0; i1 < adminrequestlistmap.size(); i1++) {
                                if (adminrequestlistmap.get(i1).get("rid").toString().equals(temprid)) {
                                    flag1 = 1;
                                }
                            }
                            if(flag1==1){
                                temprid=randomAlphaNumeric(8);
                            }
                        }while(flag1==1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        arfb.addChildEventListener(arfb_listener);

    }

    private void InitializeLogic(){
        uid=vmauth.getCurrentUser().getUid();


        cffb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                complaintlistmap = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        complaintlistmap.add(_map);
                    }
                }
                catch (Exception _e) {
                    _e.printStackTrace();
                }

                dudfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        deptdatalistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                deptdatalistmap.add(_map);
                            }
                        }
                        catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        crfb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                compreplylistmap= new ArrayList<>();
                                try {
                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                    };
                                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                        HashMap<String, Object> _map = _data.getValue(_ind);
                                        compreplylistmap.add(_map);
                                    }
                                }
                                catch (Exception _e) {
                                    _e.printStackTrace();
                                }
                                dfb.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        deptlistmap= new ArrayList<>();
                                        try {
                                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                            };
                                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                                HashMap<String, Object> _map = _data.getValue(_ind);
                                                deptlistmap.add(_map);
                                            }
                                        }
                                        catch (Exception _e) {
                                            _e.printStackTrace();
                                        }
                                        arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                adminrequestlistmap= new ArrayList<>();
                                                try {
                                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                                    };
                                                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                                        HashMap<String, Object> _map = _data.getValue(_ind);
                                                        adminrequestlistmap.add(_map);
                                                    }
                                                }
                                                catch (Exception _e) {
                                                    _e.printStackTrace();
                                                }
                                                int flag1=0;
                                                do {
                                                    flag1=0;
                                                    temprid=randomAlphaNumeric(8);
                                                    for (int i1 = 0; i1 < adminrequestlistmap.size(); i1++) {
                                                        if (adminrequestlistmap.get(i1).get("rid").toString().equals(temprid)) {
                                                            flag1 = 1;
                                                        }
                                                    }
                                                }while(flag1==1);

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
    public class complaintlistadapter extends BaseAdapter {

        private ArrayList<HashMap<String,Object>> _data=new ArrayList<>();
        private  int sortmode=0;
        Calendar cutime=Calendar.getInstance();
        Calendar comptime=Calendar.getInstance();
        ArrayList<HashMap<String,Object>> dtim=new ArrayList<>();
        ArrayList<HashMap<String,Object>> sdtim=new ArrayList<>();

        public complaintlistadapter(ArrayList<HashMap<String,Object>> _arr,int sm){
            sortmode=sm;
            sortcidlist=new ArrayList<>();
            ArrayList<HashMap<String,Object>> mtdlist =new ArrayList<>();
            if(sm==0){
                _data=new ArrayList<>();
                for(int i7=0;i7<_arr.size();i7++) {
                    String comptimes = _arr.get(i7).get("time").toString();
                    comptime.set(Calendar.HOUR, (int) (Double.parseDouble(comptimes.substring((int) (0), (int) (2)))));
                    comptime.set(Calendar.MINUTE, (int) (Double.parseDouble(comptimes.substring((int) (3), (int) (5)))));
                    comptime.set(Calendar.SECOND, (int) (00));
                    comptime.set(Calendar.DAY_OF_MONTH, (int) (Double.parseDouble(comptimes.substring((int) (6), (int) (8)))));
                    comptime.set(Calendar.MONTH, (int) (Double.parseDouble(comptimes.substring((int) (9), (int) (11))) - 1));
                    comptime.set(Calendar.YEAR, (int) (Double.parseDouble(comptimes.substring((int) (12), (int) (16)))));
                    int ttd=(Double.valueOf(cutime.getTimeInMillis()-comptime.getTimeInMillis()).intValue());
                    tempmap1=new HashMap<>();
                    tempmap1.put("pos",i7);
                    tempmap1.put("val",ttd);
                    mtdlist.add(tempmap1);
                }
                for(int i7=0;i7<_arr.size();i7++){
                    int min=(int)Double.parseDouble(mtdlist.get(0).get("val").toString());
                    int temppos=(int)Double.parseDouble(mtdlist.get(0).get("pos").toString());
                    int temppos1=0;
                    for(int j7=0;j7<mtdlist.size();j7++){
                        if((int)Double.parseDouble(mtdlist.get(j7).get("val").toString())<min){
                            min=(int)Double.parseDouble(mtdlist.get(j7).get("val").toString());
                            temppos=(int)Double.parseDouble(mtdlist.get(j7).get("pos").toString());
                            temppos1=j7;
                        }
                    }
                    _data.add(_arr.get(temppos));
                    sortcidlist.add(_arr.get(temppos).get("cid").toString());
                    mtdlist.remove(temppos1);
                }

            }
            else{
                _data=new ArrayList<>();

                for (int i7=0;i7<_arr.size();i7++){
                    tempmap1=new HashMap<>();
                    tempmap1.put("pos",i7);
                    tempmap1.put("val",_arr.get(i7).get("supportno"));
                    mtdlist.add(tempmap1);
                }

                for(int i7=0;i7<_arr.size();i7++){
                    int max=0;
                    int temppos=(int)Double.parseDouble(mtdlist.get(0).get("pos").toString());
                    int temppos1=0;
                    for (int j7=0;j7<mtdlist.size();j7++){
                        if(max<(int)Double.parseDouble(mtdlist.get(j7).get("val").toString())){
                            max=(int)Double.parseDouble(mtdlist.get(j7).get("val").toString());
                            temppos=(int)Double.parseDouble(mtdlist.get(j7).get("pos").toString());
                            temppos1=j7;
                        }
                    }
                    _data.add(_arr.get(temppos));
                    sortcidlist.add(_arr.get(temppos).get("cid").toString());
                    mtdlist.remove(temppos1);
                }

            }
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public View getView(final int ist, View view, ViewGroup viewGroup) {
            LayoutInflater cv_inflater=(LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View cv_view=view;
            if(cv_view==null)
            {
                cv_view=cv_inflater.inflate(R.layout.deptcompistcustom,null);
            }

            final TextView comptitletext1 = cv_view.findViewById(R.id.comptitletext1);
            final TextView comptimetext1 = cv_view.findViewById(R.id.comptimetext1);
            final TextView compdesctext1=cv_view.findViewById(R.id.compdesctext1);
            final TextView supportnotext1=cv_view.findViewById(R.id.supportnotext1);
            final LinearLayout complistmainlinear1=cv_view.findViewById(R.id.complistmainlinear1);
            final ImageView complistimage1=cv_view.findViewById(R.id.complistimage1);

            complistmainlinear1.setVisibility(View.VISIBLE);
            comptitletext1.setText(_data.get(ist).get("title").toString());
            comptimetext1.setText(_data.get(ist).get("time").toString().substring(6,16));
            compdesctext1.setText(_data.get(ist).get("desc").toString());
            supportnotext1.setText("Support:"+_data.get(ist).get("supportno").toString());
            if(_data.get(ist).containsKey("ciuri")){
                Glide.with(getApplicationContext()).load(_data.get(ist).get("ciuri").toString()).into(complistimage1);
                Glide.with(getApplicationContext()).load(_data.get(ist).get("ciuri").toString()).into(complistimage1);
            }
            else {
                complistimage1.setVisibility(View.GONE);
            }
            /*dcomplaintlistviews.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    final String tempcid=sortcidlist.get(position);
                    final int tempposcid=searchcidlist(complaintlistmap,tempcid);
                    storageReference=compimgfbs.child("complaintimages/"+tempcid);
                    tempdepttext=complaintlistmap.get(tempposcid).get("dept").toString();
                    switch (index){
                        case 0:
                            tcdintent.putExtra("cid",tempcid );
                            ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(deptcomplaints.this,comptitletext1,"trans1");
                            startActivity(tcdintent,activityOptionsCompat.toBundle());
                            break;
                        case 1:
                            final Dialog dialog=new Dialog(deptcomplaints.this);
                            View view=dialog.getLayoutInflater().inflate(R.layout.replydialog,null );
                            rdreplyedit=view.findViewById(R.id.rdreplyedit);
                            rdreplybutton=view.findViewById(R.id.rdreplybutton);
                            try {
                                for (int i15 = 0; i15 < compreplylistmap.size(); i15++) {
                                    if (compreplylistmap.get(i15).get("cid").toString().equals(tempcid)) {
                                        rdreplyedit.setText(compreplylistmap.get(i15).get("reply").toString());
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(deptcomplaints.this, "Try Agaian", Toast.LENGTH_LONG).show();
                                break;
                            }
                            rdreplybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(rdreplyedit.getText().toString().length()>0) {
                                        calendar=Calendar.getInstance();
                                        tempmap1 = new HashMap<>();
                                        tempmap1.put("reply", rdreplyedit.getText().toString());
                                        tempmap1.put("title", complaintlistmap.get(tempposcid).get("title").toString());
                                        tempmap1.put("cid", tempcid);
                                        tempmap1.put("time", new SimpleDateFormat("HH:mm dd-MM-YYYY").format(calendar.getTime()));
                                        crfb.child(tempcid).updateChildren(tempmap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog.dismiss();
                                            }
                                        });

                                    }
                                }
                            });
                            dialog.getWindow().getAttributes().windowAnimations=R.style.DialogSlide;
                            dialog.setContentView(view);
                            dialog.show();
                            break;
                        case 2:
                            compdeptlist=new ArrayList<>();
                            for(int i9=0;i9<deptlistmap.size();i9++){
                                if(tempdepttext.contains(deptlistmap.get(i9).get("did").toString())){
                                    compdeptlist.add(deptlistmap.get(i9).get("did").toString());
                                }
                            }
                            final BottomSheetDialog botdialog=new BottomSheetDialog(deptcomplaints.this);
                            View view1=botdialog.getLayoutInflater().inflate(R.layout.deplistlayout,null);
                            depsellist=view1.findViewById(R.id.depsellist);
                            depsellist.setAdapter(new deptspinneradapter(deptlistmap));
                            setListViewHeightBasedOnItems(depsellist);
                            botdialog.setContentView(view1);
                            botdialog.show();
                            botdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    final ProgressDialog progressDialog=new ProgressDialog(deptcomplaints.this);
                                    progressDialog.setMessage("Loading");
                                    progressDialog.show();
                                    String dept1="";
                                    if(compdeptlist.size()>0){
                                        dept1=compdeptlist.get(0);
                                        for(int i12=1;i12<compdeptlist.size();i12++){
                                            dept1=dept1+","+compdeptlist.get(i12);
                                        }
                                        cffb.child(tempcid).child("dept").setValue(dept1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Snackbar.make(findViewById(R.id.dclinear),"Complaint forwarded Successfully" ,Snackbar.LENGTH_LONG ).show();
                                            }
                                        });
                                    }
                                }
                            });
                            break;
                        case 3:
                            final Dialog dialog1=new Dialog(deptcomplaints.this);
                            View view2=dialog1.getLayoutInflater().inflate(R.layout.darequestlayout,null );
                            darblockbutton=view2.findViewById(R.id.darblockbutton);
                            dardeletebutton=view2.findViewById(R.id.dardeletebutton);
                            dararchivebutton=view2.findViewById(R.id.dararchivebutton);
                            darblockbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tempmap1=new HashMap<>();
                                    tempmap1.put("type","0");
                                    tempmap1.put("rid",temprid);
                                    tempmap1.put("uid",complaintlistmap.get(tempposcid).get("uid").toString());
                                    tempmap1.put("cid",tempcid);
                                    tempmap1.put("status","0" );
                                    int f3=0;
                                    for(int i17=0;i17<adminrequestlistmap.size();i17++){
                                        if(adminrequestlistmap.get(i17).get("type").equals("0") && adminrequestlistmap.get(i17).get("status").equals("0")) {
                                            if (adminrequestlistmap.get(i17).get("uid").toString().equals(complaintlistmap.get(tempposcid).get("uid").toString()) && adminrequestlistmap.get(i17).get("reference").toString().equals(tempcid)) {
                                                f3 = 1;
                                            }
                                        }
                                    }
                                    if(f3==0){
                                        arfb.child(temprid).updateChildren(tempmap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog1.dismiss();
                                            }
                                        });
                                    }
                                    else{
                                        dialog1.dismiss();
                                        Toast.makeText(deptcomplaints.this,"This request is already made" ,Toast.LENGTH_LONG ).show();
                                    }
                                }
                            });
                            dardeletebutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tempmap1=new HashMap<>();
                                    tempmap1.put("type","1" );
                                    tempmap1.put("rid",temprid);
                                    tempmap1.put("cid",tempcid);
                                    tempmap1.put("status","0" );
                                    tempmap1.put("uid", complaintlistmap.get(tempposcid).get("uid").toString());
                                    int f3=0;
                                    for(int i17=0;i17<adminrequestlistmap.size();i17++){
                                        if(adminrequestlistmap.get(i17).get("type").toString().equals("1") && adminrequestlistmap.get(i17).get("status").equals("0")) {
                                            if (adminrequestlistmap.get(i17).get("cid").toString().equals(tempcid)) {
                                                f3 = 1;
                                            }
                                        }
                                    }
                                    if(f3==0) {
                                        arfb.child(temprid).updateChildren(tempmap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog1.dismiss();
                                            }
                                        });
                                    }
                                    else{
                                        dialog1.dismiss();
                                        Toast.makeText(deptcomplaints.this,"This request is already made" ,Toast.LENGTH_LONG ).show();
                                    }
                                }
                            });
                            dararchivebutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tempmap1=new HashMap<>();
                                    tempmap1.put("type","2" );
                                    tempmap1.put("rid",temprid );
                                    tempmap1.put("cid",tempcid);
                                    tempmap1.put("status","0");
                                    tempmap1.put("uid",complaintlistmap.get(tempposcid).get("uid").toString());
                                    int f3=0;
                                    for(int i17=0;i17<adminrequestlistmap.size();i17++){
                                        if(adminrequestlistmap.get(i17).get("type").toString().equals("2") && adminrequestlistmap.get(i17).get("status").equals("0")) {
                                            if (adminrequestlistmap.get(i17).get("cid").toString().equals(tempcid)) {
                                                f3 = 1;
                                            }
                                        }
                                    }
                                    if(f3==0) {
                                        arfb.child(temprid).updateChildren(tempmap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog1.dismiss();
                                            }
                                        });
                                    }
                                    else{
                                        dialog1.dismiss();
                                        Toast.makeText(deptcomplaints.this,"This request is already made" ,Toast.LENGTH_LONG ).show();
                                    }
                                }
                            });
                            dialog1.getWindow().getAttributes().windowAnimations=R.style.DialogSlide;
                            dialog1.setContentView(view2);
                            dialog1.show();
                            break;
                    }
                    return true;
                }
            });*/
            return cv_view;
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView)
    {
        ListAdapter listAdapter=listView.getAdapter();

        if(listAdapter!=null)
        {
            int numberOfItems =listAdapter.getCount();
            int th=0;
            for(int ip=0;ip<numberOfItems;ip++)
            {
                View item =listAdapter.getView(ip,null,listView);
                float px = 500*(listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px,View.MeasureSpec.AT_MOST),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
                th=th+item.getMeasuredHeight();
            }
            int totalDividerHeight=listView.getDividerHeight()*(numberOfItems-1);
            int totalPadding=listView.getPaddingTop()+listView.getPaddingBottom();
            ViewGroup.LayoutParams params= listView.getLayoutParams();
            params.height=(int)(th*(1.1)+totalDividerHeight+totalPadding);
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        }
        else
            return false;
    }
    public int searchcidlist(ArrayList<HashMap<String,Object>> _templistmap,String _tempcid){
        int _temppos=0;
        for (int i10=0;i10<_templistmap.size();i10++){
            if(_templistmap.get(i10).get("cid").toString().equals(_tempcid)){
                _temppos=i10;
            }
        }
        return _temppos;
    }

   /* public class deptspinneradapter extends BaseAdapter {

        private int chflag=0;

        private ArrayList<HashMap<String,Object>> _data=new ArrayList<>();

        public deptspinneradapter(ArrayList<HashMap<String,Object>> _arr){
            _data=_arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater ds_inflater=(LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View ds_view=convertView;
            if(ds_view==null)
            {
                ds_view=ds_inflater.inflate(R.layout.depspinnerlayout,null);
            }
            final TextView deptnametext=ds_view.findViewById(R.id.deptnametext);
            final CheckBox depcheck=ds_view.findViewById(R.id.depcheck);
            deptnametext.setText(_data.get(position).get("name").toString());
            if(compdeptlist.contains(deptlistmap.get(position).get("did").toString())){
                depcheck.setChecked(true);
            }
            depcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(depcheck.isChecked()){
                        if(!compdeptlist.contains(deptlistmap.get(position).get("did").toString())) {
                            compdeptlist.add(deptlistmap.get(position).get("did").toString());
                        }
                    }
                    else{
                        compdeptlist.remove(deptlistmap.get(position).get("did").toString());
                    }
                }
            });

            return ds_view;
        }
    }*/
}
