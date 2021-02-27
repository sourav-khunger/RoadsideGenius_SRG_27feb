package com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.doozycod.roadsidegenius.Fragments.ApprovedRequestsFragment;
import com.doozycod.roadsidegenius.Fragments.CancelledRequestsFragment;
import com.doozycod.roadsidegenius.Fragments.PendingRequestsFragment;

public class WithdrawRequestPagerAdapter extends FragmentStatePagerAdapter {

    public WithdrawRequestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new PendingRequestsFragment();
                break;
            case 1:
                fragment = new ApprovedRequestsFragment();
                break;
            case 2:
                fragment = new CancelledRequestsFragment();
                break;
            default:
                fragment = new PendingRequestsFragment();
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
                title = "Pending";
                break;
            case 1:
                title = "Approved";
                break;
            case 2:
                title = "Cancelled";
                break;
            default:
                title = "Pending";
        }
        return title;
    }
}
