package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.doozycod.roadsidegenius.Adapter.NotificationAdapter;
import com.doozycod.roadsidegenius.Adapter.NotificationAdminAdapter;
import com.doozycod.roadsidegenius.Adapter.NotificationDriverAdapter;
import com.doozycod.roadsidegenius.Model.Notification.NotificationListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomProgressBar customProgressBar;
    SharedPreferenceMethod sharedPreferenceMethod;
    ApiService apiService;
    Toolbar toolbar;
    LinearLayout hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
//        sharedPreferenceMethod.setTheme("dark");
        if (sharedPreferenceMethod != null) {
            setTheme(sharedPreferenceMethod.getTheme().equals("light") ? R.style.LightTheme : R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_notification);
        recyclerView = findViewById(R.id.recyclerView);
        hide = findViewById(R.id.hide);

        initToolbar();

//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();

//        recyclerView Properties
        initRecyclerView();

        getNotifications();
    }

    void getNotifications() {
        customProgressBar.showProgress();
        apiService.getNotifications(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<NotificationListModel>() {
            @Override
            public void onResponse(Call<NotificationListModel> call, Response<NotificationListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    if (response.body().getResponse().getNotifications().size() > 0) {
                        hide.setVisibility(View.GONE);

                    } else {
                        hide.setVisibility(View.VISIBLE);
                    }
                    if (sharedPreferenceMethod.getLogin().equals("driver")) {
                        recyclerView.setAdapter(new NotificationDriverAdapter(NotificationActivity.this,
                                response.body().getResponse().getNotifications(),apiService));
                        return;
                    }
                    if (sharedPreferenceMethod.getLogin().equals("admin")) {

                        recyclerView.setAdapter(new NotificationAdminAdapter(NotificationActivity.this,
                                response.body().getResponse().getNotifications(),apiService));
                        return;

                    } else {
                        recyclerView.setAdapter(new NotificationAdapter(NotificationActivity.this,
                                response.body().getResponse().getNotifications(),apiService));
                    }

                }
            }

            @Override
            public void onFailure(Call<NotificationListModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: "+t.getMessage() );
            }
        });
    }

    void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}