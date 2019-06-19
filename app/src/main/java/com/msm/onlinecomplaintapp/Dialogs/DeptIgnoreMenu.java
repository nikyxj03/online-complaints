package com.msm.onlinecomplaintapp.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.msm.onlinecomplaintapp.Others.BitMapLoader;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Others.ImageConverter;
import com.msm.onlinecomplaintapp.Interfaces.CDOnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Interfaces.OnLoad;
import com.msm.onlinecomplaintapp.Models.AdminRequests;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;
import  com.msm.onlinecomplaintapp.DepartmentActivities.*;
import com.msm.onlinecomplaintapp.Others.RandomStringBuilder;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeptIgnoreMenu extends BottomSheetDialog {

    private Context mContext;
    private Button aamviewcompbutton;
    private Button aamunarchivebutton;
    private Button aamdonebutton;
    private CircleImageView aamcompimage;
    private Complaint cuComplaint;
    private View mPassedView;
    private String rid="";

    public DeptIgnoreMenu(Context context, Complaint complaint, View passedView) {
        super(context);
        mContext=context;
        cuComplaint=complaint;
        mPassedView=passedView;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        aamviewcompbutton=view.findViewById(R.id.aamviewcompbutton);
        aamcompimage=view.findViewById(R.id.aamimage);
        aamunarchivebutton=view.findViewById(R.id.aamunarchivebutton);
        aamdonebutton=view.findViewById(R.id.aamdonebutton);
        final Activity activity=(DeptArchives)mContext;
        if(cuComplaint.getCiuri()==""){

        }
        else {
            BitMapLoader bitMapLoader=new BitMapLoader(cuComplaint.getCiuri(), new OnLoad() {
                @Override
                public void OnLoaded(Bitmap loadedBitmap) {
                    Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(loadedBitmap, 100);
                    aamcompimage.setImageBitmap(circularBitmap);
                }
            });
            bitMapLoader.execute();
        }

        aamviewcompbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent=new Intent();
                intent.setClass(mContext,depttotcompdec.class);
                intent.putExtra("cid",cuComplaint.getCid() );
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,mPassedView,"trans1");
                mContext.startActivity(intent,activityOptionsCompat.toBundle());
            }
        });
        aamunarchivebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final ConfirmationDialog confirmationDialog=new ConfirmationDialog(mContext,"RE-INSTATE","Are you Sure you want to request the re-initiation of complaint?" );
                View view1= confirmationDialog.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                confirmationDialog.setContentView(view1);
                confirmationDialog.show();
                confirmationDialog.choicelistener(new CDOnClick() {
                    @Override
                    public void OnButtonClicked(boolean choice) {
                        confirmationDialog.dismiss();
                        if(choice){
                            ((DeptPublicArchives) mContext).showProgress("Loading");
                            RandomStringBuilder randomStringBuilder=new RandomStringBuilder("rid",12 );
                            randomStringBuilder.getRandomId(new OnDataSFetchListener<String>() {
                                @Override
                                public void onDataSFetch(String s) {
                                    if(s!=null) {
                                        rid = s;
                                        AdminRequests adminRequests = new AdminRequests();
                                        adminRequests.setCid(cuComplaint.getCid());
                                        adminRequests.setRid(rid);
                                        adminRequests.setStatus("0");
                                        adminRequests.setType("3");
                                        adminRequests.setUid(cuComplaint.getUid());
                                        GlobalApplication.databaseHelper.updateRequest(adminRequests, new OnDataUpdatedListener() {
                                            @Override
                                            public void onDataUploaded(boolean success) {
                                                ((DeptPublicArchives) mContext).hideProgress();
                                                if(success){
                                                    Toast.makeText(mContext,"Request Posted" ,Toast.LENGTH_LONG ).show();
                                                }
                                                else {
                                                    Toast.makeText(mContext,"Request Failed" ,Toast.LENGTH_LONG ).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });
        aamdonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }


}
