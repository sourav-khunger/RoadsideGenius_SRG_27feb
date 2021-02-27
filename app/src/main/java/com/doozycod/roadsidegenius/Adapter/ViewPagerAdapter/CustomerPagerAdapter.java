package com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.doozycod.roadsidegenius.Tabs.CustomerNavigation.ActiveFragment;
import com.doozycod.roadsidegenius.Tabs.CustomerNavigation.CompletedRequestFragment;
import com.doozycod.roadsidegenius.Tabs.CustomerNavigation.OpenRequestFragment;

public class CustomerPagerAdapter extends FragmentStatePagerAdapter {

    public CustomerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new OpenRequestFragment();
                break;
            case 1:
                fragment = new ActiveFragment();
                break;
            case 2:
                fragment = new CompletedRequestFragment();
                break;
            default:
                fragment = new OpenRequestFragment();
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
                title = "Open";
                break;
            case 1:
                title = "Active";
                break;
            case 2:
                title = "Completed";
                break;
            default:
                title = "Open";
        }
        return title;
    }
}
