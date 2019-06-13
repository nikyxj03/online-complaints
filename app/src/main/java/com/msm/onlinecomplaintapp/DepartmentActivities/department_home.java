package com.msm.onlinecomplaintapp.DepartmentActivities;

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
import android.transition.Transition;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.MainActivity;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class department_home extends DepartmentActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private PopupMenu popup;

    private Button homebutton1;
    private Button logoutbutton1;
    private Button settingsbutton1;
    private Button deptcomplaintsbutton1;
    private Button archivebutton1;
    private Button deptarchivebutton1;
    private Button sortbutton1;
    private ListView complaintlistview1;

    private int smf=0;
    private String uid="";

    private Transition transition;

    private FirebaseAuth vmauth=FirebaseAuth.getInstance();

    private FirebaseDatabase _database=FirebaseDatabase.getInstance();
    private DatabaseReference cffb=_database.getReference("complaint");
    private DatabaseReference dud=_database.getReference("deptuserdata");

    private ArrayList<HashMap<String,Object>> dudlistmap=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> complaintlistmap=new ArrayList<>();
    private HashMap<String,Object> tempmap1=new HashMap<>();
    private HashMap<String,Object> tempmap2=new HashMap<>();
    private ArrayList<String> cidsortlist=new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
            if (resultCode==RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                        deptcomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK, deptcomplaintintent);
                    } else {
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES_D){
                            archiveintent.putExtra("ac","lo");
                            setResult(RESULT_OK,archiveintent);
                        }else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN_D) {
                                    mainintent.putExtra("key1", "logout");
                                    setResult(RESULT_OK, mainintent);
                                }
                            }

                        }

                    }
                }
                department_home.this.finish();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_home);
        initialize();
        initializelogic();
    }

    private void initialize(){
        _drawer= findViewById(R.id._drawer);
        _toggle=new ActionBarDrawerToggle(this,_drawer,R.string.open,R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vmauth=FirebaseAuth.getInstance();

        sortbutton1=findViewById(R.id.sortbutton1);
        complaintlistview1=findViewById(R.id.complaintlistview1);
        homebutton1=findViewById(R.id.homebutton1);
        deptcomplaintsbutton1=findViewById(R.id.deptcomplaintsbutton1);
        settingsbutton1=findViewById(R.id.settingsbutton1);
        archivebutton1=findViewById(R.id.archivebutton1);
        logoutbutton1=findViewById(R.id.logoutbutton1);
        deptarchivebutton1=findViewById(R.id.deptarchivebutton1);

        setintents(this);


        popup = new PopupMenu(department_home.this,sortbutton1);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.sortmenu, popup.getMenu());

        deptcomplaintsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptcomplaintintent, REQUEST_CODE_DeptCOMPLAINT_D);
            }
        });

        archivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent,REQUEST_CODE_ARCHIVES_D  );
            }
        });

        settingsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent,REQUEST_CODE_SETTINGS_D  );
            }
        });

        deptarchivebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(deptarchiveintent,REQUEST_CODE_DEPTARCHIVES_D);
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
                complaintlistview1.setAdapter(new complaintlistadapter(complaintlistmap,smf));
                return false;
            }
        });

        logoutbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS_D) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DeptCOMPLAINT_D) {
                        deptcomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK, deptcomplaintintent);
                    } else {
                        if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES_D){
                            archiveintent.putExtra("ac","lo");
                            setResult(RESULT_OK,archiveintent);
                        }else {
                            if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_DEPTARCHIVES_D){
                                deptarchiveintent.putExtra("ac","lo");
                                setResult(RESULT_OK,deptarchiveintent);
                            }
                            else {
                                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN_D) {
                                    mainintent.putExtra("key1", "logout");
                                    setResult(RESULT_OK, mainintent);
                                }
                            }

                        }

                    }
                }
                department_home.this.finish();
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
                        complaintlistview1.setAdapter(new complaintlistadapter(complaintlistmap,smf));

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
                        complaintlistview1.setAdapter(new complaintlistadapter(complaintlistmap,smf));
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
                        complaintlistview1.setAdapter(new complaintlistadapter(complaintlistmap,smf));

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
                        complaintlistview1.setAdapter(new department_home.complaintlistadapter(complaintlistmap,smf));

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
                } catch (Exception _e) {
                    _e.printStackTrace();
                }

                complaintlistview1.setAdapter(new department_home.complaintlistadapter(complaintlistmap, smf));

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
    });
    }

    public class complaintlistadapter extends BaseAdapter {

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
                    _data.add(_arr.get(temppos));
                    cidsortlist.add(_arr.get(temppos).get("cid").toString());
                    mtdlist.remove(temppos1);
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
                    _data.add(_arr.get(temppos));
                    cidsortlist.add(_arr.get(temppos).get("cid").toString());
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

            int tf2=0;

            complistmainlinear1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tcdintent.putExtra("cid",_data.get(ist).get("cid").toString());
                    tcdintent.putExtra("tiltle",_data.get(ist).get("title").toString());
                    ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(department_home.this,comptitletext1,"trans1");
                    startActivity(tcdintent,activityOptionsCompat.toBundle());
                    //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });


            if(_data.get(_data.size()-ist-1).get("mode").toString().equals("public")){
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

            }
            else{
                complistmainlinear1.setVisibility(View.GONE);
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
