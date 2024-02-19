package com.msm.onlinecomplaintapp.Department.DepartmentFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.msm.onlinecomplaintapp.Department.DepartmentActivities.DeptArchives;
import com.msm.onlinecomplaintapp.Department.DepartmentActivities.deptcomplaints;
import com.msm.onlinecomplaintapp.Department.DepartmentActivity;
import com.msm.onlinecomplaintapp.Department.DepartmentAdapters.DCompListAdapter;
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

    private List<Complaint> stcomplaintList=new ArrayList<>();
    private List<Complaint> sscomplaintList=new ArrayList<>();

    private List<Complaint> stcomplaintList1=new ArrayList<>();
    private List<Complaint> sscomplaintList1=new ArrayList<>();


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

        if(status=="0"){
            dCompListAdapter=new DCompListAdapter(activity,R.layout.deptcompistcustom,sm,((DepartmentActivity)activity).getPageRegisteredComplaintsD());
            complaintListView.setAdapter(dCompListAdapter);
            GlobalApplication.databaseHelper.getDepartmentPrivateRegisteredComplaintsSS(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    sscomplaintList=complaints;
                    if(sscomplaintList!=null) {
                        if (sm == 1)
                            dCompListAdapter.setList(sscomplaintList);
                    }
                }
            });
            GlobalApplication.databaseHelper.getDepartmentPrivateRegisteredComplaintsST(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    stcomplaintList=complaints;
                    if(stcomplaintList!=null) {
                        if (sm == 0)
                            dCompListAdapter.setList(stcomplaintList);
                    }
                }
            });
            ((deptcomplaints)activity).setSortListener_ZERO(context,new OnSortChange() {
                @Override
                public void onSortChanged(int sm) {
                    DeptComplaintsFragment.this.sm=sm;
                    if(sm==0)
                        dCompListAdapter.setList(stcomplaintList);
                    else
                        dCompListAdapter.setList(sscomplaintList);
                }
            });
        }

        if(status=="1") {
            dCompListAdapter = new DCompListAdapter(activity, R.layout.deptcompistcustom, sm, ((DepartmentActivity) activity).getPageWatchingComplaintsD());
            complaintListView.setAdapter(dCompListAdapter);
            GlobalApplication.databaseHelper.getDepartmentPrivateWatchingComplaintsSS(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    sscomplaintList1 = complaints;
                    if(sscomplaintList!=null) {
                        if (sm == 1)
                            dCompListAdapter.setList(sscomplaintList1);
                    }
                }
            });
            GlobalApplication.databaseHelper.getDepartmentPrivateWatchingComplaintsST(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    stcomplaintList1 = complaints;
                    if(stcomplaintList1!=null) {
                        if (sm == 0)
                            dCompListAdapter.setList(stcomplaintList1);
                    }
                }
            });
            ((deptcomplaints)activity).setSortListener_ONE(context,new OnSortChange() {
                @Override
                public void onSortChanged(int sm) {
                    DeptComplaintsFragment.this.sm=sm;
                    if(sm==0)
                        dCompListAdapter.setList(stcomplaintList1);
                    else
                        dCompListAdapter.setList(sscomplaintList1);
                }
            });
        }

        if(status=="2"){
            dCompListAdapter=new DCompListAdapter(activity,R.layout.deptcompistcustom,sm,((DepartmentActivity)activity).getPageResolvedComplaints());
            complaintListView.setAdapter(dCompListAdapter);
            GlobalApplication.databaseHelper.getDepartmentPrivateResolvedComplaintsSS(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    sscomplaintList=complaints;
                    if(sscomplaintList!=null) {
                        if (sm == 1)
                            dCompListAdapter.setList(sscomplaintList);
                    }
                }
            });
            GlobalApplication.databaseHelper.getDepartmentPrivateResolvedComplaintsST(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    stcomplaintList=complaints;
                    if(stcomplaintList!=null) {
                        if (sm == 0)
                            dCompListAdapter.setList(stcomplaintList);
                    }
                }
            });
            ((DeptArchives)activity).setSortListener_ZERO(context,new OnSortChange() {
                @Override
                public void onSortChanged(int sm) {
                    DeptComplaintsFragment.this.sm=sm;
                    if(sm==0)
                        dCompListAdapter.setList(stcomplaintList);
                    else
                        dCompListAdapter.setList(sscomplaintList);
                }
            });
        }

        if(status=="3") {
            dCompListAdapter = new DCompListAdapter(activity, R.layout.deptcompistcustom, sm, ((DepartmentActivity) activity).getPageIgnoredComplaints());
            complaintListView.setAdapter(dCompListAdapter);
            GlobalApplication.databaseHelper.getDepartmentPrivateIgnoredComplaintsSS(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    sscomplaintList1 = complaints;
                    if(sscomplaintList!=null) {
                        if (sm == 1)
                            dCompListAdapter.setList(sscomplaintList1);
                    }
                }
            });
            GlobalApplication.databaseHelper.getDepartmentPrivateIgnoredComplaintsST(did,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    stcomplaintList1 = complaints;
                    if(stcomplaintList1!=null) {
                        if (sm == 0)
                            dCompListAdapter.setList(stcomplaintList1);
                    }
                }
            });
            ((DeptArchives)activity).setSortListener_ONE(context,new OnSortChange() {
                @Override
                public void onSortChanged(int sm) {
                    DeptComplaintsFragment.this.sm=sm;
                    if(sm==0)
                        dCompListAdapter.setList(stcomplaintList1);
                    else
                        dCompListAdapter.setList(sscomplaintList1);
                }
            });
        }
        return view;
    }
}