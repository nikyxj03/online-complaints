package com.msm.onlinecomplaintapp.DepartmentFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.msm.onlinecomplaintapp.DepartmentActivities.DeptArchives;
import com.msm.onlinecomplaintapp.DepartmentActivity;
import com.msm.onlinecomplaintapp.DepartmentAdapters.DCompListAdapter;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnSortChange;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;

import java.util.ArrayList;
import java.util.List;

public class DeptComplaintsFragment extends Fragment {

    private String status;
    private String did;
    private int sm;
    private Context context;
    private Activity activity;

    private ListView complaintListView;

    private DCompListAdapter dCompListAdapter;

    private List<Complaint> complaintList=new ArrayList<>();

    public DeptComplaintsFragment() {
        // Required empty public constructor
    }

    public static DeptComplaintsFragment newInstance(String status, String Did,int sm) {
        DeptComplaintsFragment fragment = new DeptComplaintsFragment();
        Bundle args = new Bundle();
        args.putString("status",status);
        args.putString("DEPT_ID",Did);
        args.putInt("sm",sm);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateData(int sm){
        this.sm=sm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            did=getArguments().getString("DEPT_ID");
            status=getArguments().getString("status");
            sm=getArguments().getInt("sm",0);
        }
        context=getContext();
        activity=getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.dept_complaints_fragment,container,false);
       complaintListView=view.findViewById(R.id.dept_listview);
       Toast.makeText(context,"1",Toast.LENGTH_LONG).show();

       if(status=="2"){
           dCompListAdapter=new DCompListAdapter(activity,R.layout.deptcompistcustom,sm,((DepartmentActivity)activity).getPageResolvedComplaints());
           complaintListView.setAdapter(dCompListAdapter);
           GlobalApplication.databaseHelper.getAllComplaintsA(new OnDataFetchListener<Complaint>() {
               @Override
               public void onDataFetched(List<Complaint> complaints) {
                   complaintList=complaints;
                   dCompListAdapter.setList(complaintList);
               }
           });
           dCompListAdapter.addSMListener();
           ((DepartmentActivity)activity).setSortListener_ZERO(context,new OnSortChange() {
               @Override
               public void onSortChanged(int sm) {
                   DeptComplaintsFragment.this.sm=sm;
                   dCompListAdapter.changesm(sm);
               }
           });
       }

           if(status=="3") {
               dCompListAdapter = new DCompListAdapter(activity, R.layout.deptcompistcustom, sm, ((DepartmentActivity) activity).getPageIgnoredComplaints());
               complaintListView.setAdapter(dCompListAdapter);
               GlobalApplication.databaseHelper.getAllComplaintsUA(new OnDataFetchListener<Complaint>() {
                   @Override
                   public void onDataFetched(List<Complaint> complaints) {
                       complaintList = complaints;
                       dCompListAdapter.setList(complaintList);
                   }
               });
               ((DepartmentActivity)activity).setSortListener_ONE(context,new OnSortChange() {
                   @Override
                   public void onSortChanged(int sm) {
                       dCompListAdapter.changesm(sm);
                   }
               });
           }
       return view;
    }
}
