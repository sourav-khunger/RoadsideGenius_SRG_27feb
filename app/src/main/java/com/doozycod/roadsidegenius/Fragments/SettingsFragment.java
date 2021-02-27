package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doozycod.roadsidegenius.Activities.Admin.DashboardAdminActivity;
import com.doozycod.roadsidegenius.Activities.ChangePasswordActivity;
import com.doozycod.roadsidegenius.Activities.Customer.DashboardCustomerActivity;
import com.doozycod.roadsidegenius.Activities.Driver.DriverDashboardActivity;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchAnimListener;
import com.mahfa.dnswitch.DayNightSwitchListener;

import org.w3c.dom.Text;


public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    TextView changePassword;
    SharedPreferenceMethod sharedPreferenceMethod;
    private DayNightSwitch day_night_switch;
    LinearLayout hideLayout;

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
        View view = localInflater.inflate(R.layout.fragment_settings, container, false);
        hideLayout = view.findViewById(R.id.hideLayout);
        changePassword = view.findViewById(R.id.changePassword);
        day_night_switch = view.findViewById(R.id.day_night_switch);
        day_night_switch.setDuration(350);
        if (sharedPreferenceMethod.getTheme().equals("dark")) {
            day_night_switch.setIsNight(true, true);
        }
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });

        hideLayout.setVisibility(sharedPreferenceMethod.getLogin().equals("customer") ? View.GONE : View.VISIBLE);
        day_night_switch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                Log.e("TAG", "onSwitch: " + is_night);
                sharedPreferenceMethod.setTheme(is_night ? "dark" : "light");
            }
        });

        day_night_switch.setAnimListener(new DayNightSwitchAnimListener() {
            @Override
            public void onAnimStart() {
//                Log.d(TAG, "onAnimStart() called");
            }

            @Override
            public void onAnimEnd() {
//                Log.d(TAG, "onAnimEnd() called");
                if (sharedPreferenceMethod.getLogin().equals("customer")) {
                    startActivity(new Intent(getActivity(), DashboardCustomerActivity.class));
                }
                if (sharedPreferenceMethod.getLogin().equals("driver")) {
                    startActivity(new Intent(getActivity(), DriverDashboardActivity.class));
                }
                if (sharedPreferenceMethod.getLogin().equals("admin")) {
                    startActivity(new Intent(getActivity(), DashboardAdminActivity.class));
                }
            }

            @Override
            public void onAnimValueChanged(float value) {
//                background_view.setAlpha(value);
//                Log.d(TAG, "onAnimValueChanged() called with: value = [" + value + "]");
            }
        });
        return view;
    }
}