package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doozycod.roadsidegenius.Activities.CustomerDetailsActivity;
import com.doozycod.roadsidegenius.Activities.JobDetailsDriverActivity;
import com.doozycod.roadsidegenius.Adapter.ViewPagerAdapter.TabLayoutPagerAdapter;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.DriverActiveJobModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.CustomViewPager;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabLayoutPagerAdapter pagerAdapter;
    CustomViewPager viewPager;
    TabLayout tabLayout;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    static ApiService apiService;
    TextView customerNameTxt, serviceTypeTxt, pickupTxt, text;
    private String TAG = TaskHistoryFragment.class.getName();
    LinearLayout visibleDetails;
    private int FLAG = 0;
    ImageView show_activeJobDetails;
//    SharedPreferenceMethod sharedPreferenceMethod;

    public TaskListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TaskListFragment newInstance(String param1) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

    }

    int i = 0;

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
        View view = localInflater.inflate(R.layout.fragment_task_list, container, false);
        pagerAdapter = new TabLayoutPagerAdapter(getFragmentManager());
        text = view.findViewById(R.id.text);
        pickupTxt = view.findViewById(R.id.pickupTxt);
        show_activeJobDetails = view.findViewById(R.id.show_activeJobDetails);
        visibleDetails = view.findViewById(R.id.visibleDetails);
        serviceTypeTxt = view.findViewById(R.id.serviceTypeTxt);
        customerNameTxt = view.findViewById(R.id.customerNameTxt);
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab_layout_main);

//        setup view pager for tabs
        viewPager.setSwipeable(true);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);

//        api service init
        apiService = ApiUtils.getAPIService();

        customProgressBar = new CustomProgressBar(getActivity());

        show_activeJobDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), JobDetailsDriverActivity.class);
                intent.putExtra("tasks", "tasks");
                startActivity(intent);
            }
        });
//        apiCall
        getActiveJobAPI();
        return view;
    }

   /* public void refreshFragment() {
        Log.e("LogGeneratorHelper", "reloading fragment");
        getActiveJobAPI();

    }*/

    @Override
    public void onResume() {
        super.onResume();
        if (FLAG == 0) {
            FLAG = 1;
            return;
        }
//        getActiveJobAPI();
    }

    private void getActiveJobAPI() {
//        Log.e("LogGeneratorHelper", "reloading " + i);

//        customProgressBar.showProgress();
        apiService.activeJobForDriver(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<DriverActiveJobModel>() {
            @Override
            public void onResponse(Call<DriverActiveJobModel> call, Response<DriverActiveJobModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    if (response.body().getResponse().getActiveJob() == null) {

//                        text.setVisibility(View.VISIBLE);
//                        visibleDetails.setVisibility(GONE);

                    } else {

                        customerNameTxt.setText(response.body().getResponse().getActiveJob().getCustomerName());
                        serviceTypeTxt.setText(response.body().getResponse().getActiveJob().getService());
                        pickupTxt.setText(response.body().getResponse().getActiveJob().getCustomerPickup());

//                        text.setVisibility(GONE);

                    }
                }
            }

            @Override
            public void onFailure(Call<DriverActiveJobModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());

//                customProgressBar.hideProgress();

            }
        });
    }
}