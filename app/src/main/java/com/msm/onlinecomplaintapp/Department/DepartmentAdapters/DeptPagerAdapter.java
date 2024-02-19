package com.msm.onlinecomplaintapp.Department.DepartmentAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.msm.onlinecomplaintapp.Department.DepartmentFragments.DeptComplaintsFragment;

public class DeptPagerAdapter extends FragmentStatePagerAdapter {

  private String did;
  private int itemno=0;
  private int sm=0;
  private int pm=0;

  private DeptComplaintsFragment deptComplaintsFragment;

  public DeptPagerAdapter(FragmentManager fm, String did,int pm) {
    super(fm);
    this.did=did;
    this.sm=sm;
    this.pm=pm;
  }

  public DeptPagerAdapter(FragmentManager fm, String did,int pm, int sm) {
    super(fm);
    this.did=did;
    this.sm=sm;
    this.pm=pm;
  }

  @Override
  public Fragment getItem(int i) {

    switch (i) {
      case 0:
        itemno = 0;
        if(pm==0)
          deptComplaintsFragment=DeptComplaintsFragment.newInstance("0",did,sm);
        else
          deptComplaintsFragment=DeptComplaintsFragment.newInstance("2",did,sm);
        return deptComplaintsFragment;
      case 1:
        itemno = 1;
        if(pm==0)
          deptComplaintsFragment=DeptComplaintsFragment.newInstance("1",did,sm);
        else
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