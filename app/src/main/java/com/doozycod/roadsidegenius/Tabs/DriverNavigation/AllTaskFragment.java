package com.doozycod.roadsidegenius.Tabs.DriverNavigation;

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
import android.widget.Toast;

import com.doozycod.roadsidegenius.Adapter.AssignJobDriverAdapter;
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

public class AllTaskFragment extends Fragment {
    TextView text;
    RecyclerView recyclerView;
    CustomProgressBar customProgressBar;
    SharedPreferenceMethod sharedPreferenceMethod;
    ApiService apiService;
    List<Job> jobs = new ArrayList<>();
    private String TAG = AllTaskFragment.class.getSimpleName();

    public AllTaskFragment() {
        // Required empty public constructor
    }

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
        View view = localInflater.inflate(R.layout.fragment_all_task, container, false);
        text = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.recyclerView);
        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());


        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAssignedTasks();
        return view;
    }

    private void getAssignedTasks() {

        customProgressBar.showProgress();
        apiService.assignJobsList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<AssignJobsListModel>() {
            @Override
            public void onResponse(Call<AssignJobsListModel> call, Response<AssignJobsListModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    jobs = response.body().getResponse().getJobs();
                    if (jobs.size() == 0) {
                        text.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "No assigned jobs yet!", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    TaskListFragment taskListFragment= new TaskListFragment();
//                    taskListFragment.refreshFragment();
                    text.setVisibility(View.GONE);
                    recyclerView.setAdapter(new AssignJobDriverAdapter(getActivity(), jobs, apiService));
                }

            }

            @Override
            public void onFailure(Call<AssignJobsListModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}