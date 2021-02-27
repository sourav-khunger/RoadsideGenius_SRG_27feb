package com.doozycod.roadsidegenius.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Adapter.AutoCompleteAdapter;
import com.doozycod.roadsidegenius.Model.AddCustomerNumberModel.VerifyOTPModel;
import com.doozycod.roadsidegenius.Model.JobRequestModel.JobRequestModel;
import com.doozycod.roadsidegenius.Model.ServiceList.Service;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.Cars;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends AppCompatActivity {

    TextView customerName, contactNumberTxt, customerLocation;
    EditText fullNameET, customerEmailET, amount_quoted, notesET;
    AutoCompleteTextView getLocationET, getDropOffLocation;
    AutoCompleteAdapter adapter;
    AutoCompleteAdapter adapter2;
    PlacesClient placesClient, placesClient2;
    TextView serviceTypeSpinner;
    //    vendorIDSpinner;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    Button requestButton;
    Service serviceList;
    TextView paymentMethodSpinner;
    List<String> paymentType = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ArrayAdapter aa;
    ImageView contactDialogButton;
    String countryCode = "";
    String otpString = "",pickupLatLong="",dropOffLatLong="";
    boolean isSent = false;
    String number = "";
    Toolbar toolbar;
    private GoogleMap mMap;
    private GoogleMap mMap2;
    Spinner vehicleMakeEt, vehicleModelEt, vehicleYearEt, vehicleColor;
    Cars cars;
    List<String> selectedCarModels = new ArrayList<>();
    List<String> carModelYearsList = new ArrayList<>();
    List<String> carModelColorsList = new ArrayList<>();
    List<String> carsList = new ArrayList<>();

    public OnMapReadyCallback onMapReadyCallback1() {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng vannes = new LatLng(47.66, -2.75);
                mMap.addMarker(new MarkerOptions().position(vannes).title("Vannes"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(vannes));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
            }
        };
    }

    public OnMapReadyCallback onMapReadyCallback2() {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap2 = googleMap;
                LatLng bordeaux = new LatLng(44.833328, -0.56667);
                mMap2.addMarker(new MarkerOptions().position(bordeaux).title("Bordeaux"));
                mMap2.moveCamera(CameraUpdateFactory.newLatLng(bordeaux));
                mMap2.animateCamera(CameraUpdateFactory.zoomTo(9));

            }
        };
    }

    void mapEvents() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(onMapReadyCallback1());
        SupportMapFragment mapFragment2 = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment2.getMapAsync(onMapReadyCallback2());
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
        setContentView(R.layout.activity_request);
        apiService = ApiUtils.getAPIService();
//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        serviceList = (Service) getIntent().getExtras().getSerializable("service");

        initUI();
        Dexter.withContext(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        });
        mapEvents();
        String apiKey = getString(R.string.google_maps_key);
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Api key is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        placesClient = Places.createClient(this);
        placesClient2 = Places.createClient(this);
        cars = new Cars();

//        serviceTypeList.add("Select Service");


        paymentType.add("Credit Card");

        initAutoCompleteTextView();
        getCarSelected();
    }

    private void initAutoCompleteTextView() {
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        getLocationET = findViewById(R.id.getPickupLocationET);
        getDropOffLocation = findViewById(R.id.getDropOffLocationET);
        getLocationET.setThreshold(1);
        getLocationET.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        getLocationET.setAdapter(adapter);

        getDropOffLocation.setThreshold(1);
        getDropOffLocation.setOnItemClickListener(autocompleteClickListener2);
        adapter2 = new AutoCompleteAdapter(this, placesClient2);
        getDropOffLocation.setAdapter(adapter2);
    }

    void initUI() {
        vehicleColor = findViewById(R.id.vehicleColor);
        vehicleMakeEt = findViewById(R.id.vehicleMakeEt);
        vehicleYearEt = findViewById(R.id.vehicleYearEt);
        vehicleModelEt = findViewById(R.id.vehicleModelEt);
        notesET = findViewById(R.id.notesET);
        amount_quoted = findViewById(R.id.amount_quoted);
        requestButton = findViewById(R.id.requestButton);
        contactDialogButton = findViewById(R.id.contactDialogButton);
        customerEmailET = findViewById(R.id.customerEmailET);
//        vendorIDSpinner = findViewById(R.id.vendorIDSpinner);
        serviceTypeSpinner = findViewById(R.id.serviceTypeSpinner);
        contactNumberTxt = findViewById(R.id.contactNumberTxt);
        fullNameET = findViewById(R.id.fullNameET);
        String apiKey = getString(R.string.google_maps_key);
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Api key is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey);
        }
        placesClient = Places.createClient(this);
        initAutoCompleteTextView();

        contactDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog();
            }
        });
        if (!sharedPreferenceMethod.getCustomerContact().equals("")) {
            contactNumberTxt.setText("+" + sharedPreferenceMethod.getCustomerContact());
        }
        number = sharedPreferenceMethod.getCustomerContact();


        serviceTypeSpinner.setText(serviceList.getServiceType());
        amount_quoted.setText(serviceList.getServiceCost());
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullNameET.getText().toString().equals("")) {
                    Toast.makeText(RequestActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (customerEmailET.getText().toString().equals("")) {
                    Toast.makeText(RequestActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(customerEmailET.getText().toString())) {
                    Toast.makeText(RequestActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (contactNumberTxt.getText().toString().equals("")) {
                    Toast.makeText(RequestActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getLocationET.getText().toString().equals("")) {
                    Toast.makeText(RequestActivity.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
                    return;

                }

                /*if (paymentMethodSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(RequestActivity.this, "Please select payment type", Toast.LENGTH_SHORT).show();
                    return;

                }*/
                else {
                    if (sharedPreferenceMethod.getLogin().equals("customer")) {
                        Intent intent = new Intent(RequestActivity.this, PaymentActivity.class);
                        intent.putExtra("amount", amount_quoted.getText().toString());
                        intent.putExtra("name", fullNameET.getText().toString());
                        intent.putExtra("email", customerEmailET.getText().toString());
                        intent.putExtra("notes", notesET.getText().toString());
                        intent.putExtra("serviceId", serviceList.getId());
                        intent.putExtra("number", contactNumberTxt.getText().toString().replaceAll("\\+", ""));
                        intent.putExtra("getLocationET", pickupLatLong);
                        intent.putExtra("getDropOffLocation", dropOffLatLong);
                        if (vehicleMakeEt.getSelectedItemPosition() == 0) {
                            intent.putExtra("vehicleMake", "NA");
                            intent.putExtra("model", "NA");
                            intent.putExtra("color", "NA");
                            intent.putExtra("year", "NA");

                        } else {
                            intent.putExtra("vehicleMake", carsList.get(vehicleMakeEt.getSelectedItemPosition()));
                            intent.putExtra("model", selectedCarModels.get(vehicleModelEt.getSelectedItemPosition()));
                            intent.putExtra("color", carModelColorsList.get(vehicleColor.getSelectedItemPosition()));
                            intent.putExtra("year", carModelYearsList.get(vehicleYearEt.getSelectedItemPosition()));
                        }

                        startActivity(intent);

//                        createRequestCustomer(fullNameET.getText().toString(), customerEmailET.getText().toString(),
//                                contactNumberTxt.getText().toString().replaceAll("\\+", ""),
//                                getLocationET.getText().toString(), getDropOffLocation.getText().toString());
                        return;
                    } else {
                        if (vehicleMakeEt.getSelectedItemPosition() == 0) {
                            createRequest(fullNameET.getText().toString(), customerEmailET.getText().toString(),
                                    contactNumberTxt.getText().toString().replaceAll("\\+", ""),
                                    "", "", "", "",
                                    pickupLatLong,dropOffLatLong);
                        } else {
                            createRequest(fullNameET.getText().toString(), customerEmailET.getText().toString(),
                                    contactNumberTxt.getText().toString().replaceAll("\\+", ""),
                                    carsList.get(vehicleMakeEt.getSelectedItemPosition()),
                                    selectedCarModels.get(vehicleModelEt.getSelectedItemPosition()),
                                    carModelColorsList.get(vehicleColor.getSelectedItemPosition()),
                                    carModelYearsList.get(vehicleColor.getSelectedItemPosition()),
                                    pickupLatLong,dropOffLatLong);

                        }

                    }
                }
            }
        });
    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(task.getPlace().getLatLng()));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                            mMap.addMarker(new MarkerOptions().position(task.getPlace().getLatLng())
                                    .title(task.getPlace().getName()));
                            Log.e("MAP SDK", "onSuccess: " + task.getPlace().getLatLng());
                            getLocationET.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                            pickupLatLong= task.getPlace().getLatLng().latitude+","+task.getPlace().getLatLng().longitude+"";

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            getLocationET.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private AdapterView.OnItemClickListener autocompleteClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter2.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient2.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            mMap2.moveCamera(CameraUpdateFactory.newLatLng(task.getPlace().getLatLng()));
                            mMap2.animateCamera(CameraUpdateFactory.zoomTo(17));
                            mMap2.addMarker(new MarkerOptions().position(task.getPlace().getLatLng())
                                    .title(task.getPlace().getName()));
                            Log.e("MAP SDK", "onSuccess: " + task.getPlace().getLatLng());
                            getDropOffLocation.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                            dropOffLatLong= task.getPlace().getLatLng().latitude+","+task.getPlace().getLatLng().longitude+"";

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            getDropOffLocation.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    void createRequest(String fullName, String email, String contactNumber, String vehicle_year, String vehicle_color,
                       String vehicle_make, String vehicle_model, String customerPickup,
                       String customerDropOff) {
        customProgressBar.showProgress();
        apiService.createJob(sharedPreferenceMethod.getTokenJWT(),
                fullName, contactNumber, customerPickup, customerDropOff, vehicle_make, vehicle_model, vehicle_color,
                vehicle_year, email,
                serviceList.getId(),
                notesET.getText().toString(),
                amount_quoted.getText().toString()).enqueue(new Callback<JobRequestModel>() {
            @Override
            public void onResponse(Call<JobRequestModel> call, Response<JobRequestModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(RequestActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RequestActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JobRequestModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    void showContactDialog() {
        final String[] enteredOtp = new String[1];
        Dialog dialog = new Dialog(RequestActivity.this);
//        dialog.setCancelable(false);
        dialog.setContentView(R.layout.contact_verify_dialog);
        EditText contactNumber = dialog.findViewById(R.id.customerNumberET);
        Button sendOtpButton = dialog.findViewById(R.id.sendOtpButton);
        CountryCodePicker countryCodePicker = dialog.findViewById(R.id.countryCodePicker);
        OtpView otp_view = dialog.findViewById(R.id.otp_view);
        LinearLayout showOTP = dialog.findViewById(R.id.showOTP);
        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                enteredOtp[0] = otp;

            }
        });
        isSent = false;
        countryCodePicker.setAutoDetectedCountry(true);
        countryCodePicker.setCountryForNameCode("US");

        countryCode = countryCodePicker.getSelectedCountryCode();
//        countryCode = countryCodePicker.getDefaultCountryCode();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCode();
            }
        });
        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contactNumber.getText().toString().length() > 8) {

                    if (!isSent) {
                        getOTP(countryCode + contactNumber.getText().toString(), sendOtpButton);
                        showOTP.setVisibility(View.VISIBLE);
                    } else {
                        if (enteredOtp[0].equals(otpString)) {
                            Toast.makeText(RequestActivity.this, "Phone number verified!", Toast.LENGTH_SHORT).show();
                            contactNumberTxt.setText("+" + countryCode + contactNumber.getText().toString());
                            dialog.dismiss();
                        }
                    }
                } else
                    Toast.makeText(RequestActivity.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();

            }
        });
        dialog.show();
    }

    void getOTP(String phNumber, Button sendBTN) {
        customProgressBar.showProgress();
        apiService.addCustomerNumber(sharedPreferenceMethod.getTokenJWT(), phNumber).enqueue(new Callback<VerifyOTPModel>() {
            @Override
            public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    isSent = true;
                    sendBTN.setText("Submit");
                    number = phNumber;
                    otpString = String.valueOf(response.body().getResponse().getOtp());
                    Toast.makeText(RequestActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RequestActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
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

    void getCarSelected() {
        carModelYearsList = cars.getCarModelYearList();
        carModelColorsList = cars.carColorsList();
        carsList = cars.getCarBrands();


        ArrayAdapter carYearAdapter = new ArrayAdapter(RequestActivity.this,
                android.R.layout.simple_spinner_item, carModelYearsList);
        carYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleYearEt.setAdapter(carYearAdapter);

        ArrayAdapter carColorAdapter = new ArrayAdapter(RequestActivity.this,
                android.R.layout.simple_spinner_item, carModelColorsList);
        carColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleColor.setAdapter(carColorAdapter);


        ArrayAdapter aa = new ArrayAdapter(RequestActivity.this,
                android.R.layout.simple_spinner_item, carsList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        vehicleModelEt.setAdapter(carModelAdapter);

        vehicleMakeEt.setAdapter(aa);
        vehicleMakeEt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString().trim();
                if (i == 0) {
                    selectedCarModels = new ArrayList<>();
                    ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                            android.R.layout.simple_spinner_item, selectedCarModels);
                    carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vehicleModelEt.setAdapter(carModelAdapter);
                } else {
                    Log.e("TAG", "onItemSelected: " + carsList.get(i));
                    if (item.equals("Audi")) {
                        selectedCarModels = cars.audiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("GMC")) {
                        selectedCarModels = cars.gmcModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Hyundai")) {
                        selectedCarModels = cars.hyundaiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Honda")) {
                        selectedCarModels = cars.hondaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Mercedes-Benz")) {
                        selectedCarModels = cars.mercedesBenzModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Nissan")) {
                        selectedCarModels = cars.nissanModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Volkswagen")) {
                        selectedCarModels = cars.volkswagenModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Subaru")) {
                        selectedCarModels = cars.subaruModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Cadillac")) {
                        selectedCarModels = cars.subaruModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Toyota")) {
                        selectedCarModels = cars.toyotaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lamborghini")) {
                        selectedCarModels = cars.lamborghiniModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lincoln")) {
                        selectedCarModels = cars.lincolnModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Bentley")) {
                        selectedCarModels = cars.bentleyModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Chevrolet")) {
                        selectedCarModels = cars.chevroletModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ford")) {
                        selectedCarModels = cars.fordModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Kia")) {
                        selectedCarModels = cars.kiaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Buick")) {
                        selectedCarModels = cars.buickModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Porsche")) {
                        selectedCarModels = cars.porscheModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Dodge")) {
                        selectedCarModels = cars.dodgeModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Jeep")) {
                        selectedCarModels = cars.jeepModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("MINI")) {
                        selectedCarModels = cars.miniModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Rolls-Royce")) {
                        selectedCarModels = cars.rollsRoyceModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("MAZDA")) {
                        selectedCarModels = cars.mazdaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Aston Martin")) {
                        selectedCarModels = cars.astonMartinModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Land Rover")) {
                        selectedCarModels = cars.landRoverModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Mitsubishi")) {
                        selectedCarModels = cars.mitsubhishiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Jaguar")) {
                        selectedCarModels = cars.jaguarModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lexus")) {
                        selectedCarModels = cars.lexusModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("smart")) {
                        selectedCarModels = cars.smartModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Genesis")) {
                        selectedCarModels = cars.genesisModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Maserati")) {
                        selectedCarModels = cars.maseratiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Alfa Romeo")) {
                        selectedCarModels = cars.alfaRomeoModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ferrari")) {
                        selectedCarModels = cars.ferarriModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("BMW")) {
                        selectedCarModels = cars.bmwModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Acura")) {
                        selectedCarModels = cars.acuraModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Tesla")) {
                        selectedCarModels = cars.teslaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Chrysler")) {
                        selectedCarModels = cars.chrylerModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ram")) {
                        selectedCarModels = cars.ramModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("INFINITI")) {
                        selectedCarModels = cars.infinitiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Rivian")) {
                        selectedCarModels = cars.rivianModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Volvo")) {
                        selectedCarModels = cars.volvoModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("FIAT")) {
                        selectedCarModels = cars.fiatModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("McLaren")) {
                        selectedCarModels = cars.macLarenModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(RequestActivity.this,
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
