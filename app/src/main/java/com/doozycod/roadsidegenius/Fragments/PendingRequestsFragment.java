package com.doozycod.roadsidegenius.Fragments;

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

import com.doozycod.roadsidegenius.Adapter.WithdrawPendingRequestAdapter;
import com.doozycod.roadsidegenius.Model.WithdrawList.Pending;
import com.doozycod.roadsidegenius.Model.WithdrawList.WithdrawListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingRequestsFragment extends Fragment {


    public PendingRequestsFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    TextView text;

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
        View view = localInflater.inflate(R.layout.fragment_pending_requests, container, false);
        text = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.recyclerView);

        customProgressBar = new CustomProgressBar(getActivity());
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        apiService = ApiUtils.getAPIService();
        recyclerViewInit();


        getWithdrawListAPI();
        return view;
    }

    void recyclerViewInit() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    void getWithdrawListAPI() {
        customProgressBar.showProgress();
        apiService.getWithdrawList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<WithdrawListModel>() {
            @Override
            public void onResponse(Call<WithdrawListModel> call, Response<WithdrawListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    recyclerView.setAdapter(new WithdrawPendingRequestAdapter(getActivity(),
                            response.body().getResponse().getPending()));
                    List<Pending> pending = response.body().getResponse().getPending();
                    text.setVisibility(pending.size() > 0 ? View.GONE : View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<WithdrawListModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                customProgressBar.hideProgress();

            }
        });

    }
}