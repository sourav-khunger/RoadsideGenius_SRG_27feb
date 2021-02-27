package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.doozycod.roadsidegenius.Model.AssignedJobs.Job;
import com.doozycod.roadsidegenius.Model.PaymentList.Payment;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

public class PaymentDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView etaET, siteET, vehicleMakeEt, vehicleModelEt, vehicleColor, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET, dispatchedTime, dispatchDateTxt, driverName;
    TextView fullNameET, customerEmailET, amount_quoted, notesET, contactNumberTxt;
    TextView getLocationET, getDropOffLocation, bonusET, paymentET, currencyET, statusET, paymentMethodET;

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        paymentMethodET = findViewById(R.id.paymentMethodET);
        statusET = findViewById(R.id.statusET);
        currencyET = findViewById(R.id.currencyET);
        paymentET = findViewById(R.id.paymentET);
        bonusET = findViewById(R.id.bonusET);
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

    Payment payment;
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
        setContentView(R.layout.activity_paymetn_details);
//        init
        initUI();
        payment = (Payment) getIntent().getExtras().getSerializable("payment");

        setData(payment);
    }

    void setData(Payment payment) {
        paymentMethodET.setText(payment.getPaymentMethod());
        statusET.setText(payment.getStatus());
        currencyET.setText(payment.getCurrency());
        paymentET.setText(payment.getAmount());
        bonusET.setText(payment.getBonusAmount());
        etaET.setText(payment.getEta());
        fullNameET.setText(payment.getCustomerName());
        dispatchDateTxt.setText(payment.getDispatchDate());
        customerEmailET.setText(payment.getCustomerEmail());
        vehicleMakeEt.setText(payment.getVehicleMake());
        notesET.setText(payment.getComments());
        total_milesET.setText(payment.getTotalMiles());
        getDropOffLocation.setText(payment.getCustomerDropoff());
        siteET.setText(payment.getSite());
        truckET.setText(payment.getTruck());
        invoiceTotal.setText(payment.getInvoiceTotal());
        driverName.setText(payment.getDriver());
        contactNumberTxt.setText("+" + payment.getCustomerNumber());
        getLocationET.setText(payment.getCustomerPickup());
        vehicleColor.setText(payment.getVehicleColor());
        vehicleModelEt.setText(payment.getVehicleModel());
        total_job_time.setText(payment.getTotalJobTime());
        dispatchedTime.setText(payment.getDispatched());
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