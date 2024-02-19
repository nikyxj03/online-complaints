package com.msm.onlinecomplaintapp.Users.UserActivities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.protobuf.ByteString;
import com.msm.onlinecomplaintapp.Common.CloudFunctionHelper;
import com.msm.onlinecomplaintapp.Common.ImageConverter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.DeptUsers;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.msm.onlinecomplaintapp.Users.UserAdapters.UCompListAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import co.ceryle.segmentedbutton.SegmentedButton;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import de.hdodenhof.circleimageview.CircleImageView;

public class homepage extends UserActivity {

    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private int uidpos=0;

    private TextView profileEmailText;
    private TextView profileNameText;
    private CircleImageView profilePhoto;

    private Button homebutton;
    private Button logoutbutton;
    private Button settingsbutton;
    private Button mycomplaintsbutton;
    private Button archivebutton;
    private Button notificationbutton;
    private Button querybutton;
    private ListView complaintlistview;
    private FloatingActionButton ncfbutton;
    private SegmentedButtonGroup homesbg;
    private SegmentedButton homesb1;
    private SegmentedButton homesb2;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout mainlayout;
    private com.github.clans.fab.FloatingActionButton newComplaintButton;
    private com.github.clans.fab.FloatingActionButton newQueryButton;
    private com.github.clans.fab.FloatingActionButton publishNewsButton;
    private FloatingActionMenu newMenu;

    private ViewGroup viewGroup;

    private Toolbar toolbar;

    private List<Complaint> stcomplaintList=new ArrayList<>();
    private List<Complaint> sscomplaintList=new ArrayList<>();
    private List<String> uriList=new ArrayList<>();
    private List<Bitmap> bitmaps=new ArrayList<>();
    private List<String> stsupportList =new ArrayList<>();
    private List<String> sssupportList =new ArrayList<>();

    private UCompListAdapter uCompListAdapter;

    private Users users;

    private int smf=0;
    private String uid="";

    private int i;

    @Override
    public void onBackPressed() {
        if(_drawer.isDrawerOpen(GravityCompat.START)){
            _drawer.closeDrawer(GravityCompat.START);
        }
        else if(newMenu.isOpened())
        {
            newMenu.close(true);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId()==R.id.item1){
            smf=0;
            uCompListAdapter.setSupportList(stsupportList);
            uCompListAdapter.setList(stcomplaintList);
        }
        if(item.getItemId()==R.id.item2){
            smf=1;
            uCompListAdapter.setSupportList(sssupportList);
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
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_QUERY){
                                    queryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,queryintent);
                                }
                                else {
                                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                        mainintent.putExtra("key1", "logout");
                                        setResult(RESULT_OK, mainintent);
                                    }
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

        profileEmailText=findViewById(R.id.user_profile_email);
        profileNameText=findViewById(R.id.user_profile_name);
        profilePhoto=findViewById(R.id.user_profile_photo);

        complaintlistview = findViewById(R.id.complaintlistview);
        homebutton = findViewById(R.id.homebutton);
        mycomplaintsbutton = findViewById(R.id.mycomplaintsbutton);
        settingsbutton = findViewById(R.id.settingsbutton);
        archivebutton = findViewById(R.id.archivebutton);
        notificationbutton = findViewById(R.id.notification_button);
        querybutton=findViewById(R.id.myqueriesbutton);
        logoutbutton = findViewById(R.id.logoutbutton);

        ncfbutton = findViewById(R.id.ncfbutton);
        homesbg=findViewById(R.id.home_list_type);
        homesb1=findViewById(R.id.sgb1);
        homesb2=findViewById(R.id.sgb2);
        refreshLayout=findViewById(R.id.u_h_refresh);
        mainlayout=findViewById(R.id.u_h_layout);
        newComplaintButton=findViewById(R.id.fab_new_complaint);
        newQueryButton=findViewById(R.id.fab_new_query);
        publishNewsButton=findViewById(R.id.fab_new_news);
        newMenu=findViewById(R.id.fab_new_menu);


        viewGroup=(ViewGroup)mainlayout;
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

        querybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(queryintent, REQUEST_CODE_QUERY);
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
                                if(getIntent().getIntExtra("pp", 0) == REQUEST_CODE_QUERY){
                                    queryintent.putExtra("ac","lo");
                                    setResult(RESULT_OK,queryintent);
                                }
                                else {
                                    if (getIntent().getIntExtra("pp", 0) == REQUEST_CODE_MAIN) {
                                        mainintent.putExtra("key1", "logout");
                                        setResult(RESULT_OK, mainintent);
                                    }
                                }
                            }
                        }


                    }
                }
                homepage.this.finish();
            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String s="Hello";
                byte[] bytes=s.getBytes();
                try {
                    bytes=s.getBytes("UTF-8");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                byte[] encrypted;
                String s1;
                try {
                    encrypted=encrypt("onlinecomplaints-49234","global","test", "quikstart",bytes);
                    s1=new String(encrypted, StandardCharsets.UTF_8);
                    Toast.makeText(homepage.this,s1,Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                */
            }
        });

        uCompListAdapter = new UCompListAdapter(this, R.layout.pchlistcustom, smf, PAGE_HOME);
        complaintlistview.setAdapter(uCompListAdapter);

        GlobalApplication.databaseHelper.fetchUserData(getCurrentUserId(), new OnDataSFetchListener<Users>() {
            @Override
            public void onDataSFetch(Users users) {
                profileEmailText.setText(users.getEmail());
                profileNameText.setText(users.getFullname());
                if(users.getProfilePhoto()!=null)
                Glide.with(homepage.this).asBitmap().load(users.getProfilePhoto()).placeholder(R.mipmap.ananymous_person_round).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        profilePhoto.setImageBitmap(ImageConverter.getRoundedCornerBitmap(resource,100));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }
        });

       updateData();

       refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               updateData();
               enableTouch(false);
           }
       });

        ncfbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        newComplaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMenu.close(true);
                Intent intent=new Intent();
                intent.setClass(homepage.this,newcomplaint.class);
                startActivity(intent);
            }
        });

        newQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMenu.close(true);
                Intent intent=new Intent();
                intent.setClass(homepage.this,NewQueryActivity.class);
                startActivity(intent);
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

    public void updateData(){
        GlobalApplication.databaseHelper.getUserSupportList(getCurrentUserId(), new OnDataFetchListener<String>() {
            @Override
            public void onDataFetched(List<String> strings) {
                stsupportList = strings;
                sssupportList= strings;
                GlobalApplication.databaseHelper.getPublicComplaintsST(new OnDataFetchListener<Complaint>() {
                    @Override
                    public void onDataFetched(List<Complaint> complaints) {
                        if (complaints != null) {
                            stcomplaintList = complaints;
                            if (stcomplaintList != null)
                                if (smf == 0){
                                    uCompListAdapter.setSupportList(stsupportList);
                                    uCompListAdapter.setList(stcomplaintList);
                                    hideProgress();
                                    refreshLayout.setRefreshing(false);
                                    enableTouch(true);
                                }
                        }
                    }
                });

                GlobalApplication.databaseHelper.getPublicComplaintsSS(new OnDataFetchListener<Complaint>() {
                    @Override
                    public void onDataFetched(List<Complaint> complaints) {
                        sscomplaintList = complaints;
                        if (sscomplaintList != null)
                            if (smf == 1){
                                uCompListAdapter.setSupportList(sssupportList);
                                uCompListAdapter.setList(sscomplaintList);
                                hideProgress();
                                refreshLayout.setRefreshing(false);
                                enableTouch(true);
                            }
                    }
                });
            }
        });
    }

    public void enableTouch(final boolean b){
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !b;
            }
        });
    }

   /* public static byte[] encrypt(
            String projectId, String locationId, String keyRingId, String cryptoKeyId, byte[] plaintext)
            throws IOException {

        // Create the KeyManagementServiceClient using try-with-resources to manage client cleanup.
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {

            // The resource name of the cryptoKey
            String resourceName = CryptoKeyName.format(projectId, locationId, keyRingId, cryptoKeyId);

            // Encrypt the plaintext with Cloud KMS.
            EncryptResponse response = client.encrypt(resourceName, ByteString.copyFrom(plaintext));

            // Extract the ciphertext from the response.
            return response.getCiphertext().toByteArray();
        }
    }*/
}
