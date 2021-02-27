package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Adapter.DriversListAdapter;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Model.DriverList.Driver;
import com.doozycod.roadsidegenius.Model.DriverList.DriversListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriversListFragment extends Fragment {

    private static final String EXTRA_TEXT = "text";

    public DriversListFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    DriversListAdapter driversListAdapter;
    SharedPreferenceMethod sharedPreferenceMethod;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    List<Driver> driverList = new ArrayList<>();
    boolean allowRefresh = false;

    public static DriversListFragment createFor(String text) {
        DriversListFragment fragment = new DriversListFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        //Initialize();
        if (allowRefresh) {
            allowRefresh = false;
            getDrivers();

            //call your initialization code here
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!allowRefresh)
            allowRefresh = true;
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
        View view = localInflater.inflate(R.layout.fragment_drivers_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        sharedPreferenceMethod = new SharedPreferenceMethod(getContext());
        customProgressBar = new CustomProgressBar(getContext());
        apiService = ApiUtils.getAPIService();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        getDrivers();
        return view;
    }

    void getDrivers() {

        customProgressBar.showProgress();
        apiService.getDriverList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<DriversListModel>() {
            @Override
            public void onResponse(Call<DriversListModel> call, Response<DriversListModel> response) {
                customProgressBar.hideProgress();
                allowRefresh = true;

                if (response.body().getResponse().getStatus().equals("Success")) {
                    driverList = response.body().getResponse().getDrivers();
                    if (driverList.size() == 0) {
                        Toast.makeText(getActivity(), "No Driver Found!", Toast.LENGTH_SHORT).show();
                    } else
                        driversListAdapter = new DriversListAdapter(getContext(), driverList, apiService);
                    recyclerView.setAdapter(driversListAdapter);
                }
            }

            @Override
            public void onFailure(Call<DriversListModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

}