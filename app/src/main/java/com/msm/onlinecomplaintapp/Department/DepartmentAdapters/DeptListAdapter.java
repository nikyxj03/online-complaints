package com.msm.onlinecomplaintapp.Department.DepartmentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.msm.onlinecomplaintapp.Common.ConfirmationDialog;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.deptcomplaints;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.CDOnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnClick;
import com.msm.onlinecomplaintapp.Interfaces.OnDataUpdatedListener;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.Models.Departments;
import com.msm.onlinecomplaintapp.Models.ForwrardHistory;
import com.msm.onlinecomplaintapp.R;

import java.util.List;

public class DeptListAdapter extends BaseAdapter {

    private int curpos;
    private List<Departments> deptslist;
    private OnClick onClick;
    private Context mContext;
    private Complaint cuComplaint;

    public DeptListAdapter(List<Departments> deptslist,int curpos,Context context,Complaint complaint) {
        this.deptslist=deptslist;
        this.curpos=curpos;
        this.mContext=context;
        this.cuComplaint=complaint;
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
                                        if (success) {
                                            ForwrardHistory forwrardHistory = new ForwrardHistory();
                                            forwrardHistory.setFrom(deptslist.get(curpos).getDid());
                                            forwrardHistory.setTo(deptslist.get(position).getDid());
                                            forwrardHistory.setTimestamp(Timestamp.now());
                                            GlobalApplication.databaseHelper.updateForwardHistory(forwrardHistory,cuComplaint, new OnDataUpdatedListener() {
                                                @Override
                                                public void onDataUploaded(boolean success) {
                                                    ((deptcomplaints) mContext).hideProgress();
                                                    if (success) {
                                                        onClick.OnClicked();
                                                        Toast.makeText(mContext, "Complaint is forwarded", Toast.LENGTH_LONG).show();
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