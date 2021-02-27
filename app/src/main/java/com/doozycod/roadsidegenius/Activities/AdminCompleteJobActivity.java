package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCompleteJobActivity extends AppCompatActivity {
    TextView etaET, siteET, vehicleMakeEt, vehicleModelEt, vehicleColor, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET, dispatchedTime, dispatchDateTxt, driverName;
    TextView fullNameET, driverNameET, amount_quoted, notesET, contactNumberTxt;
    TextView getLocationET, getDropOffLocation, paymentET;
    Toolbar toolbar;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    private ApiService apiService;
    private Button completeButton;
    private Job activeJob;
    ImageView contactDialogButton;
    LinearLayout linearLayout;
    Spinner statusSpinner;
    private List<String> statusList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    public final String TAG = "AdminCompleteJob";
    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        statusSpinner = findViewById(R.id.statusSpinner);
        contactDialogButton = findViewById(R.id.contactDialogButton);
        completeButton = findViewById(R.id.completeButton);
        notesET = findViewById(R.id.notesET);
        amount_quoted = findViewById(R.id.amount_quoted);
        driverNameET = findViewById(R.id.driverNameET);
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
        setContentView(R.layout.activity_admin_complete_job);
        initUI();
        customProgressBar = new CustomProgressBar(this);
//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        apiService = ApiUtils.getAPIService();

        statusList.add("Completed");
        statusList.add("Gone_On_Arrival");
        statusList.add("Cancelled");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(arrayAdapter);


        if (getIntent().getExtras().getSerializable("job") != null) {
            activeJob = (Job) getIntent().getExtras().getSerializable("job");
            etaET.setText(activeJob.getEta());
            fullNameET.setText(activeJob.getCustomerName());
            dispatchDateTxt.setText(activeJob.getDispatchDate());
            driverNameET.setText(activeJob.getDriver());
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
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = statusSpinner.getSelectedItemPosition();
                if (statusSpinner.getSelectedItemPosition() == 0) {
                    completeJobByAdmin("Online", status);
                } else {
                    completeJobByAdmin("", status);

                }
            }
        });
    }

    private void completeJobByAdmin(String payment, int status) {
        apiService.completedJobAdmin(sharedPreferenceMethod.getTokenJWT(),
                activeJob.getJobId(), statusList.get(status), payment).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(AdminCompleteJobActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminCompleteJobActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

}