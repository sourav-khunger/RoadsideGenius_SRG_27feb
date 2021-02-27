package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.doozycod.roadsidegenius.Model.JobList.Job;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewRequestDetailsActivity extends AppCompatActivity {
    TextView etaET, siteET, vehicleMakeEt, vehicleModelEt, vehicleColor, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET, dispatchedTime, dispatchDateTxt, driverName;
    TextView fullNameET, customerEmailET, amount_quoted, notesET, contactNumberTxt;
    TextView getLocationET, getDropOffLocation, paymentET;
    Toolbar toolbar;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    private ApiService apiService;
    private Button completeButton;
    private Job activeJob;
    ImageView contactDialogButton;
    LinearLayout linearLayout;
    Spinner paymentMethodSpinner;
    private List<String> paymentType = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        contactDialogButton = findViewById(R.id.contactDialogButton);
        completeButton = findViewById(R.id.completeButton);
        notesET = findViewById(R.id.notesET);
//        amount_quoted = findViewById(R.id.invoiceTotal);
        customerEmailET = findViewById(R.id.customerEmailET);
        contactNumberTxt = findViewById(R.id.contactNumberTxt);
        fullNameET = findViewById(R.id.fullNameET);
        getLocationET = findViewById(R.id.getPickupLocationET);
        getDropOffLocation = findViewById(R.id.getDropOffLocationET);
        vehicleModelEt = findViewById(R.id.vehicleModelEt);
        descriptionET = findViewById(R.id.descriptionET);
        invoiceTotal = findViewById(R.id.invoiceTotal);
        total_milesET = findViewById(R.id.total_milesET);
        total_job_time = findViewById(R.id.total_job_time);
        vehicleColor = findViewById(R.id.vehicleColor);
        vehicleMakeEt = findViewById(R.id.vehicleMakeEt);
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
        setContentView(R.layout.activity_new_request_details2);
        initUI();
        customProgressBar = new CustomProgressBar(this);
//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        apiService = ApiUtils.getAPIService();


//        paymentType.add("Select Payment Method");
//        paymentType.add("Cash");
//        paymentType.add("Credit Card");
//
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentType);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        paymentMethodSpinner.setAdapter(arrayAdapter);


        if (getIntent().getExtras().getSerializable("driver") != null) {
            activeJob = (Job) getIntent().getExtras().getSerializable("driver");
            fullNameET.setText(activeJob.getCustomerName());
            customerEmailET.setText(activeJob.getCustomerEmail());
            getDropOffLocation.setText(activeJob.getCustomerDropoff());
            contactNumberTxt.setText("+" + activeJob.getCustomerNumber());
            invoiceTotal.setText("$" + activeJob.getAmountQuoted());
            vehicleColor.setText( activeJob.getCustVehicleColor());
            vehicleModelEt.setText( activeJob.getCustVehicleModel());
            vehicleMakeEt.setText( activeJob.getCustVehicleMake());
            invoiceTotal.setText( activeJob.getAmountQuoted());
            getLocationET.setText(activeJob.getCustomerPickup());

        }

        getLocationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + getLocationET.getText().toString()));
                startActivity(intent);
            }
        });
        getDropOffLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + getDropOffLocation.getText().toString()));
                startActivity(intent);
            }
        });
        contactDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + contactNumberTxt.getText().toString());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
    }
}