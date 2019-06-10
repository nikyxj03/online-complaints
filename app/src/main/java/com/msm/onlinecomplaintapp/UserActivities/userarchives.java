package com.msm.onlinecomplaintapp.UserActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.DepartmentActivities.depttotcompdec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class userarchives extends AppCompatActivity {

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

    private ArrayList<HashMap<String,Object>> udlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> acomplaintlistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> tempmap2=new HashMap<>();
    private ArrayList<String> cidsortlist=new ArrayList<>();

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;
    private Button uasortbutton;
    private ListView archivelistview1;

    private int smf=0;
    private String uid="";

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference udfb=_database.getReference("userdata");
    private DatabaseReference cffb=_database.getReference("complaint");

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
                this.finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userarchives);
        Initialize();
        InitializeLogic();
    }

    private void Initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        uasortbutton=findViewById(R.id.uasortbutton1);
        homebutton=findViewById(R.id.homebutton);
        mycomplaintsbutton=findViewById(R.id.mycomplaintsbutton);
        settingsbutton=findViewById(R.id.settingsbutton);
        archivebutton=findViewById(R.id.archivebutton);
        notificationbutton=findViewById(R.id.notification_button);
        logoutbutton=findViewById(R.id.logoutbutton);
        archivelistview1=findViewById(R.id.archivelistview1);

        mainintent.setClass(this,MainActivity.class);
        newcompintent.setClass(this,newcomplaint.class);
        settingsintent.setClass(this,settingsactivity.class);
        homeintent.setClass(this,homepage.class);
        mycomplaintintent.setClass(this,mycomplaints.class );
        notificationintent.setClass(this,notifictationsactivity.class );
        archiveintent.setClass(this,userarchives.class );
        tcdintent.setClass(this,depttotcompdec.class );

        popup = new PopupMenu(this,uasortbutton);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sortmenu, popup.getMenu());

        uasortbutton.setOnClickListener(new View.OnClickListener() {
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
                archivelistview1.setAdapter(new complaintlistadapter(acomplaintlistmap,smf));
                setListViewHeightBasedOnItems(archivelistview1);
                return false;
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
                                acomplaintlistmap=new ArrayList<>();
                                for (int i25=0;i25<complaintlistmap.size();i25++){
                                    if(complaintlistmap.get(i25).get("mode").toString().equals("public") && complaintlistmap.get(i25).get("acm").toString().equals("1")){
                                        acomplaintlistmap.add(complaintlistmap.get(i25));
                                    }
                                }

                                archivelistview1.setAdapter(new complaintlistadapter(acomplaintlistmap,smf));
                                setListViewHeightBasedOnItems(archivelistview1);
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
                cv_view=cv_inflater.inflate(R.layout.deptcompistcustom,null);
            }

            final TextView comptitletext = cv_view.findViewById(R.id.comptitletext1);
            final TextView comptimetext = cv_view.findViewById(R.id.comptimetext1);
            final TextView compdesctext=cv_view.findViewById(R.id.compdesctext1);
            final TextView supportnotext=cv_view.findViewById(R.id.supportnotext1);
            final LinearLayout complistmainlinear=cv_view.findViewById(R.id.complistmainlinear1);
            final ImageView complistimage=cv_view.findViewById(R.id.complistimage1);

            int tf2=0;

            complistmainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tcdintent.putExtra("cid",_data.get(ist).get("cid").toString());
                    ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(userarchives.this,comptitletext,"trans2");
                    startActivityForResult(tcdintent,REQUEST_CODE_TCD,activityOptionsCompat.toBundle());
                }
            });


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
