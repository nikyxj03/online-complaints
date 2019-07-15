package com.msm.onlinecomplaintapp.Admin.AdminActivities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.msm.onlinecomplaintapp.Admin.AdminActivity;
import com.msm.onlinecomplaintapp.Admin.AdminAdapters.AdminMasterPagerAdapter;
import com.msm.onlinecomplaintapp.Admin.AdminFragments.AdminManageUsersFragment;
import com.msm.onlinecomplaintapp.R;

public class AdminHomeDefault extends AdminActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private BottomNavigationMenu bottomNavigationMenu;
    private Toolbar toolbar;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_default);
        bottomNavigationView=findViewById(R.id.navigation);
        toolbar=findViewById(R.id.a_hd_toolbar);
        viewPager=findViewById(R.id.admin_pager);
        setSupportActionBar(toolbar);

        createMasterMenu(bottomNavigationView.getMenu());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.setSelectedItemId(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                viewPager.setCurrentItem(menuItem.getItemId());
                return false;
            }
        });
    }

    public void createMasterMenu(Menu menu){
        bottomNavigationView.getMenu().add(Menu.NONE,0,Menu.NONE,"MANAGE USERS").setIcon(R.drawable.ic_delete_grey_18dp);
       // bottomNavigationView.getMenu().add(Menu.NONE,1,Menu.NONE,"ANNOUNCEMENTS").setIcon(R.drawable.ic_mode_edit_grey_18dp);
       // bottomNavigationView.getMenu().add(Menu.NONE,2,Menu.NONE,"REQUUESTS").setIcon(R.drawable.ic_unarchive_grey_18dp);

        AdminMasterPagerAdapter adminMasterPagerAdapter=new AdminMasterPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adminMasterPagerAdapter);

    }
}
