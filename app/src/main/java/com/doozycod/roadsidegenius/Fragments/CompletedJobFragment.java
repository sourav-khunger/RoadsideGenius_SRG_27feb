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
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Adapter.AdminAdapter.CompletedJobsAdapter;
import com.doozycod.roadsidegenius.Adapter.AdminAdapter.HistoryCompletedJobsAdapter;
import com.doozycod.roadsidegenius.Model.AdminJobListModel.AdminListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletedJobFragment extends Fragment {


    RecyclerView recyclerView;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    SharedPreferenceMethod sharedPreferenceMethod;
    TextView text;

    public CompletedJobFragment() {
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
        View view = localInflater.inflate(R.layout.fragment_completed_job, container, false);
        //        api service init
        apiService = ApiUtils.getAPIService();

        customProgressBar = new CustomProgressBar(getActivity());
        text = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.recyclerView);

        initRecyclerView();
        getCompletedList();
        return view;
    }

    void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    void getCompletedList() {
        customProgressBar.showProgress();
        apiService.adminJobsList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<AdminListModel>() {
            @Override
            public void onResponse(Call<AdminListModel> call, Response<AdminListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    text.setVisibility(response.body().getResponse().getCompletedJobs().size() > 0 ? View.GONE : View.VISIBLE);
                    recyclerView.setAdapter(new HistoryCompletedJobsAdapter(getActivity(),
                            response.body().getResponse().getCompletedJobs()));
                }
            }

            @Override
            public void onFailure(Call<AdminListModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }
}