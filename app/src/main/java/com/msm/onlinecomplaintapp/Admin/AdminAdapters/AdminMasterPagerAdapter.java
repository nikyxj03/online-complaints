package com.msm.onlinecomplaintapp.Admin.AdminAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.msm.onlinecomplaintapp.Admin.AdminFragments.AdminManageUsersFragment;

public class AdminMasterPagerAdapter extends FragmentStatePagerAdapter {

    public AdminMasterPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return AdminManageUsersFragment.getInstance();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
