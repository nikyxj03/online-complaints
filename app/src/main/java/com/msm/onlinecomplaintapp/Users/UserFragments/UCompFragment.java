package com.msm.onlinecomplaintapp.Users.UserFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Interfaces.OnSortChange;
import com.msm.onlinecomplaintapp.Models.Complaint;
import com.msm.onlinecomplaintapp.R;
import com.msm.onlinecomplaintapp.Users.UserActivities.mycomplaints;
import com.msm.onlinecomplaintapp.Users.UserActivity;
import com.msm.onlinecomplaintapp.Users.UserAdapters.UCompListAdapter;

import java.util.ArrayList;
import java.util.List;

public class UCompFragment extends Fragment {

    private String status;
    private String uid;
    private int sm;
    private Context context;
    private Activity activity;

    private ListView complaintListView;

    private UCompListAdapter uCompListAdapter;

    private List<Complaint> stcomplaintList=new ArrayList<>();
    private List<Complaint> sscomplaintList=new ArrayList<>();

    private List<Complaint> stcomplaintList1=new ArrayList<>();
    private List<Complaint> sscomplaintList1=new ArrayList<>();


    public UCompFragment() {
        // Required empty public constructor
    }

    public static UCompFragment newInstance(String status, String uid, int sm) {
        UCompFragment fragment = new UCompFragment();
        Bundle args = new Bundle();
        args.putString("status",status);
        args.putString("USER_ID",uid);
        args.putInt("sm",sm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid=getArguments().getString("USER_ID");
            status=getArguments().getString("status");
            sm=getArguments().getInt("sm",0);
        }
        context=getContext();
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_complaint_fragment,container,false);
        complaintListView=view.findViewById(R.id.user_comp_listview);

        ((UserActivity)context).showProgress("Loading...");

        if(status=="0"){
            uCompListAdapter=new UCompListAdapter(activity,R.layout.pchlistcustom,sm,((UserActivity)activity).getPageOpenComplaints());
            complaintListView.setAdapter(uCompListAdapter);
            GlobalApplication.databaseHelper.getUserSupportList(uid, new OnDataFetchListener<String>() {
                @Override
                public void onDataFetched(List<String> strings) {
                    uCompListAdapter.setSupportList(strings);
                    GlobalApplication.databaseHelper.getUserPrivateOpenComplaintsSS(uid,new OnDataFetchListener<Complaint>() {
                        @Override
                        public void onDataFetched(List<Complaint> complaints) {
                            ((UserActivity)context).hideProgress();
                            sscomplaintList=complaints;
                            if(sscomplaintList!=null) {
                                ((UserActivity)context).hideProgress();
                                if (sm == 1)
                                    uCompListAdapter.setList(sscomplaintList);
                            }
                        }
                    });
                    GlobalApplication.databaseHelper.getUserPrivateOpenComplaintsST(uid,new OnDataFetchListener<Complaint>() {
                        @Override
                        public void onDataFetched(List<Complaint> complaints) {
                            ((UserActivity)context).hideProgress();
                            stcomplaintList=complaints;
                            if(stcomplaintList!=null) {
                                ((UserActivity)context).hideProgress();
                                if (sm == 0)
                                    uCompListAdapter.setList(stcomplaintList);
                            }
                        }
                    });
                }
            });
            ((mycomplaints)activity).setSortListener_ZERO(context,new OnSortChange() {
                @Override
                public void onSortChanged(int sm) {
                    UCompFragment.this.sm=sm;
                    Toast.makeText(context,"123",Toast.LENGTH_LONG).show();
                    if(sm==0)
                        uCompListAdapter.setList(stcomplaintList);
                    else
                        uCompListAdapter.setList(sscomplaintList);
                }
            });
        }

        if(status=="1"){
            uCompListAdapter=new UCompListAdapter(activity,R.layout.pchlistcustom,sm,((UserActivity)activity).getPageClosedComplaints());
            complaintListView.setAdapter(uCompListAdapter);
            GlobalApplication.databaseHelper.getUserPrivateClosedComplaintsSS(uid,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    ((UserActivity)context).hideProgress();
                    sscomplaintList=complaints;
                    if(sscomplaintList!=null) {
                        if (sm == 1)
                            uCompListAdapter.setList(sscomplaintList);
                    }
                }
            });
            GlobalApplication.databaseHelper.getUserPrivateClosedComplaintsST(uid,new OnDataFetchListener<Complaint>() {
                @Override
                public void onDataFetched(List<Complaint> complaints) {
                    ((UserActivity)context).hideProgress();
                    stcomplaintList=complaints;
                    if(stcomplaintList!=null) {
                        if (sm == 0)
                            uCompListAdapter.setList(stcomplaintList);
                    }
                }
            });
            ((mycomplaints)activity).setSortListener_ONE(context,new OnSortChange() {
                @Override
                public void onSortChanged(int sm) {
                    UCompFragment.this.sm=sm;
                    if(sm==0)
                        uCompListAdapter.setList(stcomplaintList);
                    else
                        uCompListAdapter.setList(sscomplaintList);
                }
            });
        }

        return view;
    }

}
