package com.msm.onlinecomplaintapp.Admin.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msm.onlinecomplaintapp.Admin.AdminActivities.AdminHomeDefault;
import com.msm.onlinecomplaintapp.Admin.AdminActivity;
import com.msm.onlinecomplaintapp.Admin.AdminAdapters.AdminUserListAdapter;
import com.msm.onlinecomplaintapp.Common.ConfirmationDialog;
import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.CDOnClick;
import com.msm.onlinecomplaintapp.Interfaces.FunctionListener;
import com.msm.onlinecomplaintapp.Interfaces.ItemClickListener;
import com.msm.onlinecomplaintapp.Interfaces.OnDataFetchListener;
import com.msm.onlinecomplaintapp.Models.Users;
import com.msm.onlinecomplaintapp.R;

import java.util.List;

public class AdminManageUsersFragment extends Fragment {

    private RecyclerView usersListView;

    private RecyclerView.LayoutManager layoutManager;

    private AdminUserListAdapter adminUserListAdapter;

    private ItemClickListener<Users> itemClickListener;

    public static AdminManageUsersFragment adminManageUsersFragment;

    public AdminManageUsersFragment(){

    }

    public static AdminManageUsersFragment getInstance(){
        if(adminManageUsersFragment==null)
            adminManageUsersFragment=new AdminManageUsersFragment();
        return adminManageUsersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.admin_manage_users_fragment,null);
        usersListView=view.findViewById(R.id.a_master_user_listview);

        layoutManager=new LinearLayoutManager(getContext());
        usersListView.setLayoutManager(layoutManager);

        itemClickListener=new ItemClickListener<Users>() {
            @Override
            public void onItemClicked(final Users item) {
                Toast.makeText(getContext(),item.getFullname(),Toast.LENGTH_LONG).show();
                final ConfirmationDialog confirmationDialog=new ConfirmationDialog(getContext(),"Block User");
                View view1=confirmationDialog.getLayoutInflater().inflate(R.layout.confirmationdialoglayout,null);
                confirmationDialog.setContentView(view1);
                confirmationDialog.choicelistener(new CDOnClick() {
                    @Override
                    public void OnButtonClicked(boolean choice) {
                        confirmationDialog.dismiss();
                        if(choice) {
                            ((AdminHomeDefault)getActivity()).showProgress("Blocking");
                            GlobalApplication.cloudFunctionHelper.blockuser(item.getUid(), new FunctionListener() {
                                @Override
                                public void onExecuted(boolean execution) {
                                    ((AdminHomeDefault)getActivity()).hideProgress();
                                    {
                                        Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }
                    }
                });
            confirmationDialog.show();
            }
        };

        adminUserListAdapter=new AdminUserListAdapter(getContext(),itemClickListener);
        usersListView.setAdapter(adminUserListAdapter);

        GlobalApplication.databaseHelper.fetchAllUsers(new OnDataFetchListener<Users>() {
            @Override
            public void onDataFetched(List<Users> list) {
                adminUserListAdapter.setList(list);
            }
        });
        return view;
    }
}
