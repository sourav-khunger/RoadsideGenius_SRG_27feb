package com.doozycod.roadsidegenius.Tabs.CustomerNavigation;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Adapter.AdminAdapter.CompletedJobsAdapter;
import com.doozycod.roadsidegenius.Model.CompletedJobModel.CompletedJobListModel;
import com.doozycod.roadsidegenius.Model.CompletedJobModel.Job;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedRequestFragment extends Fragment {

    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    RecyclerView recyclerView;
    Calendar calendar;
    String year;
    int month;
    Spinner driverSpinner;
    private Spinner monthSpinner, yearSpinner;
    //    ArrayAdapter aa;
    Button searchButton;
    TextView text;
    private SimpleDateFormat month_date;
    private List<Job> jobs;

    public CompletedRequestFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

    }

    private void initUI(View view) {
        text = view.findViewById(R.id.text);
        driverSpinner = view.findViewById(R.id.driverSpinner);
        searchButton = view.findViewById(R.id.searchButton);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        recyclerView = view.findViewById(R.id.recyclerView);
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
        View view = localInflater.inflate(R.layout.fragment_completed_request, container, false);

        initUI(view);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());
        apiService = ApiUtils.getAPIService();
        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        initRecyclerView();


        completedJobsApi();


        return view;
    }

    void completedJobsApi() {
//        customProgressBar.showProgress();
        int month = (calendar.get(Calendar.MONTH) + 1);
        String iMonth = null;
        if (month < 10) {
            iMonth = "0" + month;
        } else {
            iMonth = String.valueOf(month);
        }

        String startDate = 01 + "-" + iMonth + "-" + (calendar.get(Calendar.YEAR) - 1);
        String endDate = calendar.get(Calendar.DATE) + "-" + iMonth + "-" + (calendar.get(Calendar.YEAR));
        Log.e("TAG", "onCreateView: " + startDate + "  " + endDate);

        apiService.completedJobsList(sharedPreferenceMethod.getTokenJWT(), "", startDate, endDate,
                sharedPreferenceMethod.getCustomerID())
                .enqueue(new Callback<CompletedJobListModel>() {
                    @Override
                    public void onResponse(Call<CompletedJobListModel> call, Response<CompletedJobListModel> response) {
//                        customProgressBar.showProgress();

                        if (response.body().getResponse().getStatus().equals("Success")) {
                            jobs = new ArrayList<>();
                            jobs = response.body().getResponse().getJobs();
                            if (jobs.size() == 0) {
                                text.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "No completed jobs yet!", Toast.LENGTH_SHORT).show();

                            } else {
                                text.setVisibility(View.GONE);

                            }
                            recyclerView.setAdapter(new CompletedJobsAdapter(getActivity(), jobs, apiService));
                        }
                    }

                    @Override
                    public void onFailure(Call<CompletedJobListModel> call, Throwable t) {
                        Log.e("TAG", "onResponse: " + t.getMessage());

                    }
                });
    }

    void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}