package com.msm.onlinecomplaintapp.DepartmentAdapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.msm.onlinecomplaintapp.DepartmentFragments.DeptComplaintsFragment;

public class DeptArchivesPagerAdapter extends FragmentStatePagerAdapter {

    private String did;
    private int itemno=0;
    private int sm=0;
    private FragmentManager fragmentManager;

    private DeptComplaintsFragment deptComplaintsFragment;

    public DeptArchivesPagerAdapter(FragmentManager fm,String did) {
        super(fm);
        this.did=did;
        this.sm=sm;
        fragmentManager=fm;
    }

    public DeptArchivesPagerAdapter(FragmentManager fm,String did,int sm) {
        super(fm);
        this.did=did;
        this.sm=sm;
        fragmentManager=fm;
    }

    @Override
    public Fragment getItem(int i) {

            switch (i) {
                case 0:
                    itemno = 0;
                    deptComplaintsFragment=DeptComplaintsFragment.newInstance("2",did,sm);
                    return deptComplaintsFragment;
                case 1:
                    itemno = 1;
                    deptComplaintsFragment=DeptComplaintsFragment.newInstance("3",did,sm);
                    return deptComplaintsFragment;
                default:
                    return null;
            }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public int getItemno(){
        return itemno;
    }

    public void changeSM(int sm){
        this.sm=sm;
        notifyDataSetChanged();
    }
}
