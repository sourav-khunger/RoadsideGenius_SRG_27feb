package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import com.doozycod.roadsidegenius.Activities.Admin.CreateJobAssignActivity;
import com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter.AdminPagerAdapter;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Utils.CustomViewPager;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.material.tabs.TabLayout;


/**
 * Created by yarolegovich on 25.03.2017.
 */

public class CenteredTextFragment extends Fragment {

    private static final String EXTRA_TEXT = "text";

    public static CenteredTextFragment createFor(String text) {
        CenteredTextFragment fragment = new CenteredTextFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    SharedPreferenceMethod sharedPreferenceMethod;
    AdminPagerAdapter pagerAdapter;
    CustomViewPager viewPager;
    TabLayout tabLayout;
    Button assignJobButton;
    boolean allowRefresh = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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


        View view = localInflater.inflate(R.layout.fragment_text, container, false);
        pagerAdapter = new AdminPagerAdapter(getFragmentManager());
        assignJobButton = view.findViewById(R.id.assignJobButton);
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab_layout_main);

        viewPager.setSwipeable(true);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        assignJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getActivity(), CreateJobAssignActivity.class));
            }
        });
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        //Initialize();
//        if (allowRefresh) {
//            allowRefresh = false;
//            tabLayout.setupWithViewPager(viewPager);
//            viewPager.setAdapter(pagerAdapter);
//
//
//            //call your initialization code here
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (!allowRefresh)
//            allowRefresh = true;
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String text = args != null ? args.getString(EXTRA_TEXT) : "";

    }
}
