package com.doozycod.roadsidegenius.Tabs.DriverNavigation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doozycod.roadsidegenius.Activities.Driver.DriverDashboardActivity;
import com.doozycod.roadsidegenius.Activities.JobDetailsDriverActivity;
import com.doozycod.roadsidegenius.Adapter.SelectedImagesAdapter;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.ActiveJob;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.DriverActiveJobModel;
import com.doozycod.roadsidegenius.Model.ImageListModel.ImagesListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class CurrentTaskFragment extends Fragment implements SelectedImagesAdapter.OnListListener {

    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    static ApiService apiService;
    TextView etaET, siteET, vehicleMakeEt, vehicleModelEt, vehicleColor, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET, dispatchedTime, dispatchDateTxt, driverName;
    TextView fullNameET, addMoreButton, customerEmailET, amount_quoted, contactNumberTxt, completeTimeTxt;
    TextView getLocationET, getDropOffLocation, activeJobTxt;
    Toolbar toolbar;
    private Button completeButton;
    private ActiveJob activeJob;
    ImageView contactDialogButton, imageETDialog, imageET;
    CardView layoutHide;
    Spinner paymentMethodSpinner;
    private List<String> paymentType = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    File file;
    EditText hoursET, notesET;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Calendar date;
    LinearLayout relativeLayout, hideLayout;
    String timeTaken = "";
    private int imageListSize = 0;
    SelectedImagesAdapter selectedImagesAdapter;
    RecyclerView recyclerView;

    private void initUI(View view) {

        hideLayout = view.findViewById(R.id.hideLayout);
        addMoreButton = view.findViewById(R.id.addMoreButton);
        layoutHide = view.findViewById(R.id.layoutHide);
        recyclerView = view.findViewById(R.id.recyclerView);
        activeJobTxt = view.findViewById(R.id.activeJobTxt);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        hoursET = view.findViewById(R.id.hoursMinutesET);
        paymentMethodSpinner = view.findViewById(R.id.paymentMethodSpinner);
        contactDialogButton = view.findViewById(R.id.contactDialogButton);
        completeButton = view.findViewById(R.id.completeButton);
        notesET = view.findViewById(R.id.notesET);
        amount_quoted = view.findViewById(R.id.amount_quoted);
        customerEmailET = view.findViewById(R.id.customerEmailET);
        contactNumberTxt = view.findViewById(R.id.contactNumberTxt);
        fullNameET = view.findViewById(R.id.fullNameET);
        getLocationET = view.findViewById(R.id.getPickupLocationET);
        getDropOffLocation = view.findViewById(R.id.getDropOffLocationET);
        truckET = view.findViewById(R.id.truckET);
        invoiceTotal = view.findViewById(R.id.invoiceTotal);
        total_milesET = view.findViewById(R.id.total_milesET);
        imageET = view.findViewById(R.id.imageET);
        completeTimeTxt = view.findViewById(R.id.completeTimeTxt);
        siteET = view.findViewById(R.id.siteET);
        etaET = view.findViewById(R.id.etaET);
        completeTimeTxt = view.findViewById(R.id.completeTimeTxt);
        dispatchDateTxt = view.findViewById(R.id.dispatchDateTxt);
        driverName = view.findViewById(R.id.driverSpinner);
    }

    public CurrentTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context contextThemeWrapper;
        if (sharedPreferenceMethod != null) {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(),
                    sharedPreferenceMethod.getTheme().equals("light") ? R.style.LightTheme : R.style.DarkTheme);
        } else {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.LightTheme);

        }
        // create ContextThemeWrapper from the original Activity Context with the custom theme

// clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        // Inflate the layout for this fragment
        View view = localInflater.inflate(R.layout.fragment_today, container, false);
        initUI(view);

        customProgressBar = new CustomProgressBar(getActivity());
        apiService = ApiUtils.getAPIService();

        Date today = new Date();
        String dateToStr = format.format(today);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

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
                ImagePicker.Companion.with(getActivity())
                        .compress(1024)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
            }
        });
        addMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(getActivity())
                        .compress(1024)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(101);
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

//        completeTimeTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDateTimePicker();
//            }
//        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                completeJobDialog(dateToStr);


            }
        });
//        hideLayout.setVisibility(View.GONE);
        getActiveJobAPI();
        return view;
    }


    void completeJobDialog(String dateToStr) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.complete_job_dialog);
        Button completeButton = dialog.findViewById(R.id.completeButton);
        imageETDialog = dialog.findViewById(R.id.imageETDialog);
        imageETDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(getActivity())
                        .compress(200)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(102);
            }
        });
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file == null) {
                    completedJobApi(activeJob.getJobId(), notesET.getText().toString(),
                            activeJob.getInvoiceTotal(), "Online", dateToStr, timeTaken);
                } else {
                    completedJobApiImage(activeJob.getJobId(), notesET.getText().toString(),
                            activeJob.getInvoiceTotal(), "Online", dateToStr, timeTaken, file);
                }
            }
        });
        dialog.show();
    }

    private void getActiveJobAPI() {
        customProgressBar.showProgress();
        apiService.activeJobForDriver(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<DriverActiveJobModel>() {
            @Override
            public void onResponse(Call<DriverActiveJobModel> call, Response<DriverActiveJobModel> response) {
                customProgressBar.hideProgress();
                activeJob = response.body().getResponse().getActiveJob();
                if (activeJob != null) {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                activeJobTxt.setVisibility(activeJob == null ? View.VISIBLE : View.GONE);
                if (response.body().getResponse().getStatus().equals("Success")) {
                    etaET.setText(activeJob.getEta());
                    fullNameET.setText(activeJob.getCustomerName());

                    notesET.setText(activeJob.getComments());

                    getDropOffLocation.setText(activeJob.getCustomerDropoff());

                    invoiceTotal.setText(activeJob.getInvoiceTotal());


                    contactNumberTxt.setText("+" + activeJob.getCustomerNumber());
                    getLocationET.setText(activeJob.getCustomerPickup());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String dateToStr = format.format(new Date().getTime());


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
                        timeTaken = hours + ":" + min;
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
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), DriverDashboardActivity.class));
                    getActivity().finishAffinity();
                } else {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

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
        RequestBody requestFile = RequestBody.create("", MediaType.parse("multipart/form-data"));

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
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), DriverDashboardActivity.class));
                    getActivity().finishAffinity();
                } else {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

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
            if (requestCode == 101) {

                //Image Uri will not be null for RESULT_OK
                Uri fileUri = data.getData();
//            uri = fileUri;
                //You can get File object from intent
                file = ImagePicker.Companion.getFile(data);
//                imageET.setText(file.getName());
                //You can also get File Path from intent
//                Glide.with(getActivity()).load(file).into(imageET);
                uploadImageBeforeJobCompletion(file, activeJob.getJobId());
                String filePath = ImagePicker.Companion.getFilePath(data);
                Log.e(TAG, "onActivityResult: " + filePath);
            }
            if (requestCode == 102) {

                //Image Uri will not be null for RESULT_OK
                Uri fileUri = data.getData();
//            uri = fileUri;
                //You can get File object from intent
                file = ImagePicker.Companion.getFile(data);
//                imageETDialog.setText(file.getName());
                Glide.with(getActivity()).load(file).into(imageETDialog);
                //You can also get File Path from intent
                String filePath = ImagePicker.Companion.getFilePath(data);
                Log.e(TAG, "onActivityResult: " + filePath);
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    void uploadImageBeforeJobCompletion(File fileName, String jobId) {
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

        apiService.addImageBeforeJob(jwtToken, job, image).enqueue(new Callback<ImagesListModel>() {
            @Override
            public void onResponse(Call<ImagesListModel> call, Response<ImagesListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    imageListSize = response.body().getResponse().getImages().size();
                    recyclerView.setVisibility(imageListSize > 0 ? View.VISIBLE : View.GONE);
                    layoutHide.setVisibility(imageListSize == 0 ? View.VISIBLE : View.GONE);
                    addMoreButton.setVisibility(imageListSize > 0 ? View.VISIBLE : View.GONE);
                    if (imageListSize == 5) {
                        Toast.makeText(getActivity(), "You can not add more than 5 images",
                                Toast.LENGTH_SHORT).show();
                        addMoreButton.setVisibility(View.GONE);
                    }
                }
                selectedImagesAdapter = new SelectedImagesAdapter(getActivity(),
                        response.body().getResponse().getImages(),
                        CurrentTaskFragment.this::OnSelectedSingleImage);
                recyclerView.setAdapter(selectedImagesAdapter);
            }

            @Override
            public void onFailure(Call<ImagesListModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
    public void OnSelectedSingleImage(String imageId) {
        deleteImage(activeJob.getJobId(), imageId);
        Log.e(TAG, "OnSelectedSingleImage: " + imageId);
    }

    void deleteImage(String jobId, String imageId) {
        customProgressBar.showProgress();
        apiService.deleteImages(sharedPreferenceMethod.getTokenJWT(), jobId, imageId).enqueue(new Callback<ImagesListModel>() {
            @Override
            public void onResponse(Call<ImagesListModel> call, Response<ImagesListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    imageListSize = response.body().getResponse().getImages().size();
                    recyclerView.setVisibility(imageListSize > 0 ? View.VISIBLE : View.GONE);
                    layoutHide.setVisibility(imageListSize == 0 ? View.VISIBLE : View.GONE);
                    addMoreButton.setVisibility(imageListSize > 0 ? View.VISIBLE : View.GONE);
                    if (imageListSize == 5) {
                        Toast.makeText(getActivity(), "You can not add more than 5 images",
                                Toast.LENGTH_SHORT).show();
                        addMoreButton.setVisibility(View.GONE);
                    }
                }
                selectedImagesAdapter = new SelectedImagesAdapter(getActivity(),
                        response.body().getResponse().getImages(),
                        CurrentTaskFragment.this::OnSelectedSingleImage);
                recyclerView.setAdapter(selectedImagesAdapter);
            }

            @Override
            public void onFailure(Call<ImagesListModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}