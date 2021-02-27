package com.doozycod.roadsidegenius.Activities.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {
    ApiService apiService;
    EditText adminLoginEmailET, adminLoginpass;
    Button adminSigninButton;
    String android_id;
    CustomProgressBar customProgressBar;
    SharedPreferenceMethod sharedPreferenceMethod;
    TextView forgetButton;
    CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
//        sharedPreferenceMethod.setTheme("dark");
        if (sharedPreferenceMethod != null) {
            setTheme(sharedPreferenceMethod.getTheme().equals("light") ? R.style.LightTheme : R.style.DarkTheme);
        }
        setContentView(R.layout.activity_admin_login);
        rememberMeCheckBox = findViewById(R.id.checkBox);
        forgetButton = findViewById(R.id.forgetButton);
        adminLoginEmailET = findViewById(R.id.adminLoginEmailET);
        adminLoginpass = findViewById(R.id.adminLoginpass);
        adminSigninButton = findViewById(R.id.adminSigninButton);
        apiService = ApiUtils.getAPIService();
        customProgressBar = new CustomProgressBar(this);
//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sharedPreferenceMethod.saveDeviceID(android_id);

        if (sharedPreferenceMethod.getLoginEmail() != null) {
            rememberMeCheckBox.setChecked(true);
            if (sharedPreferenceMethod.getLogin().equals("admin")) {

                adminLoginEmailET.setText(sharedPreferenceMethod.getLoginEmail());
                adminLoginpass.setText(sharedPreferenceMethod.getLoginPassword());
            }
        }
        onClickEvents();

    }

    private void onClickEvents() {
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLoginActivity.this, ForgetAdminActivity.class);
                intent.putExtra("type", "admin");
                startActivity(intent);
            }
        });
        adminSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminLoginEmailET.getText().toString().equals("")) {
                    Toast.makeText(AdminLoginActivity.this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (adminLoginpass.getText().toString().equals("")) {
                    Toast.makeText(AdminLoginActivity.this, "Please enter your password!", Toast.LENGTH_SHORT).show();

                    return;
                } else
                    loginAPI(adminLoginEmailET.getText().toString(), adminLoginpass.getText().toString());
            }
        });
    }

    void loginAPI(String email, String password) {
        customProgressBar.showProgress();
        apiService.adminLogin(email, password, android_id, sharedPreferenceMethod.getFCMToken()).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {

                    if (rememberMeCheckBox.isChecked()) {
                        sharedPreferenceMethod.saveLoginPassword(email, password);
                    } else {
                        sharedPreferenceMethod.saveLoginPassword("", "");

                    }
                    sharedPreferenceMethod.saveUser(response.body().getResponse().getUserData().getId(),
                            response.body().getResponse().getUserData().getAdminName(),
                            response.body().getResponse().getUserData().getAdminEmail(),
                            response.body().getResponse().getUserData().getAdminContact(),
                            response.body().getResponse().getUserData().getType(),
                            response.body().getResponse().getJwt());
                    sharedPreferenceMethod.saveUserType("admin");

                    Toast.makeText(AdminLoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminLoginActivity.this, DashboardAdminActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(AdminLoginActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Toast.makeText(AdminLoginActivity.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
                customProgressBar.hideProgress();
            }
        });
    }
}