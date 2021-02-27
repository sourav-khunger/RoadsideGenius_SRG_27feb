package com.doozycod.roadsidegenius.Activities.Customer;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Activities.Driver.DriverDashboardActivity;
import com.doozycod.roadsidegenius.Activities.NotificationActivity;
import com.doozycod.roadsidegenius.Fragments.CustomerServiceListFragment;
import com.doozycod.roadsidegenius.Fragments.ServiceFragment;
import com.doozycod.roadsidegenius.Fragments.SettingsFragment;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.menu.DrawerAdapter;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.menu.DrawerItem;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.menu.SimpleItem;
import com.doozycod.roadsidegenius.Fragments.CustomerRequestTabsFragment;
import com.doozycod.roadsidegenius.Fragments.RequestServiceFragment;
import com.doozycod.roadsidegenius.Activities.LoginTypeActvvity;
import com.doozycod.roadsidegenius.Model.Customer.CustomerLoginModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardCustomerActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_REQUEST = 1;
    private static final int POS_DASHBOARD = 0;
    private static final int POS_SETTINGS = 2;
    private static final int POS_LOGOUT = 3;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    SharedPreferenceMethod sharedPreferenceMethod;
    Toolbar toolbar;
    TextView toolbar_title;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    ImageButton notificationButton;

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
        setContentView(R.layout.activity_dashboard_customer);

        notificationButton = findViewById(R.id.notificationButton);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
//        toolbar.setTitle("Dashboard");


        customProgressBar = new CustomProgressBar(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        apiService = ApiUtils.getAPIService();
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        toolbar_title.setText("Request a Service");
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_REQUEST),
                createItemFor(POS_SETTINGS),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardCustomerActivity.this, NotificationActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(int position) {
        Fragment selectedScreen;
        switch (position) {
            case POS_DASHBOARD:
                toolbar_title.setText("Dashboard");
                selectedScreen = new CustomerRequestTabsFragment();
                showFragment(selectedScreen);
                break;
            case POS_REQUEST:
                toolbar_title.setText("Request a service");
                selectedScreen = new CustomerServiceListFragment();
                showFragment(selectedScreen);
                break;
            case POS_SETTINGS:
                toolbar_title.setText("Settings");
                selectedScreen = new SettingsFragment();
                showFragment(selectedScreen);
                break;
            case POS_LOGOUT:
                logoutAPI();
                break;


        }
        slidingRootNav.closeMenu();

    }

    void logoutAPI() {
        customProgressBar.showProgress();
        apiService.customerLogout(sharedPreferenceMethod.getTokenJWT(), sharedPreferenceMethod.getDeviceId()).enqueue(new Callback<CustomerLoginModel>() {
            @Override
            public void onResponse(Call<CustomerLoginModel> call, Response<CustomerLoginModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    sharedPreferenceMethod.removeLogin();
                    startActivity(new Intent(DashboardCustomerActivity.this, LoginTypeActvvity.class));
                    finishAffinity();
                    Toast.makeText(DashboardCustomerActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DashboardCustomerActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitlesCustomer);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIconsCustomer);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(sharedPreferenceMethod.getTheme()
                        .equals("dark") ? color(R.color.white) : color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.teal_200))
                .withSelectedTextTint(sharedPreferenceMethod.getTheme()
                        .equals("dark") ? color(R.color.quantum_grey300) : color(R.color.teal_200));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}