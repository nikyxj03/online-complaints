package com.msm.onlinecomplaintapp.LoginActivities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.msm.onlinecomplaintapp.Interfaces.PageLockListener;

public class LoginPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private PageLockListener mpageLockListener;

    public LoginPagerAdapter(FragmentManager fm, Context context,PageLockListener pageLockListener) {
        super(fm);
        mContext=context;
        mpageLockListener=pageLockListener;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return UserLoginFragment.getInstance(mpageLockListener);
            case 1:
                return DeptLoginFragment.getInstance();
            case 2:
                return AdminLoginFragment.getInstance();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
