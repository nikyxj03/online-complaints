package com.msm.onlinecomplaintapp.Admin.AdminActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.LoginActivities.LoginActivity;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.depttotcompdec;

import java.util.ArrayList;
import java.util.HashMap;

public class adminrequests extends AppCompatActivity {

    private static final int REQUEST_CODE_HOMEPAGE_A=31;
    private static final int REQUEST_CODE_SETTINGS_A=34;
    private static final int REQUEST_CODE_MAIN_A=0;
    private static final int REQUEST_CODE_TCD_A=7;
    private static final int REQUEST_CODE_REQPAGE_A=32;
    private static final int REQUEST_CODE_CREATEAC_A=33;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Intent adminhomeintent=new Intent();
    private Intent requestintent=new Intent();
    private Intent createacintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent mainintent=new Intent();
    private Intent tcdintent =new Intent();

    private Button homebutton2;
    private Button logoutbutton2;
    private Button settingsbutton2;
    private Button myrequestbutton;
    private Button createaccountbutton;
    private SwipeMenuListView requestlistview;

    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> tempmap2=new HashMap<>();
    private ArrayList<HashMap<String,Object>> adminrequestlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> udlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> selrequestlist=new ArrayList<>();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference cffb=_database.getReference("complaint");
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference arfb=_database.getReference("adminrequests");

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(true) {
            if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_A) {
                adminhomeintent.putExtra("ac", "lo");
                setResult(RESULT_OK,adminhomeintent);
            } else {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_A) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                }
                else{
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_CREATEAC_A){
                        createacintent.putExtra("ac","lo");
                        setResult(RESULT_OK, createacintent);
                    }else {
                        if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                        {
                            mainintent.putExtra("key1","logout");
                            setResult(RESULT_OK,mainintent);
                        }
                    }

                }
            }
            adminrequests.this.finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
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
        setContentView(R.layout.activity_adminrequests);
        Initialize();
        InitializeLogic();
    }
    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(adminrequests.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        homebutton2=findViewById(R.id.homebutton2);
        myrequestbutton=findViewById(R.id.myrequestsbutton);
        createaccountbutton=findViewById(R.id.createaccountbutton);
        settingsbutton2=findViewById(R.id.settingsbutton2);
        logoutbutton2=findViewById(R.id.logoutbutton2);
        requestlistview=findViewById(R.id.requestlistview);

        mainintent.setClass(adminrequests.this, LoginActivity.class);
        settingsintent.setClass(adminrequests.this,adminsettings.class);
        adminhomeintent.setClass(adminrequests.this,admin_home.class);
        tcdintent.setClass(adminrequests.this,depttotcompdec.class);
        requestintent.setClass(adminrequests.this, adminrequests.class);
        createacintent.setClass(adminrequests.this,admincreateaccount.class );

        settingsbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_A);
            }
        });

        createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(createacintent,REQUEST_CODE_CREATEAC_A );
            }
        });

        homebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(adminhomeintent,REQUEST_CODE_HOMEPAGE_A);
            }
        });

        logoutbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_HOMEPAGE_A) {
                    adminhomeintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,adminhomeintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_A) {
                        settingsintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,settingsintent);
                    }
                    else{
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_CREATEAC_A){
                            createacintent.putExtra("ac","lo");
                            setResult(RESULT_OK, createacintent);
                        }else {
                            if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN_A)
                            {
                                mainintent.putExtra("key1","logout");
                                setResult(RESULT_OK,mainintent);
                            }
                        }

                    }
                }
                adminrequests.this.finish();
            }
        });

        requestlistview.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

        ChildEventListener arfb_listener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s) {
                arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
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
                        selrequestlist=new ArrayList<>();
                        for (int i16=0;i16<adminrequestlistmap.size();i16++){
                            if(adminrequestlistmap.get(i16).get("status").toString().equals("0")){
                                selrequestlist.add(adminrequestlistmap.get(i16));
                            }
                        }
                        requestlistview.setAdapter(new requestlistadapter(selrequestlist));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,String s) {
                arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adminrequestlistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                adminrequestlistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        selrequestlist=new ArrayList<>();
                        for (int i16=0;i16<adminrequestlistmap.size();i16++){
                            if(adminrequestlistmap.get(i16).get("status").toString().equals("0")){
                                selrequestlist.add(adminrequestlistmap.get(i16));
                            }
                        }
                        requestlistview.setAdapter(new requestlistadapter(selrequestlist));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
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
                        selrequestlist=new ArrayList<>();
                        for (int i16=0;i16<adminrequestlistmap.size();i16++){
                            if(adminrequestlistmap.get(i16).get("status").toString().equals("0")){
                                selrequestlist.add(adminrequestlistmap.get(i16));
                            }
                        }
                        requestlistview.setAdapter(new requestlistadapter(selrequestlist));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot,String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        arfb.addChildEventListener(arfb_listener);


    }

    private void InitializeLogic(){

        final ProgressDialog progressDialog=new ProgressDialog(adminrequests.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        udfb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                udlistmap = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        udlistmap.add(_map);
                    }
                }
                catch (Exception _e) {
                    _e.printStackTrace();
                }
                arfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
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
                        cffb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                complaintlistmap= new ArrayList<>();
                                try {
                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                    };
                                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                        HashMap<String, Object> _map = _data.getValue(_ind);
                                        complaintlistmap.add(_map);
                                    }
                                }
                                catch (Exception _e) {
                                    _e.printStackTrace();
                                }
                                selrequestlist=new ArrayList<>();
                                for (int i16=0;i16<adminrequestlistmap.size();i16++){
                                    if(adminrequestlistmap.get(i16).get("status").toString().equals("0")){
                                        selrequestlist.add(adminrequestlistmap.get(i16));
                                    }
                                }
                                SwipeMenuCreator creator=new SwipeMenuCreator() {
                                    @Override
                                    public void create(SwipeMenu menu) {
                                        SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                                        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                                        openItem.setWidth(350);
                                        openItem.setTitle("View");
                                        openItem.setTitleSize(18);
                                        openItem.setTitleColor(Color.WHITE);
                                        menu.addMenuItem(openItem);
                                    }
                                };
                                requestlistview.setMenuCreator(creator);
                                requestlistview.setAdapter(new requestlistadapter(selrequestlist));
                                requestlistview.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
                                requestlistview.setCloseInterpolator(new BounceInterpolator());
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class requestlistadapter extends BaseAdapter {

        private int chflag=0;

        private ArrayList<HashMap<String,Object>> _data=new ArrayList<>();

        public requestlistadapter(ArrayList<HashMap<String,Object>> _arr){
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
            LayoutInflater rl_inflater=(LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rl_view=convertView;
            if(rl_view==null)
            {
                rl_view=rl_inflater.inflate(R.layout.requestviewcustom,null);
            }
            final TextView rvtitletext=rl_view.findViewById(R.id.rvtitletext);
            final TextView rvrequesttext=rl_view.findViewById(R.id.rvrequesttext);
            final Button rvyesbutton=rl_view.findViewById(R.id.rvyesbutton);
            final Button rvnobutton=rl_view.findViewById(R.id.rvnobutton);



            if(_data.get(position).get("type").toString().equals("0")) {
                rvrequesttext.setText("Block the user");
                rvtitletext.setText(Html.fromHtml("<b>Complaint:</b>" +_data.get(position).get("cid").toString()));
            }else{
                if(_data.get(position).get("type").toString().equals("1")) {
                    rvtitletext.setText(Html.fromHtml("<b>Complaint:</b>" + _data.get(position).get("cid").toString()));
                    rvrequesttext.setText("Delete complaint");
                }
                else{
                    if(_data.get(position).get("type").toString().equals("2")){
                        rvtitletext.setText(Html.fromHtml("<b>Complaint:</b>" + _data.get(position).get("cid").toString()));
                        rvrequesttext.setText("Send Complaint to Archive");
                    }
                }
            }

            requestlistview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index){
                        case 0:
                            tcdintent.putExtra("cid", _data.get(position).get("cid").toString());
                            startActivity(tcdintent);
                            break;
                    }
                    return false;
                }
            });

            rvyesbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(adminrequests.this);
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(_data.get(position).get("type").toString().equals("0")) {
                                //Toast.makeText(adminrequests.this,_data.get(position).get("type").toString()+"0" ,Toast.LENGTH_LONG ).show();
                                udfb.child(_data.get(position).get("uid").toString()).child("uenable").setValue("0");
                            }
                            else{
                                if(_data.get(position).get("type").toString().equals("1")) {
                                    //Toast.makeText(adminrequests.this,_data.get(position).get("type").toString()+"1" ,Toast.LENGTH_LONG ).show();
                                    cffb.child(_data.get(position).get("cid").toString()).removeValue();
                                }
                                else{
                                    if(_data.get(position).get("type").toString().equals("2")) {
                                        //Toast.makeText(adminrequests.this,_data.get(position).get("type").toString()+"2" ,Toast.LENGTH_LONG ).show();
                                        cffb.child(_data.get(position).get("cid").toString()).child("acm").setValue("1");
                                    }
                                }
                            }
                            arfb.child(_data.get(position).get("rid").toString()).child("status").setValue("1");
                        }
                    });
                    builder.create().show();

                }
            });

            rvnobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arfb.child(_data.get(position).get("rid").toString()).child("status").setValue("1");
                }
            });

            return rl_view;
        }
    }
}