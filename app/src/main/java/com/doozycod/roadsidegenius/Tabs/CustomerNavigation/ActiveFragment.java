package com.doozycod.roadsidegenius.Tabs.CustomerNavigation;

import android.content.Context;
import android.os.Bundle;

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

import com.doozycod.roadsidegenius.Adapter.CustomerAdapter.ActiveJobsCustomerAdapter;
import com.doozycod.roadsidegenius.Model.JobList.Active;
import com.doozycod.roadsidegenius.Model.JobList.JobsListModel;
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


public class ActiveFragment extends Fragment {


    public ActiveFragment() {
        // Required empty public constructor
    }

    List<Active> actives = new ArrayList<>();
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    RecyclerView recyclerView;
    TextView text;

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
        View view = localInflater.inflate(R.layout.fragment_active, container, false);

        text = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.recyclerView);

        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());
        apiService = ApiUtils.getAPIService();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getActiveJobsAPI();
        return view;
    }

    void getActiveJobsAPI() {
//        customProgressBar.showProgress();
        apiService.getJobsList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<JobsListModel>() {
            @Override
            public void onResponse(Call<JobsListModel> call, Response<JobsListModel> response) {
//                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    actives = response.body().getResponse().getActive();
                    if (actives.size() > 0) {
                        text.setVisibility(View.GONE);
                        recyclerView.setAdapter(new ActiveJobsCustomerAdapter(getActivity(), actives));
                    } else {
                        text.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<JobsListModel> call, Throwable t) {
//                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}