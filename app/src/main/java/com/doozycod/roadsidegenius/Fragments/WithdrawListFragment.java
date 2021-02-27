package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter.AdminPagerAdapter;
import com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter.WithdrawRequestPagerAdapter;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Utils.CustomViewPager;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.material.tabs.TabLayout;


public class WithdrawListFragment extends Fragment {

    public WithdrawListFragment() {
        // Required empty public constructor
    }

    CustomViewPager viewPager;
    TabLayout tabLayout;
    WithdrawRequestPagerAdapter withdrawRequestPagerAdapter;
    SharedPreferenceMethod sharedPreferenceMethod;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context contextThemeWrapper;
        if (sharedPreferenceMethod != null) {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(),
                    sharedPreferenceMethod.getTheme().equals("light") ? R.style.LightTheme : R.style.DarkTheme);
        } else {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.LightTheme);

        }
        // create ContextThemeWrapper from the original Activity Context with the custom theme

// clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // Inflate the layout for this fragment
        View view = localInflater.inflate(R.layout.fragment_withdraw_list, container, false);

        withdrawRequestPagerAdapter = new WithdrawRequestPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab_layout_main);

        viewPager.setSwipeable(true);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(withdrawRequestPagerAdapter);
        return view;
    }
}