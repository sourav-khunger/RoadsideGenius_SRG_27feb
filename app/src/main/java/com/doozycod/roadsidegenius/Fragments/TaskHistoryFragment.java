package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter.HistoryPagerAdapter;
import com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter.TabLayoutPagerAdapter;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.CustomViewPager;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.material.tabs.TabLayout;


public class TaskHistoryFragment extends Fragment {

    HistoryPagerAdapter pagerAdapter;
    CustomViewPager viewPager;
    TabLayout tabLayout;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    static ApiService apiService;
    TextView customerNameTxt, serviceTypeTxt, pickupTxt, text;
    private String TAG = TaskHistoryFragment.class.getName();
    LinearLayout visibleDetails;

    public TaskHistoryFragment() {
        // Required empty public constructor
    }

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
        View view = localInflater.inflate(R.layout.fragment_task_history, container, false);
        pagerAdapter = new HistoryPagerAdapter(getFragmentManager());
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab_layout_main);
        tabLayout = view.findViewById(R.id.tab_layout_main);

//        setup view pager for tabs
        viewPager.setSwipeable(true);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);

//        api service init
        apiService = ApiUtils.getAPIService();

        customProgressBar = new CustomProgressBar(getActivity());




        return view;
    }
}