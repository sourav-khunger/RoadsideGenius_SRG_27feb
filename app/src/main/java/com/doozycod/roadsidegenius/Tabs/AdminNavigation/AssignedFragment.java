package com.doozycod.roadsidegenius.Tabs.AdminNavigation;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Adapter.AdminAdapter.AssignJobAdapter;
import com.doozycod.roadsidegenius.Model.AssignedJobs.AssignJobsListModel;
import com.doozycod.roadsidegenius.Model.AssignedJobs.Job;
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


public class AssignedFragment extends Fragment implements AssignJobAdapter.OnJobFinishCallback {

    RecyclerView recyclerView;
    SharedPreferenceMethod sharedPreferenceMethod;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    List<Job> jobs = new ArrayList<>();

    public AssignedFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        View view = localInflater.inflate(R.layout.fragment_assigned, container, false);

        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getAssignedJobsAPI();
        return view;
    }

    void getAssignedJobsAPI() {
        customProgressBar.showProgress();
        apiService.assignJobsList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<AssignJobsListModel>() {
            @Override
            public void onResponse(Call<AssignJobsListModel> call, Response<AssignJobsListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    jobs = response.body().getResponse().getJobs();
                    if (jobs.size() == 0) {
                        Toast.makeText(getActivity(), "No assigned jobs yet!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    recyclerView.setAdapter(new AssignJobAdapter(getActivity(), jobs, apiService, AssignedFragment.this::onJobFinish));
                }
            }

            @Override
            public void onFailure(Call<AssignJobsListModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    @Override
    public void onJobFinish(boolean refresh) {
        getAssignedJobsAPI();
        Log.e("TAG onJobFinish", refresh + "");
    }
}