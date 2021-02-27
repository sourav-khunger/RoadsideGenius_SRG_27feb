package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Adapter.AdminAdapter.CompanyAdapter;
import com.doozycod.roadsidegenius.Model.Company.Company;
import com.doozycod.roadsidegenius.Model.Company.CompanyModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompaniesListFragment extends Fragment {


    public CompaniesListFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    CompanyAdapter companyAdapter;
    SharedPreferenceMethod sharedPreferenceMethod;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    List<Company> companyList = new ArrayList<>();
    boolean allowRefresh = false;

    @Override
    public void onResume() {
        super.onResume();
        //Initialize();
        if (allowRefresh) {
            allowRefresh = false;
            getCompanies();

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
        View view = localInflater.inflate(R.layout.fragment_companies_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        sharedPreferenceMethod = new SharedPreferenceMethod(getContext());
        customProgressBar = new CustomProgressBar(getContext());
        apiService = ApiUtils.getAPIService();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        getCompanies();
        return view;
    }

    private void getCompanies() {
        customProgressBar.showProgress();
        apiService.getCompanyLists(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<CompanyModel>() {
            @Override
            public void onResponse(Call<CompanyModel> call, Response<CompanyModel> response) {
                customProgressBar.hideProgress();
                allowRefresh = true;

                if (response.body().getResponse().getStatus().equals("Success")) {
                    companyList = response.body().getResponse().getCompanies();
                    if (companyList.size() == 0) {
                        Toast.makeText(getActivity(), "No Companies Found!", Toast.LENGTH_SHORT).show();
                    } else
                        companyAdapter = new CompanyAdapter(getContext(), companyList, apiService);
                    recyclerView.setAdapter(companyAdapter);
                }
            }

            @Override
            public void onFailure(Call<CompanyModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }
}