package com.msm.onlinecomplaintapp.UserActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class mycomplaints extends AppCompatActivity {

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

    private PopupMenu popup;

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
    private Button sortbutton1;
    private ListView complaintlistview1;
    private FloatingActionButton ncfbutton;
    private SwipeMenuListView complaintlistviews;

    private BottomSheetDialog cmbsd;
    private Button cmapplybutton;
    private RadioButton cmpublicrb;
    private RadioButton cmprivaterb;
    private RadioButton cmanarb;
    private RadioButton cmnanarb;
    private RadioGroup cmprg;
    private RadioGroup cmarg;

    private ArrayList<HashMap<String,Object>> udlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usercomplistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> usersupplistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> mycomplaintlistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> tempmap2=new HashMap<>();
    private ArrayList<String> sortcidlist=new ArrayList<>();

    private int smf=0;
    private String uid="";

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference cffb=_database.getReference("complaint");
    private DatabaseReference ucfb=_database.getReference("usercomp");
    private DatabaseReference usfb=_database.getReference("usersupp");

    private FirebaseStorage vmfbs=FirebaseStorage.getInstance();
    private StorageReference compimgfbs=vmfbs.getReference();
    private StorageReference storageReference;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        if(requestCode!=REQUEST_CODE_TCD) {
            if (resultCode==RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) ==REQUEST_CODE_SETTINGS) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK,settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,homeintent);
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
                mycomplaints.this.finish();
            }
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
        setContentView(R.layout.activity_mycomplaints);
        Initialize();
        InitializeLogic();
    }

    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(mycomplaints.this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vmauth=FirebaseAuth.getInstance();

        sortbutton1=findViewById(R.id.sortbutton1);
        complaintlistview1=findViewById(R.id.complaintlistview1);
        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        logoutbutton=findViewById(R.id.logoutbutton);
        archivebutton=findViewById(R.id.archivebutton);
        notificationbutton=findViewById(R.id.notification_button);
        complaintlistviews=findViewById(R.id.complaintlistviews);

        mainintent.setClass(mycomplaints.this,MainActivity.class);
        newcompintent.setClass(mycomplaints.this,newcomplaint.class);
        settingsintent.setClass(mycomplaints.this,settingsactivity.class);
        homeintent.setClass(mycomplaints.this,homepage.class);
        mycomplaintintent.setClass(mycomplaints.this,mycomplaints.class );
        notificationintent.setClass(mycomplaints.this,notifictationsactivity.class );
        archiveintent.setClass(mycomplaints.this,userarchives.class );
        tcdintent.setClass(mycomplaints.this,totcompdesc.class);

        popup = new PopupMenu(mycomplaints.this,sortbutton1);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sortmenu, popup.getMenu());

        sortbutton1.setOnClickListener(new View.OnClickListener() {
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
                mycomplaintlistmap=new ArrayList<>();
                for(int i8=0;i8<complaintlistmap.size();i8++){
                    if(complaintlistmap.get(i8).get("uid").toString().equals(uid)){
                        mycomplaintlistmap.add(complaintlistmap.get(i8));
                    }
                }
                complaintlistviews.setAdapter(new complaintlistadapter(mycomplaintlistmap, smf));
                //setListViewHeightBasedOnItems(complaintlistview1);
                return false;
            }
        });



        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(homeintent,REQUEST_CODE_HOMEPAGE);
            }
        });

        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS);
            }
        });

        mycomplaintsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mycomplaintintent,REQUEST_CODE_MYCOMPLAINT );
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
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_HOMEPAGE) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK,homeintent);
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
                mycomplaints.this.finish();
            }
        });

    }

    private void InitializeLogic(){

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

                        mycomplaintlistmap=new ArrayList<>();
                        for(int i8=0;i8<complaintlistmap.size();i8++){
                            if(complaintlistmap.get(i8).get("uid").toString().equals(uid)){
                                mycomplaintlistmap.add(complaintlistmap.get(i8));
                            }
                        }
                        complaintlistviews.setAdapter(new complaintlistadapter(mycomplaintlistmap,smf));
                        SwipeMenuCreator creator = new SwipeMenuCreator() {
                            @Override
                            public void create(SwipeMenu menu) {
                                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                                openItem.setWidth(240);
                                openItem.setTitle("View");
                                openItem.setTitleSize(18);
                                openItem.setTitleColor(Color.WHITE);
                                menu.addMenuItem(openItem);
                                SwipeMenuItem modeitem=new SwipeMenuItem(getApplicationContext());
                                modeitem.setTitle("Change Mode");
                                modeitem.setTitleColor(Color.WHITE);
                                modeitem.setTitleSize(18);
                                modeitem.setWidth(240);
                                modeitem.setBackground(R.color.colorPrimary);
                                menu.addMenuItem(modeitem);
                                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                                deleteItem.setWidth(240);
                                deleteItem.setTitle("Delete");
                                deleteItem.setTitleSize(18);
                                deleteItem.setTitleColor(Color.WHITE);
                                menu.addMenuItem(deleteItem);
                            }
                        };
                        complaintlistviews.setMenuCreator(creator);
                        //setListViewHeightBasedOnItems(complaintlistviews);
                        complaintlistviews.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
                        complaintlistviews.setCloseInterpolator(new BounceInterpolator());
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
                complistmainlinear.setVisibility(View.VISIBLE);
                comptitletext.setText(_data.get(ist).get("title").toString());
                comptimetext.setText(_data.get(ist).get("time").toString().substring(6,16));
                compdesctext.setText(_data.get(ist).get("desc").toString());
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
            complaintlistviews.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    final String tempcid=sortcidlist.get(position);
                    final int tempposcid=searchcidlist(complaintlistmap,tempcid);
                    storageReference=compimgfbs.child("complaintimages/"+tempcid);
                    switch (index) {
                        case 0:
                            tcdintent.putExtra("cid",tempcid);
                            ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(mycomplaints.this,comptitletext,"trans2");
                            startActivityForResult(tcdintent,REQUEST_CODE_TCD,activityOptionsCompat.toBundle());
                            break;
                        case 1:
                            cmbsd=new BottomSheetDialog(mycomplaints.this);
                            View view=cmbsd.getLayoutInflater().inflate(R.layout.changemodelayout,null);
                            cmapplybutton=view.findViewById(R.id.cmapplybutton);
                            cmpublicrb=view.findViewById(R.id.cmpublicrb);
                            cmprivaterb=view.findViewById(R.id.cmprivaterb);
                            cmanarb=view.findViewById(R.id.cmanarb);
                            cmnanarb=view.findViewById(R.id.cmnanarb);
                            cmarg=view.findViewById(R.id.cmarg);
                            cmprg=view.findViewById(R.id.cmprg);
                            if(complaintlistmap.get(tempposcid).get("mode").toString().equals("public"))
                            {
                                cmpublicrb.setChecked(true);
                            }
                            else{
                                cmprivaterb.setChecked(true);
                            }
                            if(complaintlistmap.get(tempposcid).get("amode").toString().equals("yes"))
                            {
                                cmanarb.setChecked(true);
                            }
                            else{
                                cmnanarb.setChecked(true);
                            }
                            cmapplybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final ProgressDialog progressDialog = new ProgressDialog(mycomplaints.this);
                                    progressDialog.setMessage("Loading...");
                                    progressDialog.show();
                                    if(cmpublicrb.isChecked()){
                                        cffb.child(complaintlistmap.get(tempposcid).get("cid").toString()).child("mode").setValue("public").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if(cmanarb.isChecked()){
                                                    cffb.child(complaintlistmap.get(tempposcid).get("cid").toString()).child("amode").setValue("yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                }
                                                else {
                                                    cffb.child(complaintlistmap.get(tempposcid).get("cid").toString()).child("amode").setValue("no").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        if (complaintlistmap.get(tempposcid).get("dept").toString().equals("")) {
                                            progressDialog.dismiss();
                                            Toast.makeText(mycomplaints.this, "Cannot change into private mode", Toast.LENGTH_LONG).show();
                                        } else {
                                            cffb.child(complaintlistmap.get(tempposcid).get("cid").toString()).child("mode").setValue("private").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    if (cmanarb.isChecked()) {
                                                        cffb.child(complaintlistmap.get(tempposcid).get("cid").toString()).child("amode").setValue("yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                    } else {
                                                        cffb.child(complaintlistmap.get(tempposcid).get("cid").toString()).child("amode").setValue("no").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                            cmbsd.setContentView(view);
                            cmbsd.show();
                            break;
                        case 2:
                            final AlertDialog.Builder delcomp=new AlertDialog.Builder(mycomplaints.this);
                            delcomp.setMessage("Are you sure?");
                            delcomp.setTitle("Delete");
                            delcomp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(mycomplaints.this);
                                    progressDialog.setMessage("Loading...");
                                    progressDialog.show();
                                    cffb.child(tempcid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            cffb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    complaintlistmap = new ArrayList<>();
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
                                                    storageReference.delete();
                                                    complaintlistviews.setAdapter(new complaintlistadapter(complaintlistmap, smf));
                                                    //setListViewHeightBasedOnItems(complaintlistviews);
                                                    progressDialog.dismiss();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                }
                            });
                            delcomp.create().show();
                            break;
                    }
                    return true;
                }
            });

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
    public int searchcidlist(ArrayList<HashMap<String,Object>> _templistmap,String _tempcid){
        int _temppos=0;
        for (int i10=0;i10<_templistmap.size();i10++){
            if(_templistmap.get(i10).get("cid").toString().equals(_tempcid)){
                _temppos=i10;
            }
        }
        return _temppos;
    }

}
