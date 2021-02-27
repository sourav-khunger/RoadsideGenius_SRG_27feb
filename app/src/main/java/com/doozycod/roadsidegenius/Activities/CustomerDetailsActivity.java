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

import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.AssignedJobs.Job;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.ActiveJob;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.DriverActiveJobModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class CustomerDetailsActivity extends AppCompatActivity {
    TextView etaET, siteET, vehicleMakeEt, vehicleModelEt, vehicleColor, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET, dispatchedTime, dispatchDateTxt, driverName;
    TextView fullNameET, customerEmailET, amount_quoted, notesET, contactNumberTxt;
    TextView getLocationET, getDropOffLocation,paymentET;
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
        amount_quoted = findViewById(R.id.amount_quoted);
        customerEmailET = findViewById(R.id.customerEmailET);
        contactNumberTxt = findViewById(R.id.contactNumberTxt);
        fullNameET = findViewById(R.id.fullNameET);
        getLocationET = findViewById(R.id.getPickupLocationET);
        getDropOffLocation = findViewById(R.id.getDropOffLocationET);
        vehicleModelEt = findViewById(R.id.vehicleModelEt);
        descriptionET = findViewById(R.id.descriptionET);
        truckET = findViewById(R.id.truckET);
        invoiceTotal = findViewById(R.id.invoiceTotal);
        total_milesET = findViewById(R.id.total_milesET);
        total_job_time = findViewById(R.id.total_job_time);
        vehicleColor = findViewById(R.id.vehicleColor);
        vehicleMakeEt = findViewById(R.id.vehicleMakeEt);
        siteET = findViewById(R.id.siteET);
        etaET = findViewById(R.id.etaET);
        dispatchedTime = findViewById(R.id.dispatchedTime);
        dispatchDateTxt = findViewById(R.id.dispatchDateTxt);
        driverName = findViewById(R.id.driverSpinner);
    }

    String jsonString = "";

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
        setContentView(R.layout.activity_customer_details);
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

        if (getIntent().hasExtra("tasks")) {
            completeButton.setVisibility(View.VISIBLE);
            getActiveJobAPI();
        } else {
            jsonString = getIntent().getStringExtra("jsonObject");
            Log.e("TAG", "onCreate: " + jsonString + " //1");

        }

        if (getIntent().getExtras().getSerializable("driver") != null) {
            activeJob = (Job) getIntent().getExtras().getSerializable("driver");
            etaET.setText(activeJob.getEta());
            fullNameET.setText(activeJob.getCustomerName());
            dispatchDateTxt.setText(activeJob.getDispatchDate());
            customerEmailET.setText(activeJob.getCustomerEmail());
            vehicleMakeEt.setText(activeJob.getVehicleMake());
            notesET.setText(activeJob.getComments());
            total_milesET.setText(activeJob.getTotalMiles());
            getDropOffLocation.setText(activeJob.getCustomerDropoff());
            siteET.setText(activeJob.getSite());
            truckET.setText(activeJob.getTruck());
            invoiceTotal.setText(activeJob.getInvoiceTotal());
            driverName.setText(activeJob.getDriver());
            contactNumberTxt.setText("+" + activeJob.getCustomerNumber());
            getLocationET.setText(activeJob.getCustomerPickup());
            vehicleColor.setText(activeJob.getVehicleColor());
            vehicleModelEt.setText(activeJob.getVehicleModel());
            total_job_time.setText(activeJob.getTotalJobTime());
            dispatchedTime.setText(activeJob.getDispatched());

        }
        if (getIntent().hasExtra("jsonObject")) {
            if (getIntent() != null) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
//            amount_quoted.setText(jsonObject.getString("amount_quoted"));
                    etaET.setText(jsonObject.getString("eta"));
                    fullNameET.setText(jsonObject.getString("customer_name"));
                    dispatchDateTxt.setText(jsonObject.getString("dispatch_date"));
                    customerEmailET.setText(jsonObject.getString("customer_email"));
                    vehicleMakeEt.setText(jsonObject.getString("vehicle_make"));
                    notesET.setText(jsonObject.getString("comments"));
                    total_milesET.setText(jsonObject.getString("total_miles"));
                    getDropOffLocation.setText(jsonObject.getString("customer_dropoff"));
                    siteET.setText(jsonObject.getString("site"));
                    truckET.setText(jsonObject.getString("truck"));
                    invoiceTotal.setText(jsonObject.getString("invoice_total"));
                    driverName.setText(jsonObject.getString("driver_name"));
                    contactNumberTxt.setText("+" + jsonObject.getLong("customer_number"));
                    getLocationET.setText(jsonObject.getString("customer_pickup"));
                    vehicleColor.setText(jsonObject.getString("vehicle_color"));
                    vehicleModelEt.setText(jsonObject.getString("vehicle_model"));
                    total_job_time.setText(jsonObject.getString("total_job_time"));
                    dispatchedTime.setText(jsonObject.getString("dipatch"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        if (getIntent().hasExtra("customer_name")) {

            etaET.setText(getIntent().getStringExtra("eta"));
            fullNameET.setText(getIntent().getStringExtra("customer_name"));
            dispatchDateTxt.setText(getIntent().getStringExtra("dispatch_date"));
            customerEmailET.setText(getIntent().getStringExtra("customer_email"));
            vehicleMakeEt.setText(getIntent().getStringExtra("vehicle_make"));
            notesET.setText(getIntent().getStringExtra("comments"));
            total_milesET.setText(getIntent().getStringExtra("total_miles"));
            getDropOffLocation.setText(getIntent().getStringExtra("customer_dropoff"));
            siteET.setText(getIntent().getStringExtra("site"));
            truckET.setText(getIntent().getStringExtra("truck"));
            invoiceTotal.setText(getIntent().getStringExtra("invoice_total"));
            driverName.setText(getIntent().getStringExtra("driver_name"));
            contactNumberTxt.setText("+" + getIntent().getStringExtra("customer_number"));
            getLocationET.setText(getIntent().getStringExtra("customer_pickup"));
            vehicleColor.setText(getIntent().getStringExtra("vehicle_color"));
            vehicleModelEt.setText(getIntent().getStringExtra("vehicle_model"));
            total_job_time.setText(getIntent().getStringExtra("total_job_time"));
            dispatchedTime.setText(getIntent().getStringExtra("dipatch"));


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


    private void getActiveJobAPI() {

        customProgressBar.showProgress();
        apiService.activeJobForDriver(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<DriverActiveJobModel>() {
            @Override
            public void onResponse(Call<DriverActiveJobModel> call, Response<DriverActiveJobModel> response) {
                customProgressBar.hideProgress();
                ActiveJob activeJob = response.body().getResponse().getActiveJob();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    etaET.setText(activeJob.getEta());
                    fullNameET.setText(activeJob.getCustomerName());
                    dispatchDateTxt.setText(activeJob.getDispatchDate());
                    customerEmailET.setText(activeJob.getCustomerEmail());
                    vehicleMakeEt.setText(activeJob.getVehicleMake());
                    notesET.setText(activeJob.getComments());
                    total_milesET.setText(activeJob.getTotalMiles());
                    getDropOffLocation.setText(activeJob.getCustomerDropoff());
                    siteET.setText(activeJob.getSite());
                    truckET.setText(activeJob.getTruck());
                    invoiceTotal.setText(activeJob.getInvoiceTotal());
                    driverName.setText(activeJob.getDriver());
                    contactNumberTxt.setText("+" + activeJob.getCustomerNumber());
                    getLocationET.setText(activeJob.getCustomerPickup());
                    vehicleColor.setText(activeJob.getVehicleColor());
                    vehicleModelEt.setText(activeJob.getVehicleModel());
                    total_job_time.setText(activeJob.getTotalJobTime());
                    dispatchedTime.setText(activeJob.getDispatched());


                }
            }

            @Override
            public void onFailure(Call<DriverActiveJobModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                customProgressBar.hideProgress();

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