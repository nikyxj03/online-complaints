package com.msm.onlinecomplaintapp.UserActivities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class totcompdesc extends AppCompatActivity {

    private LinearLayout tcdimglinear;
    private TextView tcdheadingtext;
    private TextView timeauthtext;
    private TextView tcddesctext;
    private TextView tcdsnt;
    private ImageView tcdimg;
    private ToggleButton tcdsuppbutton;

    private int tf1=0;

    private String cucid="";
    private String cuuid="";

    private int tf2=0;

    private Toolbar toolbar;

    private ArrayList<HashMap<String,Object>> udlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usercomplistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usersupplistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> tempmap2=new HashMap<>();
    private HashMap<String,Object> cucompmap=new HashMap<>();

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference cffb=_database.getReference("complaint");
    private DatabaseReference ucfb=_database.getReference("usercomp");
    private DatabaseReference usfb=_database.getReference("usersupp");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totcompdesc);
        Initialize();
        InitializeLogic();
    }
    private void Initialize(){
        tcdimglinear=findViewById(R.id.tcdimglinear);
        tcdimg=findViewById(R.id.tcdimg);
        tcddesctext=findViewById(R.id.tcddesctext);
        tcdheadingtext=findViewById(R.id.tcdheadingtext);
        tcdsnt=findViewById(R.id.tcdsnt);
        tcdsuppbutton=findViewById(R.id.tcdsuppbutton);
        timeauthtext=findViewById(R.id.timeauthtext);

        tcdsuppbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tcdsuppbutton.isChecked()) {
                    int tf1=0;
                    tempmap1=new HashMap<>();
                    for(int i1=0;i1<usersupplistmap.size();i1++){
                        if(usersupplistmap.get(i1).get("uid").toString().equals(cuuid)){
                            tempmap1=usersupplistmap.get(i1);
                            tempmap1.put("c"+String.valueOf((int)(usersupplistmap.get(i1).size())),cucid);
                            usfb.child(cuuid).updateChildren(tempmap1);
                            tf1=1;
                            break;
                        }
                    }
                    if(tf1==0){
                        tempmap1=new HashMap<>();
                        tempmap1.put("uid",cuuid);
                        tempmap1.put("c1",cucid);
                        usfb.child(cuuid).updateChildren(tempmap1);
                    }
                    tempmap2=new HashMap<>();
                    for(int i2=0;i2<complaintlistmap.size();i2++){
                        if(complaintlistmap.get(i2).get("cid").toString().equals(cucid)){
                            tempmap2=complaintlistmap.get(i2);
                            int tsn=(int)Double.parseDouble(tempmap2.get("supportno").toString());
                            tempmap2.remove("supportno");
                            tempmap2.put("supportno",tsn+1);
                            cffb.child(cucid).updateChildren(tempmap2);
                            break;
                        }
                    }
                }
                else{
                    tempmap1=new HashMap<>();
                    int tpc=0;
                    for(int i1=0;i1<usersupplistmap.size();i1++){
                        if(usersupplistmap.get(i1).get("uid").toString().equals(cuuid)){
                            tempmap1=usersupplistmap.get(i1);
                            for(int j1=0;j1<tempmap1.size()-1;j1++){
                                if(tempmap1.get("c"+String.valueOf(j1+1)).equals(cucid)){
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
                        usfb.child(cuuid).removeValue();
                    }
                    else {
                        usfb.child(cuuid).removeValue();
                        usfb.child(cuuid).updateChildren(tempmap1);
                    }
                    tempmap2=new HashMap<>();
                    for(int i2=0;i2<complaintlistmap.size();i2++){
                        if(complaintlistmap.get(i2).get("cid").toString().equals(cucid)){
                            tempmap2=complaintlistmap.get(i2);
                            int tsn=(int)Double.parseDouble(tempmap2.get("supportno").toString());
                            tempmap2.remove("supportno");
                            tempmap2.put("supportno",tsn-1);
                            cffb.child(cucid).updateChildren(tempmap2);
                            break;
                        }
                    }

                }
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
                        for(int i10=0;i10<complaintlistmap.size();i10++){
                            if(complaintlistmap.get(i10).get("cid").toString().equals(cucid)){
                                cucompmap=complaintlistmap.get(i10);
                            }
                        }
                        tcdsnt.setText("Support"+cucompmap.get("supportno").toString());
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
                        for(int i10=0;i10<complaintlistmap.size();i10++){
                            if(complaintlistmap.get(i10).get("cid").toString().equals(cucid)){
                                cucompmap=complaintlistmap.get(i10);
                            }
                        }
                        tcdsnt.setText("Support"+cucompmap.get("supportno").toString());
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
                        for(int i10=0;i10<complaintlistmap.size();i10++){
                            if(complaintlistmap.get(i10).get("cid").toString().equals(cucid)){
                                cucompmap=complaintlistmap.get(i10);
                            }
                        }
                        tcdsnt.setText("Support"+cucompmap.get("supportno").toString());
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
                        for(int i10=0;i10<complaintlistmap.size();i10++){
                            if(complaintlistmap.get(i10).get("cid").toString().equals(cucid)){
                                cucompmap=complaintlistmap.get(i10);
                            }
                        }
                        tcdsnt.setText("Support"+cucompmap.get("supportno").toString());

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

    }
    private void InitializeLogic(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        cucid=getIntent().getStringExtra("cid");

        cuuid=vmauth.getCurrentUser().getUid();

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
                                udlistmap = new ArrayList<>();
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
                                for(int i10=0;i10<complaintlistmap.size();i10++){
                                    if(complaintlistmap.get(i10).get("cid").toString().equals(cucid)){
                                        cucompmap=complaintlistmap.get(i10);
                                    }
                                }
                                if(cucompmap.containsKey("ciuri")){
                                    tcdimglinear.setVisibility(View.VISIBLE);
                                    Glide.with(getApplicationContext()).load(cucompmap.get("ciuri").toString()).into(tcdimg);
                                }
                                else {
                                    tcdimglinear.setVisibility(View.GONE);
                                }
                                tcdheadingtext.setText(cucompmap.get("title").toString());
                                tcddesctext.setText(cucompmap.get("desc").toString());
                                tcdsnt.setText("Support"+cucompmap.get("supportno").toString());
                                timeauthtext.setText(cucompmap.get("time").toString().substring(6,16)+"|"+cucompmap.get("time").toString().substring(0,5)+" hrs");
                                if(cucompmap.get("amode").toString().equals("no")){
                                    for(int i11=0;i11<udlistmap.size();i11++){
                                        if(cuuid.equals(udlistmap.get(i11).get("uid").toString())){
                                            timeauthtext.setText(timeauthtext.getText()+"|by "+udlistmap.get(i11).get("fullname").toString());
                                            break;
                                        }
                                    }
                                }
                                for(int i3=0;i3<usersupplistmap.size();i3++){
                                    if(usersupplistmap.get(i3).get("uid").toString().equals(cuuid)){
                                        for (int j3=0;j3<usersupplistmap.get(i3).size()-1;j3++){
                                            if(usersupplistmap.get(i3).get("c"+String.valueOf(j3+1)).toString().equals(cucompmap.get("cid").toString())){
                                                tf2=1;
                                            }
                                        }
                                    }
                                }
                                if(tf2==1){
                                    tcdsuppbutton.setChecked(true);
                                }
                                else {
                                    tcdsuppbutton.setChecked(false);
                                }
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
}
