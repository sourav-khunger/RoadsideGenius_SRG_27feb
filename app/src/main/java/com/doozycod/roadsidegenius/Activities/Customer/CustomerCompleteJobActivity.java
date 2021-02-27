package com.doozycod.roadsidegenius.Activities.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.JobList.Active;
import com.doozycod.roadsidegenius.Activities.PaymentActivity;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

public class CustomerCompleteJobActivity extends AppCompatActivity {
    Spinner paymentMethodSpinner;
    List<String> paymentType = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    Active activeJob;
    TextView fullNameET, customerEmailET, contactNumberTxt, getPickupLocationET, getDropOffLocationET,
            driverName, invoiceTotalTxt;
    Button payButton;
    SharedPreferenceMethod sharedPreferenceMethod;

    void initUI() {
        payButton = findViewById(R.id.payButton);
        invoiceTotalTxt = findViewById(R.id.invoiceTotalTxt);
        driverName = findViewById(R.id.driverSpinner);
        getDropOffLocationET = findViewById(R.id.getDropOffLocationET);
        getPickupLocationET = findViewById(R.id.getPickupLocationET);
        contactNumberTxt = findViewById(R.id.contactNumberTxt);
        customerEmailET = findViewById(R.id.customerEmailET);
        fullNameET = findViewById(R.id.fullNameET);
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);

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
        setContentView(R.layout.activity_customer_complete_job);

        initUI();

        if (getIntent() != null) {
            activeJob = (Active) getIntent().getExtras().getSerializable("customer");
            invoiceTotalTxt.setText(activeJob.getInvoiceTotal());
            driverName.setText(activeJob.getDriver());
            getDropOffLocationET.setText(activeJob.getCustomerDropoff());
            getPickupLocationET.setText(activeJob.getCustomerPickup());
            contactNumberTxt.setText(activeJob.getCustomerNumber());
            customerEmailET.setText(activeJob.getCustomerEmail());
            fullNameET.setText(activeJob.getCustomerName());
        }
        paymentType.add("Select Payment Method");
        paymentType.add("Cash");
        paymentType.add("Credit Card");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(arrayAdapter);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentMethodSpinner.getSelectedItemPosition() == 0) {

                    Toast.makeText(CustomerCompleteJobActivity.this,
                            "Select Payment Method", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    Intent intent = new Intent(CustomerCompleteJobActivity.this, PaymentActivity.class);
                    intent.putExtra("amount", activeJob.getInvoiceTotal());
                    intent.putExtra("payment", paymentType.get(paymentMethodSpinner.getSelectedItemPosition()));
                    intent.putExtra("job_id", activeJob.getJobId());
                    startActivity(intent);
                }
            }
        });
    }
}