package com.msm.onlinecomplaintapp.Department.DepartmentMenus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.msm.onlinecomplaintapp.Common.ConfirmationDialog;
import com.msm.onlinecomplaintapp.Common.RandomStringBuilder;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.deptcomplaints;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.depttotcompdec;
import com.msm.onlinecomplaintapp.Department.DepartmentAdapters.DeptListAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.CDOnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataSFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Models.AdminRequests;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.Models.StatusLog;
import com.msm.onlinecomplaintapp.Models.StatusMessage;
import com.msm.onlinecomplaintapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeptWatchingMenu extends BottomSheetDialog {
    private Context mContext;
    private Button dwmviewcompbutton;
    private Button dwmresolvebutton;
    private Button dwmignorebutton;
    private Button dwmforwardbutton;
    private Button dwmdeletebutton;
    private Button dwmsendmsgbutton;
    private Button dwmdonebutton;
    private CircleImageView dwmcompimage;
    private Complaint cuComplaint;
    private View mPassedView;
    private String rid="";
    private List<Departments> departmentsList;


    public DeptWatchingMenu(Context context,Complaint complaint,View passedView,List<Departments> departments) {
        super(context);
        mContext=context;
        cuComplaint=complaint;
        mPassedView=passedView;
        departmentsList=departments;
    }

    @Override
    public void setContentView(final View view) {
        super.setContentView(view);
        dwmviewcompbutton=view.findViewById(R.id.dwmviewcompbutton);
        dwmresolvebutton=view.findViewById(R.id.dwmresolvebutton);
        dwmignorebutton=view.findViewById(R.id.dwmignorebutton);
        dwmforwardbutton=view.findViewById(R.id.dwmforwardbutton);
        dwmsendmsgbutton=view.findViewById(R.id.dwmsendmsgbutton);
        dwmdeletebutton=view.findViewById(R.id.dwmdeletebutton);
        dwmdonebutton=view.findViewById(R.id.dwmdonebutton);
        dwmcompimage=view.findViewById(R.id.dwmimage);

        final Activity activity=(deptcomplaints)mContext;
        if(cuComplaint.getCriuri()==null || cuComplaint.getCriuri().equals("")){
            dwmcompimage.setVisibility(View.GONE);
        }
        else {
            Glide.with(mContext.getApplicationContext()).load(cuComplaint.getCriuri()).placeholder(R.drawable.ic_launcher_foreground).into(dwmcompimage);
        }

        dwmviewcompbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent=new Intent();
                intent.setClass(mContext, depttotcompdec.class);
                intent.putExtra("cuComplaint",cuComplaint.toHashMap());
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,mPassedView,"trans1");
                mContext.startActivity(intent,activityOptionsCompat.toBundle());
            }
        });

        dwmsendmsgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mContext);
                View view1=bottomSheetDialog.getLayoutInflater().inflate(R.layout.status_message_layout,null);
                bottomSheetDialog.setContentView(view1);
                final EditText smedit=view1.findViewById(R.id.smedit);
                final ImageButton smsendbutton=view1.findViewById(R.id.smsendbutton);
                smsendbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (smedit.getText().toString() != "") {
                            ((deptcomplaints) mContext).showProgress("Sending Message..");
                            RandomStringBuilder randomStringBuilder = new RandomStringBuilder("msgId", 12);
                            randomStringBuilder.getRandomId(new OnDataSFetchListener<String>() {
                                @Override
                                public void onDataSFetch(final String s) {
                                    StatusMessage statusMessage = new StatusMessage();
                                    statusMessage.setMsgId(s);
                                    statusMessage.setAuthorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    statusMessage.setCid(cuComplaint.getCid());
                                    statusMessage.setMessage(smedit.getText().toString());
                                    statusMessage.setTimestamp(Timestamp.now());
                                    GlobalApplication.databaseHelper.sendStatusMsg(statusMessage, new OnDataUpdatedListener() {
                                        @Override
                                        public void onDataUploaded(boolean success) {
                                            if (success) {
                                                cuComplaint.setStatement(smedit.getText().toString());
                                                GlobalApplication.databaseHelper.updateComplaint(cuComplaint, new OnDataUpdatedListener() {
                                                    @Override
                                                    public void onDataUploaded(boolean success) {
                                                        ((deptcomplaints) mContext).hideProgress();
                                                        if (success)
                                                            bottomSheetDialog.dismiss();
                                                    }
                                                });
                                            }
                                            else {
                                                ((deptcomplaints) mContext).hideProgress();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });

        dwmresolvebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mContext);
                View view1=bottomSheetDialog.getLayoutInflater().inflate(R.layout.status_message_layout,null);
                bottomSheetDialog.setContentView(view1);
                final TextView smheading=view1.findViewById(R.id.smheading);
                final EditText smedit=view1.findViewById(R.id.smedit);
                final ImageButton smsendbutton=view1.findViewById(R.id.smsendbutton);
                smheading.setText("Enter Final Statement");
                smsendbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (smedit.getText().toString() != "") {
                            ((deptcomplaints) mContext).showProgress("Updating Status...");
                            RandomStringBuilder randomStringBuilder = new RandomStringBuilder("msgId", 12);
                            randomStringBuilder.getRandomId(new OnDataSFetchListener<String>() {
                                @Override
                                public void onDataSFetch(final String s) {
                                    StatusMessage statusMessage = new StatusMessage();
                                    statusMessage.setMsgId(s);
                                    statusMessage.setAuthorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    statusMessage.setCid(cuComplaint.getCid());
                                    statusMessage.setMessage(smedit.getText().toString());
                                    statusMessage.setTimestamp(Timestamp.now());
                                    GlobalApplication.databaseHelper.sendStatusMsg(statusMessage, new OnDataUpdatedListener() {
                                        @Override
                                        public void onDataUploaded(boolean success) {
                                            if (success) {
                                                cuComplaint.setStatement(s);
                                                cuComplaint.setOm("1");
                                                cuComplaint.setAcm("2");
                                                cuComplaint.setResolvedTime(Timestamp.now());
                                                GlobalApplication.databaseHelper.updateComplaint(cuComplaint, new OnDataUpdatedListener() {
                                                    @Override
                                                    public void onDataUploaded(boolean success) {
                                                        ((deptcomplaints) mContext).hideProgress();
                                                        if (success){
                                                            StatusLog statusLog=new StatusLog();
                                                            statusLog.setDuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            statusLog.setFrom("1");
                                                            statusLog.setTo("2");
                                                            statusLog.setTimestamp(Timestamp.now());
                                                            GlobalApplication.databaseHelper.updateStatusLog(statusLog,cuComplaint.getCid());
                                                            bottomSheetDialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                            else {
                                                ((deptcomplaints) mContext).hideProgress();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });

        dwmignorebutton.setOnClickListener(new View.OnClickListener() {
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
                                    if (success){
                                        StatusLog statusLog=new StatusLog();
                                        statusLog.setDuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        statusLog.setFrom("1");
                                        statusLog.setTo("3");
                                        statusLog.setTimestamp(Timestamp.now());
                                        GlobalApplication.databaseHelper.updateStatusLog(statusLog,cuComplaint.getCid());
                                    }
                                    ((deptcomplaints) activity).hideProgress();
                                }
                            });

                        }
                    }
                });
            }
        });

        dwmforwardbutton.setOnClickListener(new View.OnClickListener() {
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
                    DeptListAdapter deptListAdapter=new DeptListAdapter(departmentsList,temppos,mContext,cuComplaint);
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

        dwmdeletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final ConfirmationDialog confirmationDialog=new ConfirmationDialog(mContext,"Delete Complaint","Are you sure you want to delete complaint?");
                View view1= confirmationDialog.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                confirmationDialog.setContentView(view1);
                confirmationDialog.show();
                confirmationDialog.choicelistener(new CDOnClick() {
                    @Override
                    public void OnButtonClicked(boolean choice) {
                        confirmationDialog.dismiss();
                        if(choice){
                            ((deptcomplaints) activity).showProgress("Loading...");
                            GlobalApplication.databaseHelper.deleteComplaint(cuComplaint.getCid(), new OnDataUpdatedListener() {
                                @Override
                                public void onDataUploaded(boolean success) {
                                    ((deptcomplaints) activity).hideProgress();
                                    if(success){
                                        Toast.makeText(mContext,"Complaint deleted",Toast.LENGTH_LONG).show();
                                        final ConfirmationDialog confirmationDialog1=new ConfirmationDialog(mContext,"Block User","Do you want to report admin about the ussr who issued the complaint?");
                                        View view2= confirmationDialog1.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                                        confirmationDialog1.setContentView(view2);
                                        confirmationDialog1.show();
                                        confirmationDialog1.choicelistener(new CDOnClick() {
                                            @Override
                                            public void OnButtonClicked(boolean choice) {
                                                confirmationDialog1.dismiss();
                                                if(choice){
                                                    BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(mContext);
                                                    View view3=bottomSheetDialog.getLayoutInflater().inflate(R.layout.status_message_layout,null);
                                                    bottomSheetDialog.setContentView(view3);
                                                    final TextView smheading=view3.findViewById(R.id.smheading);
                                                    final EditText smedit=view3.findViewById(R.id.smedit);
                                                    final ImageButton smsendbutton=view3.findViewById(R.id.smsendbutton);
                                                    bottomSheetDialog.show();
                                                    smsendbutton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if(smedit.getText().toString()!=""){
                                                                ((deptcomplaints)mContext).showProgress("Sending Request...");
                                                                RandomStringBuilder randomStringBuilder=new RandomStringBuilder("rid",10);
                                                                randomStringBuilder.getRandomId(new OnDataSFetchListener<String>() {
                                                                    @Override
                                                                    public void onDataSFetch(String s) {
                                                                        AdminRequests adminRequests=new AdminRequests();
                                                                        adminRequests.setCid(cuComplaint.getCid());
                                                                        adminRequests.setMessage(smedit.getText().toString());
                                                                        adminRequests.setRid(s);
                                                                        adminRequests.setTimestamp(Timestamp.now());
                                                                        adminRequests.setUid(cuComplaint.getUid());
                                                                        adminRequests.setType("user_report");
                                                                        adminRequests.setDid(cuComplaint.getDept());
                                                                        adminRequests.setDuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                        GlobalApplication.databaseHelper.updateRequest(adminRequests, new OnDataUpdatedListener() {
                                                                            @Override
                                                                            public void onDataUploaded(boolean success) {
                                                                                ((deptcomplaints)mContext).hideProgress();
                                                                                if(success){
                                                                                    Toast.makeText(mContext,"Requested Posted",Toast.LENGTH_LONG).show();
                                                                                }
                                                                                else {
                                                                                    Toast.makeText(mContext,"Request failed",Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                            else {
                                                                Toast.makeText(mContext,"Message should not empty",Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(mContext,"Complaint Deletion Failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });


        dwmdonebutton.setOnClickListener(new View.OnClickListener() {
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