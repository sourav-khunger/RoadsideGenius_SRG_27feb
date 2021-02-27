package com.doozycod.roadsidegenius.Activities.Driver;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Activities.Admin.ForgetAdminActivity;
import com.doozycod.roadsidegenius.Activities.MainActivity;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Model.Company.Company;
import com.doozycod.roadsidegenius.Model.Company.CompanyModel;
import com.doozycod.roadsidegenius.Model.DriverLogin.DriverLoginModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverLoginActvity extends AppCompatActivity {
    Spinner spinner;
    List<String> vendors = new ArrayList<>();
    Button signinButton;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    List<Company> companyList = new ArrayList<>();
    List<String> vendorIdList = new ArrayList<>();
    List<String> companyIdList = new ArrayList<>();
    EditText emailET, passwordET;
    ArrayAdapter aa;
    CustomProgressBar customProgressBar;
    TextView forgetButton;
    CheckBox rememberMeCheckBox;

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
        setContentView(R.layout.activity_driver_login_actvity);
        rememberMeCheckBox = findViewById(R.id.checkBox);
        signinButton = findViewById(R.id.signinButton);
        spinner = findViewById(R.id.vendorIdSpinner);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        forgetButton = findViewById(R.id.forgetButton);

        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        vendors.add("Select Company");

        if (sharedPreferenceMethod.getDriverEmail() != null) {
            rememberMeCheckBox.setChecked(true);
            if (sharedPreferenceMethod.getLogin().equals("driver")) {

                emailET.setText(sharedPreferenceMethod.getDriverEmail());
                passwordET.setText(sharedPreferenceMethod.getDriverPassword());
            }
        }

        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DriverLoginActvity.this, MainActivity.class));
            }
        });

        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DriverLoginActvity.this, ForgetAdminActivity.class);
                intent.putExtra("type", "driver");
                startActivity(intent);
            }
        });
//        getCompanyList();

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, vendors);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (spinner.getSelectedItemPosition() == 0) {
//                    Toast.makeText(DriverLoginActvity.this, "Please select a company", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (emailET.getText().toString().equals("")) {
                    Toast.makeText(DriverLoginActvity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordET.getText().toString().equals("")) {
                    Toast.makeText(DriverLoginActvity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    loginDriverAPI(emailET.getText().toString(), passwordET.getText().toString(),
                            sharedPreferenceMethod.getDeviceId(), sharedPreferenceMethod.getFCMToken());
                }
            }
        });
    }

    void loginDriverAPI(String email, String password, String deviceId, String pushToken) {
        customProgressBar.showProgress();
        apiService.loginDriver(email, password, deviceId, pushToken).enqueue(new Callback<DriverLoginModel>() {
            @Override
            public void onResponse(Call<DriverLoginModel> call, Response<DriverLoginModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    sharedPreferenceMethod.saveUserType("driver");

                    if (rememberMeCheckBox.isChecked()) {
                        sharedPreferenceMethod.saveDriverLoginPassword(email, password);
                    } else {
                        sharedPreferenceMethod.saveDriverLoginPassword("", "");
                    }
                    sharedPreferenceMethod.saveDriverId(response.body().getResponse().getUserData().getId());
                    sharedPreferenceMethod.setTokenJWT(response.body().getResponse().getJwt());
                    Log.d("saved driver JWT", sharedPreferenceMethod.getTokenJWT());
                    Toast.makeText(DriverLoginActvity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DriverLoginActvity.this, DriverDashboardActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(DriverLoginActvity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DriverLoginModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Toast.makeText(DriverLoginActvity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getCompanyList() {
        apiService.getCompanyLists(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<CompanyModel>() {
            @Override
            public void onResponse(Call<CompanyModel> call, Response<CompanyModel> response) {
                customProgressBar.hideProgress();

                companyIdList = new ArrayList<>();
                vendorIdList = new ArrayList<>();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    companyList = response.body().getResponse().getCompanies();
                    for (int i = 0; i < response.body().getResponse().getCompanies().size(); i++) {
                        companyIdList.add(response.body().getResponse().getCompanies().get(i).getId());
                        vendorIdList.add(response.body().getResponse().getCompanies().get(i).getVendorId());
                    }

                    aa = new ArrayAdapter(DriverLoginActvity.this, android.R.layout.simple_spinner_item, vendorIdList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(aa);
                } else {
                    Toast.makeText(DriverLoginActvity.this, "No Companies Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyModel> call, Throwable t) {
                Toast.makeText(DriverLoginActvity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}