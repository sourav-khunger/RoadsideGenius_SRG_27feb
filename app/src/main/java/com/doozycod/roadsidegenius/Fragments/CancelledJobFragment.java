package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doozycod.roadsidegenius.Adapter.AdminCancelledJobAdapter;
import com.doozycod.roadsidegenius.Adapter.GoneOnArrivalAdapter;
import com.doozycod.roadsidegenius.Model.AdminJobListModel.AdminListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelledJobFragment extends Fragment {


    public CancelledJobFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    SharedPreferenceMethod sharedPreferenceMethod;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    TextView text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        View view = localInflater.inflate(R.layout.fragment_cancelled_job, container, false);
        text = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView();

        apiService = ApiUtils.getAPIService();
        customProgressBar = new CustomProgressBar(getActivity());
        cancelledJobsAPI();
        return view;
    }

    void cancelledJobsAPI() {
        apiService.adminJobsList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<AdminListModel>() {
            @Override
            public void onResponse(Call<AdminListModel> call, Response<AdminListModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    recyclerView.setAdapter(new AdminCancelledJobAdapter(getActivity(),
                            response.body().getResponse().getCancelledJobs()));
                }
                text.setVisibility(response.body().getResponse()
                        .getGoneAllArrivalJobs().size() > 0 ?
                        View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailure(Call<AdminListModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}