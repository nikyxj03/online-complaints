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

import com.msm.onlinecomplaintapp.GlobalApplication;
import com.msm.onlinecomplaintapp.Interfaces.FunctionListener;
import com.msm.onlinecomplaintapp.R;

public class AdminManageUsersFragment extends Fragment {

    private ListView usersListView;

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

        return view;
    }
}
