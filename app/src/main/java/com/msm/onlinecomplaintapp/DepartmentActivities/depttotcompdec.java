package com.msm.onlinecomplaintapp.DepartmentActivities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class depttotcompdec extends AppCompatActivity {

    private LinearLayout tcdimglinear1;
    private TextView tcdheadingtext1;
    private TextView timeauthtext1;
    private TextView tcddesctext1;
    private TextView tcdsnt1;
    private ImageView tcdimg1;

    private String cucid="";
    private String cuuid="";

    private int tf2=0;

    private Toolbar toolbar;

    private Transition transition;

    private ArrayList<HashMap<String,Object>> udlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> cucompmap=new HashMap<>();

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference cffb=_database.getReference("complaint");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depttotcompdec);


        tcdimglinear1=findViewById(R.id.tcdimglinear1);
        tcdimg1=findViewById(R.id.tcdimg1);
        tcddesctext1=findViewById(R.id.tcddesctext1);
        tcdheadingtext1=findViewById(R.id.tcdheadingtext1);
        tcdsnt1=findViewById(R.id.tcdsnt1);
        timeauthtext1=findViewById(R.id.timeauthtext1);

        cucid=getIntent().getStringExtra("cid");
        tcdheadingtext1.setText(getIntent().getStringExtra("title"));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();



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
                            tcdimglinear1.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(cucompmap.get("ciuri").toString()).into(tcdimg1);
                        }
                        else {
                            tcdimglinear1.setVisibility(View.GONE);
                        }
                        tcdheadingtext1.setText(cucompmap.get("title").toString());
                        tcddesctext1.setText(cucompmap.get("desc").toString());
                        tcdsnt1.setText("Support:"+cucompmap.get("supportno").toString());
                        timeauthtext1.setText(cucompmap.get("time").toString().substring(6,16)+"|"+cucompmap.get("time").toString().substring(0,5)+" hrs");
                        if(cucompmap.get("amode").toString().equals("no")){
                            for(int i11=0;i11<udlistmap.size();i11++){
                                if(cuuid.equals(udlistmap.get(i11).get("uid").toString())){
                                    timeauthtext1.setText(timeauthtext1.getText()+"|by "+udlistmap.get(i11).get("fullname").toString());
                                    break;
                                }
                            }
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
}
