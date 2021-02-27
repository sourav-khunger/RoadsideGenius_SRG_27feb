package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Activities.Driver.DriverDashboardActivity;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.AssignedJobs.Job;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.ActiveJob;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.DriverActiveJobModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class JobDetailsDriverActivity extends AppCompatActivity {
    TextView etaET, siteET, vehicleMakeEt, vehicleModelEt, vehicleColor, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET, dispatchedTime, dispatchDateTxt, driverName;
    TextView fullNameET, customerEmailET, amount_quoted, contactNumberTxt, completeTimeTxt, imageET;
    TextView getLocationET, getDropOffLocation;
    Toolbar toolbar;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    private ApiService apiService;
    private Button completeButton;
    private ActiveJob activeJob;
    ImageView contactDialogButton;
    LinearLayout linearLayout;
    Spinner paymentMethodSpinner;
    private List<String> paymentType = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    File file;
    EditText hoursET, notesET;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        minutesET = findViewById(R.id.minutesET);
        hoursET = findViewById(R.id.hoursMinutesET);
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        contactDialogButton = findViewById(R.id.contactDialogButton);
        completeButton = findViewById(R.id.completeButton);
        notesET = findViewById(R.id.notesET);
        amount_quoted = findViewById(R.id.amount_quoted);
        customerEmailET = findViewById(R.id.customerEmailET);
        contactNumberTxt = findViewById(R.id.contactNumberTxt);
        fullNameET = findViewById(R.id.fullNameET);
        getLocationET = findViewById(R.id.getPickupLocationET);
        getDropOffLocation = findViewById(R.id.getDropOffLocationET);
//        vehicleModelEt = findViewById(R.id.vehicleModelEt);
//        descriptionET = findViewById(R.id.descriptionET);
        truckET = findViewById(R.id.truckET);
        invoiceTotal = findViewById(R.id.invoiceTotal);
        total_milesET = findViewById(R.id.total_milesET);
        imageET = findViewById(R.id.imageET);
//        vehicleColor = findViewById(R.id.vehicleColor);
//        vehicleMakeEt = findViewById(R.id.vehicleMakeEt);
        siteET = findViewById(R.id.siteET);
        etaET = findViewById(R.id.etaET);
        completeTimeTxt = findViewById(R.id.completeTimeTxt);
        dispatchDateTxt = findViewById(R.id.dispatchDateTxt);
        driverName = findViewById(R.id.driverSpinner);
    }

    Calendar date;

    String jsonString = "";


    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(JobDetailsDriverActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String dateToStr = format.format(date.getTime());
//                        Log.e("TAG", "The choosen one " + date.getTime());
                        completeTimeTxt.setText(dateToStr);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
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

        setContentView(R.layout.activity_job_details_driver);
        initUI();
        customProgressBar = new CustomProgressBar(this);
//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        apiService = ApiUtils.getAPIService();


//        paymentType.add("Select Payment Method");
//        paymentType.add("Cash");
        paymentType.add("Online");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(arrayAdapter);
//          completed Date

        Date today = new Date();
        String dateToStr = format.format(today);
        completeTimeTxt.setText(dateToStr);


        getLocationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + getLocationET.getText().toString()));
                startActivity(intent);
            }
        });
        imageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(JobDetailsDriverActivity.this)
                        .compress(1024)
                        .galleryOnly()
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
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
        completeTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (completeTimeTxt.getText().toString().equals("")) {
                    Toast.makeText(JobDetailsDriverActivity.this, "Please set complete Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hoursET.getText().toString().equals("")) {
                    Toast.makeText(JobDetailsDriverActivity.this, "Please enter full Taken time", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (hoursET.getText().toString().length() == 0) {
                    Toast.makeText(JobDetailsDriverActivity.this, "Please enter full Taken time", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (paymentMethodSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(JobDetailsDriverActivity.this, "Please select payment method type", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    if (file == null) {
                        completedJobApi(activeJob.getJobId(), notesET.getText().toString(), activeJob.getInvoiceTotal(),
                                paymentType.get(paymentMethodSpinner.getSelectedItemPosition()), completeTimeTxt.getText().toString(),
                                hoursET.getText().toString());
                    } else {
                        completedJobApiImage(activeJob.getJobId(), notesET.getText().toString(), activeJob.getInvoiceTotal(),
                                paymentType.get(paymentMethodSpinner.getSelectedItemPosition()), completeTimeTxt.getText().toString(),
                                hoursET.getText().toString(), file);

                    }
                }

            }
        });
        getActiveJobAPI();
    }

    private void getActiveJobAPI() {

        customProgressBar.showProgress();
        apiService.activeJobForDriver(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<DriverActiveJobModel>() {
            @Override
            public void onResponse(Call<DriverActiveJobModel> call, Response<DriverActiveJobModel> response) {
                customProgressBar.hideProgress();
                activeJob = response.body().getResponse().getActiveJob();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    etaET.setText(activeJob.getEta());
                    fullNameET.setText(activeJob.getCustomerName());
//                    dispatchDateTxt.setText(activeJob.getDispatchDate());
                    customerEmailET.setText(activeJob.getCustomerEmail());
//                    vehicleMakeEt.setText(activeJob.getVehicleMake());
                    notesET.setText(activeJob.getComments());
                    total_milesET.setText(activeJob.getTotalMiles());
                    getDropOffLocation.setText(activeJob.getCustomerDropoff());
                    siteET.setText(activeJob.getSite());
                    truckET.setText(activeJob.getTruck());
                    invoiceTotal.setText(activeJob.getInvoiceTotal());
                    driverName.setText(activeJob.getDriver());
                    contactNumberTxt.setText("+" + activeJob.getCustomerNumber());
                    getLocationET.setText(activeJob.getCustomerPickup());
//                    vehicleColor.setText(activeJob.getVehicleColor());
//                    vehicleModelEt.setText(activeJob.getVehicleModel());
//                    total_job_time.setText(activeJob.getTotalJobTime());
//                    dispatchedTime.setText(activeJob.getDispatched());
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//                    String simpleDate= simpleDateFormat.format(activeJob.getStartTimestamps());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String dateToStr = format.format(new Date().getTime());
//                    String dateToStr2 = simpleDateFormat.format(dateToStr);
                    Log.e(TAG, "onResponse: " + activeJob.getStartTimestamps());
                    Log.e(TAG, "onResponse: " + dateToStr);

                    Date d1 = null;
                    Date d2 = null;

                    try {
                        d1 = format.parse(activeJob.getStartTimestamps());
                        d2 = format.parse(dateToStr);

                        //in milliseconds
//                        long diff = d2.getTime() - d1.getTime();
//
//                        long diffSeconds = diff / 1000 % 60;
//                        long diffMinutes = diff / (60 * 1000) % 60;
//                        long diffHours = diff / (60 * 60 * 1000) % 24;
//                        long diffDays = diff / (24 * 60 * 60 * 1000);

                        long difference = d2.getTime() - d1.getTime();
                        int days = (int) (difference / (1000 * 60 * 60 * 24));
                        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                        hours = (hours < 0 ? -hours : hours);
                        min = (min < 0 ? -min : min);
                        Log.e("======= Hours", " :: " + hours);
                        Log.e("======= Minutes", " :: " + min);
                        hoursET.setText(hours + ":" + min);
//                        Log.e(TAG, "onResponse: "+diffDays+"  "+diffHours+"  "+diffMinutes );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverActiveJobModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                customProgressBar.hideProgress();

            }
        });
    }

    private void completedJobApiImage(String jobId, String comments, String amount, String payment,
                                      String timestamp, String timeTaken, File fileName) {
        customProgressBar.showProgress();


        File file = fileName;
        RequestBody requestFile = null;
//        if (!fileName.equals("")) {

        //pass it like this
        file = new File(fileName.getPath());
        requestFile =
                RequestBody.create(file, MediaType.parse("multipart/form-data"));
//        }

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part image =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody jwtToken =
                RequestBody.create(sharedPreferenceMethod.getTokenJWT(), MediaType.parse("multipart/form-data"));

        RequestBody job =
                RequestBody.create(jobId, MediaType.parse("multipart/form-data"));

        RequestBody status =
                RequestBody.create("Completed", MediaType.parse("multipart/form-data"));

        RequestBody comment =
                RequestBody.create(comments, MediaType.parse("multipart/form-data"));

        RequestBody amountBody =
                RequestBody.create(amount, MediaType.parse("multipart/form-data"));

        RequestBody paymentMethodBody =
                RequestBody.create(payment, MediaType.parse("multipart/form-data"));

        RequestBody timestamps =
                RequestBody.create(timestamp, MediaType.parse("multipart/form-data"));

        RequestBody time_taken =
                RequestBody.create(timeTaken, MediaType.parse("multipart/form-data"));

        apiService.completedJobDriverAPIwithIMAGE(jwtToken, job, status, comment, amountBody,
                paymentMethodBody, timestamps, time_taken, image).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(JobDetailsDriverActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(JobDetailsDriverActivity.this, DriverDashboardActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(JobDetailsDriverActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    private void completedJobApi(String jobId, String comments, String amount, String payment,
                                 String timestamp, String timeTaken) {
        customProgressBar.showProgress();
//        File file = null;
        RequestBody requestFile = null;
//        if (!fileName.equals("")) {

//        //pass it like this
//        file = new File(fileName);
        requestFile =
                RequestBody.create("", MediaType.parse("multipart/form-data"));
//        }

// MultipartBody.Part is used to send also the actual file name
//        MultipartBody.Part image =
//                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody jwtToken =
                RequestBody.create(sharedPreferenceMethod.getTokenJWT(), MediaType.parse("multipart/form-data"));

        RequestBody job =
                RequestBody.create(jobId, MediaType.parse("multipart/form-data"));

        RequestBody status =
                RequestBody.create("Completed", MediaType.parse("multipart/form-data"));

        RequestBody comment =
                RequestBody.create(comments, MediaType.parse("multipart/form-data"));

        RequestBody amountBody =
                RequestBody.create(amount, MediaType.parse("multipart/form-data"));

        RequestBody paymentMethodBody =
                RequestBody.create(payment, MediaType.parse("multipart/form-data"));

        RequestBody timestamps =
                RequestBody.create(timestamp, MediaType.parse("multipart/form-data"));

        RequestBody time_taken =
                RequestBody.create(timeTaken, MediaType.parse("multipart/form-data"));

        apiService.completedJobDriverAPI(jwtToken, job, status, comment, amountBody,
                paymentMethodBody, timestamps, time_taken).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(JobDetailsDriverActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(JobDetailsDriverActivity.this, DriverDashboardActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(JobDetailsDriverActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
//            uri = fileUri;
            //You can get File object from intent
            file = ImagePicker.Companion.getFile(data);
            imageET.setText(file.getName());
            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);
            Log.e(TAG, "onActivityResult: " + filePath);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(JobDetailsDriverActivity.this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(JobDetailsDriverActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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