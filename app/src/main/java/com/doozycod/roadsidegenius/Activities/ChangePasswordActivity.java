package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.Customer.CustomerLoginModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    CustomProgressBar customProgressBar;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    EditText oldPasswordET, newPasswordET;
    Button submitButton;
    Toolbar toolbar;

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
        setContentView(R.layout.activity_change_password);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        oldPasswordET = findViewById(R.id.oldPasswordET);
        newPasswordET = findViewById(R.id.newPasswordET);
        submitButton = findViewById(R.id.submitButton);

        customProgressBar = new CustomProgressBar(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        apiService = ApiUtils.getAPIService();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldPasswordET.getText().toString().equals("")) {
                    Toast.makeText(ChangePasswordActivity.this, "Enter your current password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oldPasswordET.getText().toString().equals("")) {
                    Toast.makeText(ChangePasswordActivity.this, "Enter your new password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oldPasswordET.getText().toString().length() < 6) {
                    Toast.makeText(ChangePasswordActivity.this, "password should be at least 7 digits", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (sharedPreferenceMethod.getLogin().equals("admin")) {
                        changePasswordAdmin();
                    } else {
                        changePassword();
                    }
                }
            }
        });
    }

    void changePassword() {
        customProgressBar.showProgress();
        apiService.changeDriverPassword(sharedPreferenceMethod.getTokenJWT(),
                oldPasswordET.getText().toString(), newPasswordET.getText().toString()).enqueue(new Callback<CustomerLoginModel>() {
            @Override
            public void onResponse(Call<CustomerLoginModel> call, Response<CustomerLoginModel> response) {
//                if (response.body().getResponse().getStatus().equals("Success")) {
//                }else{
                customProgressBar.hideProgress();
                finish();
//                }
                Toast.makeText(ChangePasswordActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<CustomerLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    void changePasswordAdmin() {
        customProgressBar.showProgress();
        apiService.changeAdminPassword(sharedPreferenceMethod.getTokenJWT(),
                oldPasswordET.getText().toString(), newPasswordET.getText().toString()).enqueue(new Callback<CustomerLoginModel>() {
            @Override
            public void onResponse(Call<CustomerLoginModel> call, Response<CustomerLoginModel> response) {
//                if (response.body().getResponse().getStatus().equals("Success")) {
//                }else{
                customProgressBar.hideProgress();
                finish();

//                }
                Toast.makeText(ChangePasswordActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<CustomerLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

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