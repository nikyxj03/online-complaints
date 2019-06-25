package com.msm.onlinecomplaintapp.UserAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.msm.onlinecomplaintapp.DepartmentFragments.DeptComplaintsFragment;
import com.msm.onlinecomplaintapp.UserFragments.UCompFragment;

import java.util.List;

public class UCompPagerAdapter extends FragmentStatePagerAdapter {

    private String uid;
    private int itemno=0;
    private int sm=0;

    private UCompFragment uCompFragment;

    public UCompPagerAdapter(FragmentManager fm, String uid, int sm) {
        super(fm);
        this.uid=uid;
        this.sm=sm;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                itemno = 0;
                uCompFragment=UCompFragment.newInstance("0",uid,sm);
                return uCompFragment;
            case 1:
                itemno = 1;
                uCompFragment=UCompFragment.newInstance("1",uid,sm);
                return uCompFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
