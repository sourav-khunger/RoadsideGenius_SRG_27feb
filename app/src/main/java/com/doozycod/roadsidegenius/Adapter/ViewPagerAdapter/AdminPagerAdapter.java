package com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.doozycod.roadsidegenius.Tabs.AdminNavigation.AssignedFragment;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.CompletedFragment;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.NewRequestFragment;

public class AdminPagerAdapter extends FragmentStatePagerAdapter {

    public AdminPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new NewRequestFragment();
                break;
            case 1:
                fragment = new AssignedFragment();
                break;
            case 2:
                fragment = new CompletedFragment();
                break;
            default:
                fragment = new NewRequestFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        switch (position) {
            case 0:
                title = "New Request";
                break;
            case 1:
                title = "Assigned";
                break;
            case 2:
                title = "Completed";
                break;
            default:
                title = "New Request";
        }
        return title;
    }
}
