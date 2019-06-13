package com.msm.onlinecomplaintapp.UserActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class homepage extends AppCompatActivity {

    private static final int REQUEST_CODE_NEWCOMP=5;
    private static final int REQUEST_CODE_HOMEPAGE=11;
    private static final int REQUEST_CODE_MYCOMPLAINT=12;
    private static final int REQUEST_CODE_SETTINGS=13;
    private static final int REQUEST_CODE_ARCHIVES=14;
    private static final int REQUEST_CODE_NOTIFICATION=15;
    private static final int REQUEST_CODE_MAIN=0;
    private static final int REQUEST_CODE_TCD=6;

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private Dialog mydialog;
    private Toolbar sorttool;

    private PopupMenu popup;

    private int uidpos=0;

    private Intent mainintent=new Intent();
    private Intent newcompintent=new Intent();
    private Intent mycomplaintintent=new Intent();
    private Intent settingsintent=new Intent();
    private Intent homeintent=new Intent();
    private Intent tcdintent=new Intent();
    private Intent notificationintent=new Intent();
    private Intent archiveintent=new Intent();

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;
    private Button sortbutton;
    private ListView complaintlistview;
    private FloatingActionButton ncfbutton;
    private TextView profilename;
    private TextView profileemail;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private ArrayList<HashMap<String,Object>> udlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> fcomplaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usercomplistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usersupplistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> tempmap2=new HashMap<>();
    private ArrayList<String> cidsortlist=new ArrayList<>();

    private int smf=0;
    private String uid="";

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference cffb=_database.getReference("complaint");
    private DatabaseReference ucfb=_database.getReference("usercomp");
    private DatabaseReference usfb=_database.getReference("usersupp");

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
        if(item.getItemId()==R.id.csortbutton){
            popup.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortbutton_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(requestCode!=REQUEST_CODE_NEWCOMP && requestCode!=REQUEST_CODE_TCD) {
            if (resultCode==RESULT_OK) {
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
                            if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_NOTIFICATION){
                                notificationintent.putExtra("ac","lo" );
                                setResult(RESULT_OK,notificationintent );
                            }
                            else{
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }
                        }


                    }
                }
                homepage.this.finish();
            }
        }
        else{
            complaintlistview.setAdapter(new complaintlistadapter(complaintlistmap,smf));
            setListViewHeightBasedOnItems(complaintlistview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initialize();
        initializelogic();
    }
    private void initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(homepage.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vmauth=FirebaseAuth.getInstance();

        sortbutton=findViewById(R.id.sortbutton);
        complaintlistview=findViewById(R.id.complaintlistview);
        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        archivebutton=findViewById(R.id.archivebutton);
        notificationbutton=findViewById(R.id.notification_button);
        logoutbutton=findViewById(R.id.logoutbutton);
        ncfbutton=findViewById(R.id.ncfbutton);
        profileemail=findViewById(R.id.profileemail);
        profilename=findViewById(R.id.profilename);

        mainintent.setClass(homepage.this,MainActivity.class);
        newcompintent.setClass(homepage.this,newcomplaint.class);
        settingsintent.setClass(homepage.this,settingsactivity.class);
        homeintent.setClass(homepage.this,homepage.class);
        mycomplaintintent.setClass(homepage.this,mycomplaints.class );
        notificationintent.setClass(homepage.this,notifictationsactivity.class );
        archiveintent.setClass(homepage.this,userarchives.class );
        tcdintent.setClass(homepage.this,totcompdesc.class);

        popup = new PopupMenu(homepage.this,sortbutton);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sortmenu, popup.getMenu());

        sortbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup.show();
            }
        });
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.toString().equals("Sort by time"))
                {
                    smf=0;
                }
                else{
                    smf=1;
                }
                complaintlistview.setAdapter(new complaintlistadapter(complaintlistmap,smf));
                setListViewHeightBasedOnItems(complaintlistview);
                return false;
            }
        });

        //mydialog=new Dialog(this);
        //mydialog.setContentView(R.layout._drawer_main);
        //mydialog.show();



        ncfbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(udlistmap.get(uidpos).get("uenable").toString().equals("1")) {
                    startActivityForResult(newcompintent, REQUEST_CODE_NEWCOMP);
                }
                else {
                    Toast.makeText(homepage.this, "This user is not allowed to post",Toast.LENGTH_LONG ).show();
                }
            }
        });


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

        notificationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(notificationintent,REQUEST_CODE_NOTIFICATION);
            }
        });

        archivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES);
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
                            if (getIntent().getIntExtra("pp",0 )==REQUEST_CODE_NOTIFICATION){
                                notificationintent.putExtra("ac","lo" );
                                setResult(RESULT_OK,notificationintent );
                            }
                            else{
                                if (getIntent().getIntExtra("pp",0)==REQUEST_CODE_MAIN)
                                {
                                    mainintent.putExtra("key1","logout");
                                    setResult(RESULT_OK,mainintent);
                                }
                            }
                        }


                    }
                }
                homepage.this.finish();
            }
        });


        ChildEventListener cffb_fl = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
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
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
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
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
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
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
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
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        cffb.addChildEventListener(cffb_fl);

        ChildEventListener usfb_fl = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap= new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        usfb.addChildEventListener(usfb_fl);

        final ChildEventListener ucfb_fl = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(complaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                final String _childKey = dataSnapshot.getKey();
                final HashMap<String, Object> _childValue = dataSnapshot.getValue(_ind);
                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};

                ucfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usercomplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usercomplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        fcomplaintlistmap=new ArrayList<>();
                        for (int i25=0;i25<complaintlistmap.size();i25++){
                            if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                fcomplaintlistmap.add(complaintlistmap.get(i25));
                            }
                        }
                        complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                        setListViewHeightBasedOnItems(complaintlistview);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ucfb.addChildEventListener(ucfb_fl);
    }
    private void initializelogic(){

        uid=vmauth.getCurrentUser().getUid();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

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
                usfb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot _dataSnapshot) {
                        usersupplistmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                usersupplistmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        udfb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                udlistmap= new ArrayList<>();
                                try {
                                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                    };
                                    for (DataSnapshot _data : dataSnapshot.getChildren()) {
                                        HashMap<String, Object> _map = _data.getValue(_ind);
                                        udlistmap.add(_map);
                                    }
                                } catch (Exception _e) {
                                    _e.printStackTrace();
                                }
                                for (int i23=0;i23<udlistmap.size();i23++){
                                    if(udlistmap.get(i23).get("uid").toString().equals(uid)){
                                        uidpos=i23;
                                    }
                                }
                                fcomplaintlistmap=new ArrayList<>();
                                for (int i25=0;i25<complaintlistmap.size();i25++){
                                    if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("0")){
                                        fcomplaintlistmap.add(complaintlistmap.get(i25));
                                    }
                                }
                                profileemail.setText(udlistmap.get(uidpos).get("email").toString());
                                profilename.setText(udlistmap.get(uidpos).get("fullname").toString());
                                complaintlistview.setAdapter(new complaintlistadapter(fcomplaintlistmap,smf));
                                setListViewHeightBasedOnItems(complaintlistview);
                                progressDialog.dismiss();
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


    public class complaintlistadapter extends BaseAdapter{

        private ArrayList<HashMap<String,Object>> _data=new ArrayList<>();;
        Calendar cutime=Calendar.getInstance();
        Calendar comptime=Calendar.getInstance();
        ArrayList<HashMap<String,Object>> dtim=new ArrayList<>();
        ArrayList<HashMap<String,Object>> sdtim=new ArrayList<>();

        public complaintlistadapter(ArrayList<HashMap<String,Object>> _arr,int sm){
            ArrayList<HashMap<String,Object>> mtdlist =new ArrayList<>();
            if(sm==0){
                _data=new ArrayList<>();
                cidsortlist=new ArrayList<>();
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
                    if(_arr.get(temppos).get("mode").toString().equals("public")) {
                        _data.add(_arr.get(temppos));
                        cidsortlist.add(_arr.get(temppos).get("cid").toString());
                        mtdlist.remove(temppos1);
                    }else{
                        mtdlist.remove(temppos1);
                    }
                }

            }
            else{
                _data=new ArrayList<>();
                cidsortlist=new ArrayList<>();
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
                    if(_arr.get(temppos).get("mode").toString().equals("public")) {
                        _data.add(_arr.get(temppos));
                        cidsortlist.add(_arr.get(temppos).get("cid").toString());
                        mtdlist.remove(temppos1);
                    }else{
                        mtdlist.remove(temppos1);
                    }
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
                cv_view=cv_inflater.inflate(R.layout.pchlistcustom,null);
            }

            final TextView comptitletext = cv_view.findViewById(R.id.comptitletext);
            final TextView comptimetext = cv_view.findViewById(R.id.comptimetext);
            final TextView compdesctext=cv_view.findViewById(R.id.compdesctext);
            final TextView supportnotext=cv_view.findViewById(R.id.supportnotext);
            final ToggleButton supportbutton=cv_view.findViewById(R.id.supportbutton);
            final LinearLayout complistmainlinear=cv_view.findViewById(R.id.complistmainlinear);
            final ImageView complistimage=cv_view.findViewById(R.id.complistimage);

            int tf2=0;

            complistmainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tcdintent.putExtra("cid",_data.get(ist).get("cid").toString());
                    ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(homepage.this,comptitletext,"trans2");
                    startActivityForResult(tcdintent,REQUEST_CODE_TCD,activityOptionsCompat.toBundle());
                }
            });

            for(int i3=0;i3<usersupplistmap.size();i3++){
                if(usersupplistmap.get(i3).get("uid").toString().equals(uid)){
                    for(int j3=0;j3<usersupplistmap.get(i3).size()-1;j3++){
                        if(usersupplistmap.get(i3).get("c"+String.valueOf(j3+1)).toString().equals(_data.get(ist).get("cid").toString())){
                            tf2=1;
                        }
                    }
                }
            }
            if(tf2==1){
                supportbutton.setChecked(true);
            }
            else {
                supportbutton.setChecked(false);
            }

            if(_data.get(_data.size()-ist-1).get("mode").toString().equals("public")){
                complistmainlinear.setVisibility(View.VISIBLE);
                comptitletext.setText(_data.get(ist).get("title").toString());
                comptimetext.setText(_data.get(ist).get("time").toString().substring(6,16));
                compdesctext.setText(_data.get(ist).get("desc").toString());
                       /* compdesctext.post(new Runnable() {
                            @Override
                            public void run() {
                                int lc=compdesctext.getLineCount();
                                if(lc>=4)
                                {
                                    compdesctext.setMaxLines(4);
                                    compdesctext.setText(_data.get(ist).get("desc").toString());
                                }

                                else{
                                    compdesctext.setText(_data.get(ist).get("desc").toString());
                                }
                            }
                        });

                       if(_data.get(ist).get("desc").toString().length()>100){
                            compdesctext.setText(Html.fromHtml(_data.get(ist).get("desc").toString().substring(0,90)+"....\n"+"<b>"+"Tap to see more info"+"</b>") );
                        }
                        else {
                            compdesctext.setText(_data.get(ist).get("desc").toString());
                        }*/
                supportnotext.setText("Support:"+_data.get(ist).get("supportno").toString());
                if(_data.get(ist).containsKey("ciuri")){
                    Glide.with(getApplicationContext()).load(_data.get(ist).get("ciuri").toString()).into(complistimage);
                    Glide.with(getApplicationContext()).load(_data.get(ist).get("ciuri").toString()).into(complistimage);
                }
                else {
                    complistimage.setVisibility(View.GONE);
                }
                supportbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(supportbutton.isChecked()) {
                            int tf1=0;
                            tempmap1=new HashMap<>();
                            for(int i1=0;i1<usersupplistmap.size();i1++){
                                if(usersupplistmap.get(i1).get("uid").toString().equals(uid)){
                                    tempmap1=usersupplistmap.get(i1);
                                    tempmap1.put("c"+String.valueOf((int)(usersupplistmap.get(i1).size())),_data.get(ist).get("cid").toString());
                                    usfb.child(uid).updateChildren(tempmap1);
                                    tf1=1;
                                    break;
                                }
                            }
                            if(tf1==0){
                                tempmap1=new HashMap<>();
                                tempmap1.put("uid",uid);
                                tempmap1.put("c1",_data.get(ist).get("cid").toString());
                                usfb.child(uid).updateChildren(tempmap1);
                            }
                            tempmap2=new HashMap<>();
                            for(int i2=0;i2<complaintlistmap.size();i2++){
                                if(complaintlistmap.get(i2).get("cid").toString().equals(_data.get(ist).get("cid").toString())){
                                    tempmap2=complaintlistmap.get(i2);
                                    int tsn=(int)Double.parseDouble(tempmap2.get("supportno").toString());
                                    tempmap2.remove("supportno");
                                    tempmap2.put("supportno",tsn+1);
                                    cffb.child(_data.get(ist).get("cid").toString()).updateChildren(tempmap2);
                                    break;
                                }
                            }
                        }
                        else{
                            tempmap1=new HashMap<>();
                            int tpc=0;
                            for(int i1=0;i1<usersupplistmap.size();i1++){
                                if(usersupplistmap.get(i1).get("uid").toString().equals(uid)){
                                    tempmap1=usersupplistmap.get(i1);
                                    for(int j1=0;j1<tempmap1.size()-1;j1++){
                                        if(tempmap1.get("c"+String.valueOf(j1+1)).equals(_data.get(ist).get("cid").toString())){
                                            tpc=j1+1;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            tempmap1.remove("c"+String.valueOf(tpc));
                            for(int j1=tpc;j1<tempmap1.size();j1++){
                                tempmap1.put("c"+String.valueOf(j1),tempmap1.get("c"+String.valueOf(j1+1)));
                                tpc=j1+1;
                            }
                            if(tempmap1.containsKey("c"+String.valueOf(tpc))) {
                                tempmap1.remove("c" + String.valueOf(tpc));
                            }
                            if(tempmap1.size()==1){
                                usfb.child(uid).removeValue();
                            }
                            else {
                                usfb.child(uid).removeValue();
                                usfb.child(uid).updateChildren(tempmap1);
                            }
                            tempmap2=new HashMap<>();
                            for(int i2=0;i2<complaintlistmap.size();i2++){
                                if(complaintlistmap.get(i2).get("cid").toString().equals(_data.get(ist).get("cid").toString())){
                                    tempmap2=complaintlistmap.get(i2);
                                    int tsn=(int)Double.parseDouble(tempmap2.get("supportno").toString());
                                    tempmap2.remove("supportno");
                                    tempmap2.put("supportno",tsn-1);
                                    cffb.child(_data.get(ist).get("cid").toString()).updateChildren(tempmap2);
                                    break;
                                }
                            }

                        }
                    }
                });
            }
            else{
                complistmainlinear.setVisibility(View.GONE);
            }

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
            params.height=th+totalDividerHeight+totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        }
        else
            return false;
    }

}
