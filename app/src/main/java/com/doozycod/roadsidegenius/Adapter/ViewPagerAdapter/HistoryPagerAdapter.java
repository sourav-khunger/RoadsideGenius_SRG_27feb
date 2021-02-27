package com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.doozycod.roadsidegenius.Activities.AdminCompleteJobActivity;
import com.doozycod.roadsidegenius.Fragments.CancelledJobFragment;
import com.doozycod.roadsidegenius.Fragments.CompletedJobFragment;
import com.doozycod.roadsidegenius.Fragments.GoneOnArrivalFragment;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.AssignedFragment;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.CompletedFragment;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.NewRequestFragment;

public class HistoryPagerAdapter extends FragmentStatePagerAdapter {

    public HistoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new CompletedJobFragment();
                break;
            case 1:
                fragment = new GoneOnArrivalFragment();
                break;
            case 2:
                fragment = new CancelledJobFragment();
                break;
            default:
                fragment = new GoneOnArrivalFragment();
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
                title = "Completed";

                break;
            case 1:
                title = "GOA";

                break;
            case 2:
                title = "Cancelled";
                break;
            default:
                title = "GOA";
        }
        return title;
    }
}
