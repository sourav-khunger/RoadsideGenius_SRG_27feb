package com.doozycod.roadsidegenius.Activities.Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.Company.Company;
import com.doozycod.roadsidegenius.Model.Company.CompanyModel;
import com.doozycod.roadsidegenius.Model.DriverList.Driver;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    Driver driver;
    Toolbar toolbar;
    EditText nameET, emailET, numberET, driverET, driverAddressET, driverZipcodeET, payperJobET,
            serviceAreaET, serviceTypeET,
            serviceModelET, serviceMakeET;
    Spinner vendorIDSpinner, serviceYearET;
    Button addDriverButton;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    HashMap<String, String> hashMap = new HashMap<>();
    List<Company> companyList = new ArrayList<>();
    List<String> vendorIdList = new ArrayList<>();
    List<String> companyIdList = new ArrayList<>();
    CustomProgressBar customProgressBar;
    ArrayAdapter aa;
    Bundle bundle = new Bundle();
    List<String> yearList = new ArrayList<>();

    int year = Calendar.getInstance().get(Calendar.YEAR);

    void getYears() {
        yearList.add(year + "");
        for (int i = 15; i > 0; i--) {
            yearList.add((year - 1) + "");
            year--;
        }
    }

    void initUI() {
        addDriverButton = findViewById(R.id.addDriverButton);
        vendorIDSpinner = findViewById(R.id.vendorIDSpinner);
        serviceMakeET = findViewById(R.id.serviceMakeET);
        serviceYearET = findViewById(R.id.serviceYearET);
        serviceModelET = findViewById(R.id.serviceModelET);
        serviceTypeET = findViewById(R.id.serviceTypeET);
        serviceAreaET = findViewById(R.id.serviceAreaET);
        payperJobET = findViewById(R.id.payperJobET);
        driverZipcodeET = findViewById(R.id.driverZipcodeET);
        driverAddressET = findViewById(R.id.driverAddressET);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        driverET = findViewById(R.id.driverET);
        getYears();
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yearList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceYearET.setAdapter(aa);
    }

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
        setContentView(R.layout.activity_edit);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        apiService = ApiUtils.getAPIService();
        customProgressBar = new CustomProgressBar(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        initUI();
        vendorIdList.add("Select Company");
        companyIdList.add("Select Company");

        getCompanyList();
        bundle = getIntent().getExtras();
        driver = (Driver) bundle.getSerializable("driver");
        /*
        AddDriverFragment myFragment = new AddDriverFragment();
        Bundle args = new Bundle();
        args.putSerializable("driver", (Serializable) driver);
        myFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, myFragment).commit();*/

        onClickEvents();

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

    private void onClickEvents() {

        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Driver Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(emailET.getText().toString())) {
                    Toast.makeText(EditActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (numberET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Driver Number", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (driverET.getText().toString().equals("")) {
//                    Toast.makeText(EditActivity.this, "Please enter Driver ID", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (driverAddressET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Driver Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (driverZipcodeET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Driver Zipcode", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (payperJobET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Pay Per Job", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceAreaET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Service Area", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceTypeET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Service Vehicle Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceModelET.getText().toString().equals("")) {

                    Toast.makeText(EditActivity.this, "Please enter Service Vehicle Model", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceMakeET.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "Please enter Service Vehicle Make", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    editDriverAPI(emailET.getText().toString(), numberET.getText().toString(), nameET.getText().toString(),
                            driver.getId(), driverAddressET.getText().toString(), driverZipcodeET.getText().toString(),
                            serviceAreaET.getText().toString(), payperJobET.getText().toString(), serviceTypeET.getText().toString(),
                            serviceModelET.getText().toString(), yearList.get(serviceYearET.getSelectedItemPosition()), serviceMakeET.getText().toString());
                }
            }
        });
    }

    void editDriverAPI(String email, String number, String name, String driverId, String driverAddress,
                       String driverZipCode, String serviceArea, String payPerJob, String serviceVehicleType,
                       String serviceVehicleModel, String serviceVehicleYear, String serviceVehicleMake) {

        customProgressBar.showProgress();
        apiService.editDriverDetails(sharedPreferenceMethod.getTokenJWT(), driverId, email, number, name,
                vendorIdList.get(vendorIDSpinner.getSelectedItemPosition()), driverAddress, driverZipCode, serviceArea,
                payPerJob, serviceVehicleType, serviceVehicleModel, serviceVehicleYear, serviceVehicleMake).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(EditActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
//                    emailET.setText("");
//                    nameET.setText("");
//                    numberET.setText("");
//                    driverET.setText("");
//                    driverAddressET.setText("");
//                    driverZipcodeET.setText("");
//                    serviceAreaET.setText("");
//                    serviceTypeET.setText("");
//                    serviceMakeET.setText("");
//                    serviceYearET.setText("");
//                    serviceModelET.setText("");
//                    payperJobET.setText("");
                } else {
                    Toast.makeText(EditActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Toast.makeText(EditActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCompanyList() {
        customProgressBar.showProgress();
        apiService.getCompanyLists(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<CompanyModel>() {
            @Override
            public void onResponse(Call<CompanyModel> call, Response<CompanyModel> response) {
                customProgressBar.hideProgress();

//                companyIdList = new ArrayList<>();
//                vendorIdList = new ArrayList<>();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    companyList = response.body().getResponse().getCompanies();
                    for (int i = 0; i < response.body().getResponse().getCompanies().size(); i++) {
                        companyIdList.add(response.body().getResponse().getCompanies().get(i).getCompanyName());
                        vendorIdList.add(response.body().getResponse().getCompanies().get(i).getId());
                    }

                    aa = new ArrayAdapter(EditActivity.this, android.R.layout.simple_spinner_item, companyIdList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vendorIDSpinner.setAdapter(aa);
//                    if (getIntent().getExtras().containsKey("driver")) {
                    setData();
//                    }
                } else {
                    Toast.makeText(EditActivity.this, "No Companies Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompanyModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    void setData() {
        Log.e("TAG", "setData: " + driver.getVendorId());

        for (int i = 0; i < vendorIdList.size(); i++) {
            if (vendorIdList.get(i).equals(driver.getVendorId())) {
                vendorIDSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < yearList.size(); i++) {
            if (yearList.get(i).equals(driver.getServiceVehicleYear())) {
                serviceYearET.setSelection(i);
                break;
            }
        }
        serviceMakeET.setText(driver.getServiceVehicleMake());
//        serviceYearET.setText(driver.getServiceVehicleYear());
        serviceModelET.setText(driver.getServiceVehicleModel());
        serviceTypeET.setText(driver.getServiceVehicleType());
        serviceAreaET.setText(driver.getServiceArea());
        payperJobET.setText(driver.getPayPerJob());
        driverZipcodeET.setText(driver.getDriverZipcode());
        driverAddressET.setText(driver.getDriverAddress());
        numberET.setText(driver.getDriverNumber());
        emailET.setText(driver.getDriverEmail());
        nameET.setText(driver.getDriverName());
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}