package com.msm.onlinecomplaintapp.UserActivities;

import android.animation.Animator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.msm.onlinecomplaintapp.Common.BitMapLoader;
import com.msm.onlinecomplaintapp.Common.UriToBitmapExecutor;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnLoad;
import com.msm.onlinecomplaintapp.LoginActivities.LoginActivity;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.UserActivity;
import com.msm.onlinecomplaintapp.UserAdapters.UCompListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButton;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class homepage extends UserActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private int uidpos=0;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;
    private ListView complaintlistview;
    private FloatingActionButton ncfbutton;
    private TextView profilename;
    private TextView profileemail;
    private SegmentedButtonGroup homesbg;
    private SegmentedButton homesb1;
    private SegmentedButton homesb2;

    private Toolbar toolbar;

    private List<Complaint> stcomplaintList=new ArrayList<>();
    private List<Complaint> sscomplaintList=new ArrayList<>();
    private List<String> uriList=new ArrayList<>();
    private List<Bitmap> bitmaps=new ArrayList<>();
    private List<String> supportList =new ArrayList<>();

    private UCompListAdapter uCompListAdapter;

    private Users users;

    private int smf=0;
    private String uid="";

    private int i;

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
        if(item.getItemId()==R.id.item1){
            smf=0;
            uCompListAdapter.setList(stcomplaintList);
        }
        if(item.getItemId()==R.id.item2){
            smf=1;
            uCompListAdapter.setList(sscomplaintList);
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
        if(requestCode!=REQUEST_CODE_TCD) {
            if (resultCode == RESULT_OK) {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MYCOMPLAINT) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK, mycomplaintintent);
                    } else {
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES) {
                            archiveintent.putExtra("ac", "lo");
                            setResult(RESULT_OK, archiveintent);
                        } else {
                            if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_NOTIFICATION) {
                                notificationintent.putExtra("ac", "lo");
                                setResult(RESULT_OK, notificationintent);
                            } else {
                                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                    mainintent.putExtra("key1", "logout");
                                    setResult(RESULT_OK, mainintent);
                                }
                            }
                        }


                    }
                }
                homepage.this.finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        toolbar=findViewById(R.id.u_h_toolbar);

        setSupportActionBar(toolbar);

        _drawer = findViewById(R.id._drawer);
        _toggle = new ActionBarDrawerToggle(homepage.this, _drawer, R.string.open, R.string.close);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgress("Loading...");

        setintents(this);

        complaintlistview = findViewById(R.id.complaintlistview);
        homebutton = findViewById(R.id.homebutton);
        mycomplaintsbutton = findViewById(R.id.mycomplaintsbutton);
        settingsbutton = findViewById(R.id.settingsbutton);
        archivebutton = findViewById(R.id.archivebutton);
        notificationbutton = findViewById(R.id.notification_button);
        logoutbutton = findViewById(R.id.logoutbutton);
        ncfbutton = findViewById(R.id.ncfbutton);
        profileemail = findViewById(R.id.profileemail);
        profilename = findViewById(R.id.profilename);
        homesbg=findViewById(R.id.home_list_type);
        homesb1=findViewById(R.id.sgb1);
        homesb2=findViewById(R.id.sgb2);

        ncfbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(homepage.this,newcomplaint.class);
                startActivity(intent);
            }
        });


        mycomplaintsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mycomplaintintent, REQUEST_CODE_MYCOMPLAINT);
            }
        });

        settingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(settingsintent, REQUEST_CODE_SETTINGS);
            }
        });

        notificationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(notificationintent, REQUEST_CODE_NOTIFICATION);
            }
        });

        archivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(archiveintent, REQUEST_CODE_ARCHIVES);
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_SETTINGS) {
                    settingsintent.putExtra("ac", "lo");
                    setResult(RESULT_OK, settingsintent);
                } else {
                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MYCOMPLAINT) {
                        mycomplaintintent.putExtra("ac", "lo");
                        setResult(RESULT_OK, mycomplaintintent);
                    } else {
                        if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_ARCHIVES) {
                            archiveintent.putExtra("ac", "lo");
                            setResult(RESULT_OK, archiveintent);
                        } else {
                            if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_NOTIFICATION) {
                                notificationintent.putExtra("ac", "lo");
                                setResult(RESULT_OK, notificationintent);
                            } else {
                                if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                    mainintent.putExtra("key1", "logout");
                                    setResult(RESULT_OK, mainintent);
                                }
                            }
                        }


                    }
                }
                homepage.this.finish();
            }
        });

        uCompListAdapter = new UCompListAdapter(this, R.layout.pchlistcustom, smf, PAGE_HOME);
        complaintlistview.setAdapter(uCompListAdapter);


        GlobalApplication.databaseHelper.getUserSupportList(getCurrentUserId(), new OnDataFetchListener<String>() {
            @Override
            public void onDataFetched(List<String> strings) {
                supportList = strings;
                uCompListAdapter.setSupportList(supportList);
                GlobalApplication.databaseHelper.getPublicComplaintsST(new OnDataFetchListener<Complaint>() {
                    @Override
                    public void onDataFetched(List<Complaint> complaints) {
                        if (complaints != null) {
                            stcomplaintList = complaints;
                            if (stcomplaintList != null)
                                if (smf == 0)
                                    uCompListAdapter.setList(stcomplaintList);
                            hideProgress();
                        }
                    }
                });

                GlobalApplication.databaseHelper.getPublicComplaintsSS(new OnDataFetchListener<Complaint>() {
                    @Override
                    public void onDataFetched(List<Complaint> complaints) {
                        sscomplaintList = complaints;
                        if (sscomplaintList != null)
                            if (smf == 1)
                                uCompListAdapter.setList(sscomplaintList);
                            hideProgress();
                    }
                });
            }
        });

        /*complaintlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    hideToolBar();
                else
                    showToolBar();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/

    }

    public void hideToolBar(){
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getSupportActionBar().hide();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void showToolBar(){
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getSupportActionBar().show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}
