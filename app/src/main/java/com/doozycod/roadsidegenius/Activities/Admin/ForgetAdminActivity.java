package com.doozycod.roadsidegenius.Activities.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ForgetAdminActivity extends AppCompatActivity {
    CustomProgressBar customProgressBar;
    ApiService apiService;
    EditText emailET;
    Button forgotPasswordButton;
    SharedPreferenceMethod sharedPreferenceMethod;
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
        setContentView(R.layout.activity_forget_admin);
        emailET = findViewById(R.id.emailET);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        apiService = ApiUtils.getAPIService();
        customProgressBar = new CustomProgressBar(this);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailET.getText().toString().equals("")) {
                    Toast.makeText(ForgetAdminActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else {
                    if (getIntent().getStringExtra("type").equals("admin")) {
                        forgotPassword(emailET.getText().toString());
                        return;
                    }
                    if (getIntent().getStringExtra("type").equals("driver")) {
                        forgotDriverPassword(emailET.getText().toString());
                    }
                }
            }
        });
    }

    void forgotDriverPassword(String email) {
        customProgressBar.showProgress();
        apiService.recoverDriverPassword(email).enqueue(new Callback<CustomerLoginModel>() {
            @Override
            public void onResponse(Call<CustomerLoginModel> call, Response<CustomerLoginModel> response) {
                customProgressBar.hideProgress();
                Toast.makeText(ForgetAdminActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<CustomerLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    void forgotPassword(String email) {
        customProgressBar.showProgress();
        apiService.recoverPassword(email).enqueue(new Callback<CustomerLoginModel>() {
            @Override
            public void onResponse(Call<CustomerLoginModel> call, Response<CustomerLoginModel> response) {
                customProgressBar.hideProgress();
                Toast.makeText(ForgetAdminActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<CustomerLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }
}