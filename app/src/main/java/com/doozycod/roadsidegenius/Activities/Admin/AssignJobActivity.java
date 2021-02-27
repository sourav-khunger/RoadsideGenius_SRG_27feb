package com.doozycod.roadsidegenius.Activities.Admin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.DriverList.Driver;
import com.doozycod.roadsidegenius.Model.DriverList.DriversListModel;
import com.doozycod.roadsidegenius.Model.JobList.Job;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignJobActivity extends AppCompatActivity {
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    List<Driver> driverList = new ArrayList<>();
    List<String> driverNameList = new ArrayList<>();
    List<String> driverIdList = new ArrayList<>();
    Spinner driverSpinner;
    ArrayAdapter aa;
    Job job;
    Toolbar toolbar;
    DatePickerDialog datePickerDialog;
    ImageView datePickerDialogButton;
    final Calendar myCalendar = Calendar.getInstance();
    TextView dispatchDateTxt, dispatchedTime, customerNameTxt, serviceNeededTxt;
    String myFormat = "MM-dd-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    TimePickerDialog timePickerDialog;
    Button assignButton;
    Spinner etaET;
    EditText siteET, vehicleMakeEt, vehicleModelEt, vehicleColor, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET;
    private List<String> etaList = new ArrayList();

    private void initUI() {
        serviceNeededTxt = findViewById(R.id.serviceNeededTxt);
        customerNameTxt = findViewById(R.id.customerNameTxt);
        vehicleModelEt = findViewById(R.id.vehicleModelEt);
        descriptionET = findViewById(R.id.descriptionET);
        truckET = findViewById(R.id.truckET);
        invoiceTotal = findViewById(R.id.invoiceTotal);
//        total_milesET = findViewById(R.id.total_milesET);
        total_job_time = findViewById(R.id.total_job_time);
        vehicleColor = findViewById(R.id.vehicleColor);
        vehicleMakeEt = findViewById(R.id.vehicleMakeEt);
        siteET = findViewById(R.id.siteET);
        etaET = findViewById(R.id.etaET);
        assignButton = findViewById(R.id.assignButton);
        dispatchedTime = findViewById(R.id.dispatchedTime);
        dispatchDateTxt = findViewById(R.id.dispatchDateTxt);
        datePickerDialogButton = findViewById(R.id.datePickerDialogButton);
        driverSpinner = findViewById(R.id.driverSpinner);
        toolbar = findViewById(R.id.toolbar);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        setContentView(R.layout.activity_assign_job);
//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();
        initUI();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra("job")) {
            job = (Job) getIntent().getSerializableExtra("job");
        }
        datePickerDialog = new DatePickerDialog(this);

        datePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        dispatchDateTxt.setText(sdf.format(new Date()));
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        customerNameTxt.setText(job.getCustomerName());
        serviceNeededTxt.setText(job.getServiceNeeded());
        dispatchedTime.setText(hour + ":" + minute);
        etaList.add("select Estimated ETA");
        etaList.add("15");
        etaList.add("30");
        etaList.add("45");
        etaList.add("60");

        ArrayAdapter aa = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, etaList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etaET.setAdapter(aa);

        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    invoiceTotal.setText(job.getAmountQuoted());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dispatchedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(AssignJobActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dispatchedTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        });
        getDriverList();
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (driverSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(AssignJobActivity.this, "Please select driver", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (siteET.getText().equals("")) {
                    Toast.makeText(AssignJobActivity.this, "Please enter site", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etaET.getSelectedItemPosition() == 0) {
                    Toast.makeText(AssignJobActivity.this, "Please Select eta", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (vehicleMakeEt.getText().equals("")) {
//                    Toast.makeText(AssignJobActivity.this, "Please enter vehicle Make", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (vehicleModelEt.getText().equals("")) {
//                    Toast.makeText(AssignJobActivity.this, "Please enter vehicle Model", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (vehicleColor.getText().equals("")) {
//                    Toast.makeText(AssignJobActivity.this, "Please enter vehicle color", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (total_job_time.getText().equals("")) {
//                    Toast.makeText(AssignJobActivity.this, "Please enter total job time", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (total_milesET.getText().equals("")) {
//                    Toast.makeText(AssignJobActivity.this, "Please enter total miles", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (invoiceTotal.getText().equals("")) {
                    Toast.makeText(AssignJobActivity.this, "Please enter invoice total", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (truckET.getText().equals("")) {
                    Toast.makeText(AssignJobActivity.this, "Please enter truck details", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    assignJob(dispatchDateTxt.getText().toString(), siteET.getText().toString(),
                            etaList.get(etaET.getSelectedItemPosition()),
                            "Assigned", vehicleMakeEt.getText().toString(), vehicleModelEt.getText().toString(),
                            vehicleColor.getText().toString(), dispatchedTime.getText().toString(), "",
                            "", invoiceTotal.getText().toString(), descriptionET.getText().toString(), truckET.getText().toString());
                }
            }
        });
    }

    void assignJob(String dispatchDate, String site, String eta, String status, String make, String model, String color,
                   String dispatchedTime, String totalJobTime, String totalMiles, String invoiceTotal, String comment,
                   String truck) {
        customProgressBar.showProgress();
        apiService.assignJob(sharedPreferenceMethod.getTokenJWT(), job.getId(),
                driverIdList.get(driverSpinner.getSelectedItemPosition()),
                dispatchDate, site, eta, status, make, model, color, dispatchedTime, totalJobTime, totalMiles, invoiceTotal,
                comment, truck).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(AssignJobActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AssignJobActivity.this, DashboardAdminActivity.class));
                    finishAffinity();

                } else {
                    Toast.makeText(AssignJobActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dispatchDateTxt.setText(sdf.format(myCalendar.getTime()));
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

    void getDriverList() {
        apiService.getDriverList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<DriversListModel>() {
            @Override
            public void onResponse(Call<DriversListModel> call, Response<DriversListModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    driverIdList.add("Select");
                    driverNameList.add("Select Driver");
                    if (response.body().getResponse().getDrivers().size() > 0) {
                        driverList = response.body().getResponse().getDrivers();
                        for (int i = 0; i < driverList.size(); i++) {
                            driverIdList.add(driverList.get(i).getId());
                            driverNameList.add(driverList.get(i).getDriverName());
                        }
                        aa = new ArrayAdapter(AssignJobActivity.this,
                                android.R.layout.simple_spinner_item, driverNameList);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        driverSpinner.setAdapter(aa);

                    }
                }
            }

            @Override
            public void onFailure(Call<DriversListModel> call, Throwable t) {

            }
        });
    }
}