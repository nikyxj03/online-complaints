package com.msm.onlinecomplaintapp.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msm.onlinecomplaintapp.DepartmentActivities.DeptArchives;
import com.msm.onlinecomplaintapp.DepartmentActivities.deptcomplaints;
import com.msm.onlinecomplaintapp.DepartmentActivities.depttotcompdec;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.CDOnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Interfaces.OnLoad;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.Others.BitMapLoader;
import com.msm.onlinecomplaintapp.Others.ImageConverter;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeptRegisteredMenu extends BottomSheetDialog {

    private Context mContext;
    private Button drmviewcompbutton;
    private Button drmwatchbutton;
    private Button drmignorebutton;
    private Button drmforwardbutton;
    private Button drmdonebutton;
    private CircleImageView drmcompimage;
    private Complaint cuComplaint;
    private View mPassedView;
    private String rid="";
    private List<Departments> departmentsList;


    public DeptRegisteredMenu(Context context,Complaint complaint,View passedView,List<Departments> departments) {
        super(context);
        mContext=context;
        cuComplaint=complaint;
        mPassedView=passedView;
        departmentsList=departments;
    }

    @Override
    public void setContentView(final View view) {
        super.setContentView(view);
        drmviewcompbutton=view.findViewById(R.id.drmviewcompbutton);
        drmwatchbutton=view.findViewById(R.id.drmwatchbutton);
        drmignorebutton=view.findViewById(R.id.drmignorebutton);
        drmforwardbutton=view.findViewById(R.id.drmforwardbutton);
        drmdonebutton=view.findViewById(R.id.drmdonebutton);
        drmcompimage=view.findViewById(R.id.drmimage);

        final Activity activity=(deptcomplaints)mContext;
        if(cuComplaint.getCiuri()==""){

        }
        else {
            BitMapLoader bitMapLoader=new BitMapLoader(cuComplaint.getCiuri(), new OnLoad() {
                @Override
                public void OnLoaded(Bitmap loadedBitmap) {
                    Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(loadedBitmap, 100);
                    drmcompimage.setImageBitmap(circularBitmap);
                }
            });
            bitMapLoader.execute();
        }

        drmviewcompbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent=new Intent();
                intent.setClass(mContext, depttotcompdec.class);
                intent.putExtra("cid",cuComplaint.getCid() );
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,mPassedView,"trans1");
                mContext.startActivity(intent,activityOptionsCompat.toBundle());
            }
        });

        drmwatchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final ConfirmationDialog confirmationDialog=new ConfirmationDialog(mContext,"Accept Complaint","Are you sure you want to accept complaint?");
                View view1= confirmationDialog.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                confirmationDialog.setContentView(view1);
                confirmationDialog.show();
                confirmationDialog.choicelistener(new CDOnClick() {
                    @Override
                    public void OnButtonClicked(boolean choice) {
                        confirmationDialog.dismiss();
                        if(choice){
                            ((deptcomplaints) activity).showProgress("Loading...");
                            cuComplaint.setAcm("1");
                            GlobalApplication.databaseHelper.updateComplaint(cuComplaint, new OnDataUpdatedListener() {
                                @Override
                                public void onDataUploaded(boolean success) {
                                    ((deptcomplaints) activity).hideProgress();
                                }
                            });

                        }
                    }
                });
            }
        });
        drmignorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final ConfirmationDialog confirmationDialog=new ConfirmationDialog(mContext,"Ignore Complaint","Are you sure you want to ignore complaint?");
                View view1= confirmationDialog.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                confirmationDialog.setContentView(view1);
                confirmationDialog.show();
                confirmationDialog.choicelistener(new CDOnClick() {
                    @Override
                    public void OnButtonClicked(boolean choice) {
                        confirmationDialog.dismiss();
                        if(choice){
                            ((deptcomplaints) activity).showProgress("Loading...");
                            cuComplaint.setAcm("3");
                            cuComplaint.setOm("1");
                            GlobalApplication.databaseHelper.updateComplaint(cuComplaint, new OnDataUpdatedListener() {
                                @Override
                                public void onDataUploaded(boolean success) {
                                    ((deptcomplaints) activity).hideProgress();
                                }
                            });

                        }
                    }
                });
            }
        });

        drmforwardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (departmentsList==null){
                   GlobalApplication.databaseHelper.getDepartmentsList(new OnDataFetchListener<Departments>() {
                       @Override
                       public void onDataFetched(List<Departments> departments) {
                           departmentsList=departments;
                       }
                   });
               }
               else {
                   dismiss();
                   final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mContext);
                   View view1=bottomSheetDialog.getLayoutInflater().inflate(R.layout.deplistlayout,null);
                   bottomSheetDialog.setContentView(view1);
                   bottomSheetDialog.show();
                   ListView listView=view1.findViewById(R.id.depsellist);
                   TextView dlladmintext=view1.findViewById(R.id.dlldmintext);
                   dlladmintext.setVisibility(View.GONE);
                   int temppos=0;
                   for(int i=0;i<departmentsList.size();i++) {
                       if (cuComplaint.getDept().contains(departmentsList.get(i).getDid()))
                           temppos = i;
                   }
                   DeptListAdapter deptListAdapter=new DeptListAdapter(departmentsList,temppos);
                   deptListAdapter.dissmiss(new OnClick() {
                       @Override
                       public void OnClicked() {
                           bottomSheetDialog.dismiss();
                       }
                   });
                   listView.setAdapter(deptListAdapter);



               }
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

    private class DeptListAdapter extends BaseAdapter{

        private int curpos;
        private List<Departments> deptslist;
        private OnClick onClick;

        public DeptListAdapter(List<Departments> deptslist,int curpos) {
            this.deptslist=deptslist;
            this.curpos=curpos;
        }

        @Override
        public int getCount() {
            return deptslist.size();
        }

        @Override
        public Departments getItem(int position) {
            return deptslist.get(position);   
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(R.layout.depspinnerlayout,null);
            final TextView deptnametext=view.findViewById(R.id.deptnametext);
            deptnametext.setText(deptslist.get(position).getName()+"("+deptslist.get(position).getDid()+")");
            if(position==curpos)
                deptnametext.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            deptnametext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ConfirmationDialog confirmationDialog=new ConfirmationDialog(mContext,"Forward","Are you sure you want to move complaint to "+deptnametext.getText().toString()+" ?");
                    View view1=confirmationDialog.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                    confirmationDialog.setContentView(view1);
                    confirmationDialog.show();
                    confirmationDialog.choicelistener(new CDOnClick() {
                        @Override
                        public void OnButtonClicked(boolean choice) {
                            confirmationDialog.dismiss();
                            if(choice){
                                if(position!=curpos){
                                    ((deptcomplaints)mContext).showProgress("Forwarding...");
                                    onClick.OnClicked();
                                    cuComplaint.setDept(deptslist.get(position).getDid());
                                    GlobalApplication.databaseHelper.updateComplaint(cuComplaint, new OnDataUpdatedListener() {
                                        @Override
                                        public void onDataUploaded(boolean success) {
                                            ((deptcomplaints)mContext).hideProgress();
                                            if (success){
                                                onClick.OnClicked();
                                                Toast.makeText(mContext,"Complaint is forwarded",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                else {
                                    onClick.OnClicked();
                                    Toast.makeText(mContext,"This is current department",Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    });

                }
            });
            return view;
        }

        public void dissmiss(OnClick onClick){
            this.onClick=onClick;
        }
    }
}
