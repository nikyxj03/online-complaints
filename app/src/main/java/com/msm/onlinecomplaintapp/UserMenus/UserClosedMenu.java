package com.msm.onlinecomplaintapp.UserMenus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.msm.onlinecomplaintapp.Common.BitMapLoader;
import com.msm.onlinecomplaintapp.Common.ConfirmationDialog;
import com.msm.onlinecomplaintapp.Common.ImageConverter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.CDOnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Interfaces.OnLoad;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.UserActivities.mycomplaints;
import com.msm.onlinecomplaintapp.UserActivities.totcompdesc;
import com.msm.onlinecomplaintapp.UserActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserClosedMenu extends BottomSheetDialog {
    private Context mContext;
    private Button ucmviewcompbutton;
    private Button ucmchangemodebutton;
    private Button ucmmsglogsbutton;
    private Button ucmrqstrevokebutton;
    private Button ucmdeletebutton;
    private Button ucmdonebutton;
    private CircleImageView ucmcompimage;
    private Complaint cuComplaint;
    private View mPassedView;

    public UserClosedMenu(Context context,Complaint complaint,View passedView) {
        super(context);
        mContext=context;
        cuComplaint=complaint;
        mPassedView=passedView;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ucmchangemodebutton=view.findViewById(R.id.ucmchangemodebutton);
        ucmviewcompbutton=view.findViewById(R.id.ucmviewcompbutton);
        ucmmsglogsbutton=view.findViewById(R.id.ucmmsglogsbutton);
        ucmrqstrevokebutton=view.findViewById(R.id.ucmrqstrevokebutton);
        ucmdeletebutton=view.findViewById(R.id.ucmdeletebutton);
        ucmdonebutton=view.findViewById(R.id.ucmdonebutton);
        ucmcompimage=view.findViewById(R.id.ucmimage);

        if(cuComplaint.getCriuri()==null || cuComplaint.getCriuri().equals("")){
            ucmcompimage.setVisibility(View.GONE);
        }
        else {
            Glide.with(mContext.getApplicationContext()).load(cuComplaint.getCriuri()).placeholder(R.drawable.ic_launcher_foreground).into(ucmcompimage);
        }

        ucmviewcompbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent=new Intent();
                intent.setClass(mContext, totcompdesc.class);
                intent.putExtra("cuComplaint",cuComplaint.toHashMap());
                intent.putExtra("sbe",false);
                intent.putExtra("sbp",false);
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((mycomplaints)mContext,mPassedView,"trans2");
                ((mycomplaints) mContext).startActivityForResult(intent,6,activityOptionsCompat.toBundle());
            }
        });

        ucmchangemodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final Dialog dialog=new Dialog(mContext);
                View view1=dialog.getLayoutInflater().inflate(R.layout.changemodelayout,null);
                dialog.setContentView(view1);
                dialog.getWindow().getAttributes().windowAnimations=R.style.DialogSlide;
                final Button cmapplybutton=view1.findViewById(R.id.cmapplybutton);
                final RadioButton cmpublicrb=view1.findViewById(R.id.cmpublicrb);
                final RadioButton cmprivaterb=view1.findViewById(R.id.cmprivaterb);
                final RadioButton cmanarb=view1.findViewById(R.id.cmanarb);
                final RadioButton cmnanarb=view1.findViewById(R.id.cmnanarb);
                final RadioGroup cmarg=view1.findViewById(R.id.cmarg);
                final RadioGroup cmprg=view1.findViewById(R.id.cmprg);
                if(cuComplaint.getMode().equals("public")){
                    cmpublicrb.setChecked(true);
                }
                else {
                    cmprivaterb.setChecked(true);
                }
                if(cuComplaint.getAmode().equals("yes")){
                    cmanarb.setChecked(true);
                }
                else {
                    cmnanarb.setChecked(true);
                }
                cmapplybutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ((UserActivity)mContext).showProgress("Changing Mode...");
                        if(cmpublicrb.isChecked()){
                            cuComplaint.setMode("public");
                        }
                        else {
                            cuComplaint.setMode("private");
                        }
                        if(cmanarb.isChecked()){
                            cuComplaint.setAmode("yes");
                        }
                        else {
                            cuComplaint.setAmode("no");
                        }
                        GlobalApplication.databaseHelper.updateComplaint(cuComplaint, new OnDataUpdatedListener() {
                            @Override
                            public void onDataUploaded(boolean success) {
                                ((UserActivity)mContext).hideProgress();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        ucmdeletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final ConfirmationDialog confirmationDialog=new ConfirmationDialog(mContext,"Delete");
                View view1=confirmationDialog.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                confirmationDialog.setContentView(view1);
                confirmationDialog.show();
                confirmationDialog.choicelistener(new CDOnClick() {
                    @Override
                    public void OnButtonClicked(boolean choice) {
                        confirmationDialog.dismiss();
                        if(choice){
                            ((UserActivity)mContext).showProgress("Deleting...");
                            GlobalApplication.databaseHelper.deleteComplaint(cuComplaint.getCid(), new OnDataUpdatedListener() {
                                @Override
                                public void onDataUploaded(boolean success) {
                                    ((UserActivity)mContext).hideProgress();
                                }
                            });
                        }
                    }
                });
            }
        });

        ucmdonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
