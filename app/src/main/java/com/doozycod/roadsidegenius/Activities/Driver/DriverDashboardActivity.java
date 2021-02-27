package com.doozycod.roadsidegenius.Activities.Driver;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Activities.LoginTypeActvvity;
import com.doozycod.roadsidegenius.Activities.NotificationActivity;
import com.doozycod.roadsidegenius.Fragments.AdminDriverCreateJobFragment;
import com.doozycod.roadsidegenius.Fragments.PaymentsWithdrawFragment;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.menu.DrawerAdapter;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.menu.DrawerItem;
import com.doozycod.roadsidegenius.Tabs.AdminNavigation.menu.SimpleItem;
import com.doozycod.roadsidegenius.Fragments.RequestServiceFragment;
import com.doozycod.roadsidegenius.Fragments.AnalyticsFragment;
import com.doozycod.roadsidegenius.Fragments.ChatFragment;
import com.doozycod.roadsidegenius.Fragments.SettingsFragment;
import com.doozycod.roadsidegenius.Fragments.SupportFragment;
import com.doozycod.roadsidegenius.Fragments.TaskHistoryFragment;
import com.doozycod.roadsidegenius.Fragments.TaskListFragment;
import com.doozycod.roadsidegenius.Fragments.UploadDriverFragment;
import com.doozycod.roadsidegenius.Model.Customer.CustomerLoginModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.LocationService;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashboardActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_TASK_LIST = 0;
    //    private static final int POS_TASK_HISTORY = 1;
    private static final int POS_PAYMENTS = 1;
    private static final int POS_CREATE_JOB = 3;
    private static final int POS_UPLOAD_FILES = 2;
    //    private static final int POS_ANALYTICS = 5;
//    private static final int POS_CHAT = 6;
    private static final int POS_SETTINGS = 4;
    //    private static final int POS_SUPPORT = 8;
    private static final int POS_LOGOUT = 5;

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
        setContentView(R.layout.activity_driver_dashboard);

        notificationButton = findViewById(R.id.notificationButton);
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
//        toolbar.setTitle("Dashboard");

        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        toolbar_title.setText("Task List");
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

//        permission request
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();


        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_TASK_LIST).setChecked(true),
//                createItemFor(POS_TASK_HISTORY),
                createItemFor(POS_PAYMENTS),
                createItemFor(POS_CREATE_JOB),
                createItemFor(POS_UPLOAD_FILES),
//                createItemFor(POS_ANALYTICS),
//                createItemFor(POS_CHAT),
                createItemFor(POS_SETTINGS),
//                createItemFor(POS_SUPPORT),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_TASK_LIST);

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverDashboardActivity.this, NotificationActivity.class));
            }
        });


        stopService(new Intent(getApplicationContext(), LocationService.class));
        startService(new Intent(getApplicationContext(), LocationService.class));

    }

    @Override
    public void onItemSelected(int position) {
        Fragment selectedScreen;
        switch (position) {
//            case POS_TASK_HISTORY:
//                toolbar_title.setText("Task History");
//                selectedScreen = TaskHistoryFragment.newInstance(screenTitles[position]);
//                showFragment(selectedScreen);
//                break;
            case POS_PAYMENTS:

                toolbar_title.setText("Payments");
                selectedScreen = new PaymentsWithdrawFragment();
                showFragment(selectedScreen);
                break;
            case POS_UPLOAD_FILES:
                toolbar_title.setText("Roadside Genius");
                selectedScreen = new UploadDriverFragment();
                showFragment(selectedScreen);
                break;
            case POS_CREATE_JOB:
                toolbar_title.setText("Create Job");
                selectedScreen = new AdminDriverCreateJobFragment();
                showFragment(selectedScreen);

                break;
//            case POS_ANALYTICS:
//                toolbar_title.setText("Analytics");
//                selectedScreen = AnalyticsFragment.newInstance(screenTitles[position]);
//                showFragment(selectedScreen);
//                break;
//            case POS_CHAT:
//                toolbar_title.setText("Chat");
//                selectedScreen = ChatFragment.newInstance(screenTitles[position]);
//                showFragment(selectedScreen);
//                break;
            case POS_SETTINGS:
                toolbar_title.setText("Settings");
                selectedScreen = new SettingsFragment();
                showFragment(selectedScreen);
                break;
//            case POS_SUPPORT:
//                toolbar_title.setText("Support");
//                selectedScreen = SupportFragment.newInstance(screenTitles[position], "None");
//                showFragment(selectedScreen);
//                break;
            case POS_LOGOUT:
                logoutAPI();
                break;
            default:
                toolbar_title.setText("Dashboard");
                selectedScreen = TaskListFragment.newInstance(screenTitles[position]);
                showFragment(selectedScreen);
                break;

        }
        slidingRootNav.closeMenu();

    }

    void logoutAPI() {
        customProgressBar.showProgress();
        apiService.driverLogout(sharedPreferenceMethod.getTokenJWT(), sharedPreferenceMethod.getDeviceId()).enqueue(new Callback<CustomerLoginModel>() {
            @Override
            public void onResponse(Call<CustomerLoginModel> call, Response<CustomerLoginModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    sharedPreferenceMethod.removeLogin();
                    sharedPreferenceMethod.saveLoginPassword("", "");
                    startActivity(new Intent(DriverDashboardActivity.this, LoginTypeActvvity.class));
                    finishAffinity();
                    Toast.makeText(DriverDashboardActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DriverDashboardActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            f.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitlesDriver);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIconsDriver);
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