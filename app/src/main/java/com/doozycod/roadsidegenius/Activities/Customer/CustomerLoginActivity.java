package com.doozycod.roadsidegenius.Activities.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Model.Customer.CustomerLoginModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerLoginActivity extends AppCompatActivity {
    EditText customerNumberET;
    Button senOTPButton;
    ApiService apiService;
    CountryCodePicker countryCodePicker;
    String countryCode = "";
    CustomProgressBar customProgressBar;
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
        setContentView(R.layout.activity_customer_loigin);
        customerNumberET = findViewById(R.id.customerNumberET);
        countryCodePicker = findViewById(R.id.countryCodeLogin);
        senOTPButton = findViewById(R.id.senOTPButton);
        apiService = ApiUtils.getAPIService();
        customProgressBar = new CustomProgressBar(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        countryCodePicker.setAutoDetectedCountry(true);
        countryCodePicker.setCountryForNameCode("US");

        countryCode = countryCodePicker.getSelectedCountryCode();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCode();
            }
        });

        senOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customerNumberET.getText().toString().equals("")) {
                    Toast.makeText(CustomerLoginActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                } else
                    getOtpAPI(countryCode + customerNumberET.getText().toString());
            }
        });


    }

    void getOtpAPI(String number) {
        customProgressBar.showProgress();
        apiService.customerLogin(number).enqueue(new Callback<CustomerLoginModel>() {
            @Override
            public void onResponse(Call<CustomerLoginModel> call, Response<CustomerLoginModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    sharedPreferenceMethod.setTokenJWT(response.body().getResponse().getJwt());
                    startActivity(new Intent(CustomerLoginActivity.this, VerifyOTPActivity.class));

                } else {
                    Toast.makeText(CustomerLoginActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CustomerLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Toast.makeText(CustomerLoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}