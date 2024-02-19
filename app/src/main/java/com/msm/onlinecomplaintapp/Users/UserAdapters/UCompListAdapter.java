package com.msm.onlinecomplaintapp.Users.UserAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msm.onlinecomplaintapp.Common.DateFormatUtils;
import com.msm.onlinecomplaintapp.Interfaces.OnListItemRemoveListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivities.homepage;
import com.msm.onlinecomplaintapp.Users.UserActivities.totcompdesc;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.msm.onlinecomplaintapp.Users.UserMenus.UserClosedMenu;
import com.msm.onlinecomplaintapp.Users.UserMenus.UserOpenMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UCompListAdapter extends BaseAdapter {
    private int page;
    private Context mcontext;
    private int mresource;
    private int sm;
    private List<Complaint> complaintList=new ArrayList<>();
    private List<String> supportList=new ArrayList<>();
    private List<Complaint> objectsList=new ArrayList<>();
    private Activity activity;
    private String uid;

    private FirebaseFirestore database=FirebaseFirestore.getInstance();

    public static final String COMPLAINT_DB_KEY="Complaints";
    public static final String USERS_DB_KEY = "Users";
    public static final String SUPP_DB_KEY = "Support";

    public UCompListAdapter(Context context, int resource, int sm, int page) {
        mcontext=context;
        mresource=resource;
        this.sm=sm;
        this.page=page;
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public int getCount() {
        return complaintList.size();
    }

    @Override
    public Complaint getItem(int position) {
        return complaintList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(mresource,null);
        final TextView comptitletext = convertView.findViewById(R.id.comptitletext);
        final TextView comptimetext = convertView.findViewById(R.id.comptimetext);
        final TextView compdesctext=convertView.findViewById(R.id.compdesctext);
        final TextView supportnotext=convertView.findViewById(R.id.supportnotext);
        final LinearLayout complistmainlinear=convertView.findViewById(R.id.complistmainlinear);
        final ImageView complistimage=convertView.findViewById(R.id.complistimage);
        final CircleImageView authorimageview=convertView.findViewById(R.id.authorimageview);
        final TextView authortext=convertView.findViewById(R.id.authortext);
        final TextView statustextview=convertView.findViewById(R.id.statustextview);
        final ToggleButton supportbutton=convertView.findViewById(R.id.supportbutton);

        comptitletext.setText(complaintList.get(position).getTitle());
        comptitletext.setAllCaps(true);
        comptimetext.setText(DateFormatUtils.getRelativeTimeSpanStringShort(mcontext,complaintList.get(position).getTime().toDate().getTime()));
        compdesctext.setText(complaintList.get(position).getDesc());
        supportnotext.setText("Support:"+complaintList.get(position).getSupportno());
        if(complaintList.get(position).getCiuri()!=""){
            Glide.with(mcontext.getApplicationContext()).load(complaintList.get(position).getCiuri()).into(complistimage);
            Glide.with(mcontext.getApplicationContext()).load(complaintList.get(position).getCiuri()).into(complistimage);
        }
        else {
            complistimage.setVisibility(View.GONE);
        }

        if(complaintList.get(position).getAcm().equals("0")){
            statustextview.setText("Registered");
            statustextview.setCompoundDrawablesWithIntrinsicBounds(mcontext.getResources().getDrawable(R.drawable.ic_registered_grey_18dp),null,null,null);
        }
        else if(complaintList.get(position).getAcm().equals("1")){
            statustextview.setText("Under Watch");
            statustextview.setCompoundDrawablesWithIntrinsicBounds(mcontext.getResources().getDrawable(R.drawable.ic_underwatch_18dp),null,null,null);
        }
        else if (complaintList.get(position).getAcm().equals("2")){
            statustextview.setText("Resolved");
            statustextview.setCompoundDrawablesWithIntrinsicBounds(mcontext.getResources().getDrawable(R.drawable.ic_solved_18dp),null,null,null);
        }
        else {
            statustextview.setText("Ignored");
            statustextview.setCompoundDrawablesWithIntrinsicBounds(mcontext.getResources().getDrawable(R.drawable.ic_ignored_18dp),null,null,null);
        }

        if(page==((UserActivity)mcontext).getPageHome()) {
            supportbutton.setVisibility(View.VISIBLE);
            if (supportList.contains(complaintList.get(position).getCid())) {
                supportbutton.setChecked(true);
            } else {
                supportbutton.setChecked(false);
            }
            supportbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (supportbutton.isChecked()) {
                        supportList.add(complaintList.get(position).getCid());
                        complaintList.get(position).setSupportno(complaintList.get(position).getSupportno()+1);
                        supportnotext.setText("Support:"+String.valueOf(complaintList.get(position).getSupportno()));
                        final Map<String, Object> map = new HashMap<>();
                        map.put("time", Timestamp.now());
                        database.collection(USERS_DB_KEY).document(uid).collection(SUPP_DB_KEY).document(complaintList.get(position).getCid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                }
                        });
                        database.collection(COMPLAINT_DB_KEY).document(complaintList.get(position).getCid()).update("supportno", complaintList.get(position).getSupportno() );


                    } else {
                        supportList.remove(complaintList.get(position).getCid());
                        complaintList.get(position).setSupportno(complaintList.get(position).getSupportno()-1);
                        supportnotext.setText("Support:"+String.valueOf(complaintList.get(position).getSupportno()));
                        database.collection(USERS_DB_KEY).document(uid).collection(SUPP_DB_KEY).document(complaintList.get(position).getCid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               }
                        });
                        database.collection(COMPLAINT_DB_KEY).document(complaintList.get(position).getCid()).update("supportno", complaintList.get(position).getSupportno());

                    }
                }
            });
        }

        if(page==((UserActivity)mcontext).getPageHome()){
            complistmainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(mcontext, totcompdesc.class);
                    intent.putExtra("cuComplaint",complaintList.get(position).toHashMap());
                    intent.putExtra("sbe",true);
                    if (supportList.contains(complaintList.get(position).getCid())){
                        intent.putExtra("sbp",true);
                    }
                    else { 
                        intent.putExtra("sbp",false);
                    }
                    ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((homepage)mcontext,comptitletext,"trans1");
                    ((homepage) mcontext).startActivityForResult(intent,6,activityOptionsCompat.toBundle());
                }
            });
        }

        if(page==((UserActivity)mcontext).getPageOpenComplaints()){
            complistmainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserOpenMenu openMenu=new UserOpenMenu(mcontext,R.style.BottomSheetDialog,complaintList.get(position),comptitletext);
                    View view=openMenu.getLayoutInflater().inflate(R.layout.user_open_menu,null);
                    openMenu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    openMenu.setContentView(view);
                    openMenu.setItemRemoveListener(new OnListItemRemoveListener() {
                        @Override
                        public void onItemRemoved(Object item) {
                            complaintList.remove(item);
                            notifyDataSetChanged();
                        }
                    });
                    openMenu.show();
                }
            });
        }

        if(page==((UserActivity)mcontext).getPageClosedComplaints()){
            complistmainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserClosedMenu closedMenu=new UserClosedMenu(mcontext,R.style.BottomSheetDialog,complaintList.get(position),comptitletext);
                    View view=closedMenu.getLayoutInflater().inflate(R.layout.user_open_menu,null);
                    closedMenu.setContentView(view);
                    closedMenu.setItemRemoveListener(new OnListItemRemoveListener() {
                        @Override
                        public void onItemRemoved(Object item) {
                            complaintList.remove(item);
                            notifyDataSetChanged();
                        }
                    });
                    closedMenu.show();
                }
            });
        }

        if(page==((UserActivity)mcontext).getPagePublicArchives()){
            complistmainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(mcontext, totcompdesc.class);
                    intent.putExtra("cuComplaint",complaintList.get(position).toHashMap());
                    intent.putExtra("sbe",false);
                    intent.putExtra("sbp",false);
                    ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((homepage)mcontext,comptitletext,"trans1");
                    ((homepage) mcontext).startActivityForResult(intent,6,activityOptionsCompat.toBundle());
                }
            });
        }

        return convertView;
    }

    public void setList(List<Complaint> list){
        complaintList=list;
        notifyDataSetChanged();
    }

    public List<String> getSupportList(){
        return supportList;
    }

    public  void setSupportList(List<String> supportList){
        this.supportList=supportList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
