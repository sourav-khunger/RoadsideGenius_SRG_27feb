package com.doozycod.roadsidegenius.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Activities.Admin.AssignJobActivity;
import com.doozycod.roadsidegenius.Adapter.AutoCompleteAdapter;
import com.doozycod.roadsidegenius.Adapter.DriversListAdapter;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.DriverList.Driver;
import com.doozycod.roadsidegenius.Model.DriverList.DriversListModel;
import com.doozycod.roadsidegenius.Model.ServiceList.Service;
import com.doozycod.roadsidegenius.Model.ServiceList.ServiceListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.Cars;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
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

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminCreateJobFragment extends Fragment {

    public AdminCreateJobFragment() {
        // Required empty public constructor
    }

    EditText siteET, total_job_time, total_milesET, invoiceTotal,
            truckET, descriptionET;
    TextView dispatchedTime, dispatchDateTxt;
    EditText fullNameET, customerEmailET, notesET, contactNumberTxt;
    Spinner serviceSpinner, driverSpinner, etaET, vehicleMakeEt, vehicleModelEt, vehicleColor, vehicleYear;
    List<String> driverIdList = new ArrayList<>();
    List<String> driverNameList = new ArrayList<>();
    List<String> etaList = new ArrayList<>();
    List<String> serviceIdList = new ArrayList<>();
    List<String> serviceNameList = new ArrayList<>();
    List<Driver> driverList = new ArrayList<>();
    List<Service> serviceList = new ArrayList<>();
    AutoCompleteTextView getLocationET, getDropOffLocation;
    AutoCompleteAdapter adapter;
    AutoCompleteAdapter adapter2;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;

    DatePickerDialog datePickerDialog;
    ImageView datePickerDialogButton;
    final Calendar myCalendar = Calendar.getInstance();
    String myFormat = "MM-dd-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    TimePickerDialog timePickerDialog, timePickerDialog2;
    RadioGroup radioGroup;
    RadioButton asapRadio, scheduledRadio;
    Button createButton;
    PlacesClient placesClient, placesClient2;
    String pickupLatLong = "", dropOffLatLong = "";

    void initUI(@NotNull View view) {
        vehicleYear = view.findViewById(R.id.vehicleYearEt);
        countryCodePicker = view.findViewById(R.id.countryCodeLogin);
        datePickerDialogButton = view.findViewById(R.id.datePickerDialogButton);
        createButton = view.findViewById(R.id.createButton);
        scheduledRadio = view.findViewById(R.id.scheduledRadio);
        asapRadio = view.findViewById(R.id.asapRadio);
        radioGroup = view.findViewById(R.id.radioGroup);
        serviceSpinner = view.findViewById(R.id.serviceSpinner);
        notesET = view.findViewById(R.id.descriptionET);
        customerEmailET = view.findViewById(R.id.customerEmailET);
        contactNumberTxt = view.findViewById(R.id.contactNumberTxt);
        fullNameET = view.findViewById(R.id.fullNameET);
        getLocationET = view.findViewById(R.id.getPickupLocationET);
        getDropOffLocation = view.findViewById(R.id.getDropOffLocationET);
        vehicleModelEt = view.findViewById(R.id.vehicleModelEt);
//        descriptionET = view.findViewById(R.id.descriptionET);
        truckET = view.findViewById(R.id.truckET);
        invoiceTotal = view.findViewById(R.id.invoiceTotal);
        total_milesET = view.findViewById(R.id.total_milesET);
        total_job_time = view.findViewById(R.id.total_job_time);
        vehicleColor = view.findViewById(R.id.vehicleColor);
        vehicleMakeEt = view.findViewById(R.id.vehicleMakeEt);
        siteET = view.findViewById(R.id.siteET);
        etaET = view.findViewById(R.id.etaET);
        dispatchedTime = view.findViewById(R.id.dispatchedTime);
        dispatchDateTxt = view.findViewById(R.id.dispatchDateTxt);
        driverSpinner = view.findViewById(R.id.driverSpinner);
    }

    CustomProgressBar customProgressBar;
    CountryCodePicker countryCodePicker;
    String countryCode = "";
    private GoogleMap mMap;
    private GoogleMap mMap2;
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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(onMapReadyCallback1());
        SupportMapFragment mapFragment2 = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment2.getMapAsync(onMapReadyCallback2());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

    }


    private void initAutoCompleteTextView(View view) {

//        getLocationET = view.findViewById(R.id.getPickupLocationET);
        getLocationET = view.findViewById(R.id.getPickupLocationET);
        getDropOffLocation = view.findViewById(R.id.getDropOffLocationET);
        getLocationET.setThreshold(1);
        getLocationET.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(getActivity(), placesClient);
        getLocationET.setAdapter(adapter);

        getDropOffLocation.setThreshold(1);
        getDropOffLocation.setOnItemClickListener(autocompleteClickListener2);
        adapter2 = new AutoCompleteAdapter(getActivity(), placesClient2);
        getDropOffLocation.setAdapter(adapter2);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        View view = localInflater.inflate(R.layout.fragment_admin_create_job, container, false);

        String apiKey = getString(R.string.google_maps_key);
        if (apiKey.isEmpty()) {
            Toast.makeText(getActivity(), "Api key is invalid!", Toast.LENGTH_SHORT).show();
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }
        placesClient = Places.createClient(getActivity());
        placesClient2 = Places.createClient(getActivity());
        cars = new Cars();
        initUI(view);
        initAutoCompleteTextView(view);
        mapEvents();

        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());

        datePickerDialog = new DatePickerDialog(getActivity());

//        radio button for select current time or scheduled a time
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (asapRadio.isChecked()) {
                    dispatchedTime.setEnabled(false);

                    dispatchDateTxt.setText(sdf.format(new Date()));
                    int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = myCalendar.get(Calendar.MINUTE);
                    dispatchedTime.setText(hour + ":" + minute);
                }
                if (scheduledRadio.isChecked()) {
                    dispatchedTime.setEnabled(true);
                }
            }
        });
//        etaET.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
//                int minute = myCalendar.get(Calendar.MINUTE);
//                timePickerDialog2 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        etaET.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);
//                timePickerDialog2.show();
//            }
//        });
        datePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        dispatchDateTxt.setText(sdf.format(new Date()));
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        dispatchedTime.setText(hour + ":" + minute);

//        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                if(driverList.get(i-1).get)
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        etaList.add("select Estimated ETA");
        etaList.add("15");
        etaList.add("30");
        etaList.add("45");
        etaList.add("60");

        ArrayAdapter aa = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, etaList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etaET.setAdapter(aa);
        Log.e("TAG", "onCreateView: " + cars.audiModels().size());
        getCarSelected();
        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    invoiceTotal.setText(serviceList.get(i - 1).getServiceCost());
                }
//                if (serviceList.get(i - 1).getServiceCost())
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //  country Code
        countryCodePicker.setAutoDetectedCountry(true);
        countryCodePicker.setCountryForNameCode("US");

        countryCode = countryCodePicker.getSelectedCountryCode();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCode();
            }
        });

        dispatchedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dispatchedTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
//          date picker for dispatch date
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


        getDrivers();
        getServiceList();

//        api call
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullNameET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter customer name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contactNumberTxt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getLocationET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter pickup location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (driverSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select driver", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select service", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etaET.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select eta", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dispatchDateTxt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please select site", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vehicleMakeEt.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select maker", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vehicleModelEt.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select model", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vehicleColor.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select color", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vehicleYear.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select year", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!asapRadio.isChecked() && !scheduledRadio.isChecked()) {
                    Toast.makeText(getActivity(), "Please select at least one service time asap or schedules", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dispatchedTime.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please select dispatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (invoiceTotal.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please invoice total", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    createAssignJobAPI(dispatchDateTxt.getText().toString(),
                            siteET.getText().toString(), etaList.get(etaET.getSelectedItemPosition()),
                            fullNameET.getText().toString(),
                            countryCode + contactNumberTxt.getText().toString(),
                            pickupLatLong, dropOffLatLong,
                            customerEmailET.getText().toString(),
                            carsList.get(vehicleMakeEt.getSelectedItemPosition()),
                            selectedCarModels.get(vehicleModelEt.getSelectedItemPosition()),
                            carModelColorsList.get(vehicleColor.getSelectedItemPosition()),
                            carModelYearsList.get(vehicleColor.getSelectedItemPosition()),
                            dispatchedTime.getText().toString(), total_job_time.getText().toString(),
                            total_milesET.getText().toString(),
                            invoiceTotal.getText().toString(),
                            notesET.getText().toString(), truckET.getText().toString());
                }
            }
        });
        return view;
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
                            pickupLatLong = task.getPlace().getLatLng().latitude + "," + task.getPlace().getLatLng().longitude;
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
                            dropOffLatLong = task.getPlace().getLatLng().latitude + "," + task.getPlace().getLatLng().longitude + "";

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


    void createAssignJobAPI(String dispatchDate, String site, String eta, String customerName,
                            String contactNumber, String pickup, String dropoff, String email,
                            String vehicleMake, String vehicleModel, String color, String year, String dispatched, String totalJobTime,
                            String totalMiles, String invoiceTotal, String comments, String truck) {


        Log.e("TAG", "createAssignJobAPI: "+vehicleMake+vehicleModel+color);
        customProgressBar.showProgress();
        apiService.createAssignJob(sharedPreferenceMethod.getTokenJWT(),
                driverIdList.get(driverSpinner.getSelectedItemPosition()),
                dispatchDate, site, eta, customerName, contactNumber, pickup, dropoff, email,
                serviceIdList.get(serviceSpinner.getSelectedItemPosition()), "Assigned", vehicleMake,
                vehicleModel, color, year, dispatched, totalJobTime, totalMiles, invoiceTotal, comments, truck).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                customProgressBar.hideProgress();

            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dispatchDateTxt.setText(sdf.format(myCalendar.getTime()));
    }

    void getServiceList() {
//        customProgressBar.showProgress();
        apiService.serviceList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<ServiceListModel>() {
            @Override
            public void onResponse(Call<ServiceListModel> call, Response<ServiceListModel> response) {
//                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    serviceIdList.add("Select");
                    serviceNameList.add("Select Service");

                    serviceList = response.body().getResponse().getServices();
                    for (int i = 0; i < response.body().getResponse().getServices().size(); i++) {
                        serviceIdList.add(response.body().getResponse().getServices().get(i).getId());
                        serviceNameList.add(response.body().getResponse().getServices().get(i).getServiceType());
                    }

                    ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, serviceNameList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    serviceSpinner.setAdapter(aa);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ServiceListModel> call, Throwable t) {
//                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());

            }
        });
    }

    void getDrivers() {
//        customProgressBar.showProgress();
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
                        ArrayAdapter aa = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, driverNameList);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        driverSpinner.setAdapter(aa);

                    }
                }
            }

            @Override
            public void onFailure(Call<DriversListModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    void getCarSelected() {
        carModelYearsList = cars.getCarModelYearList();
        carModelColorsList = cars.carColorsList();
        carsList = cars.getCarBrands();


        ArrayAdapter carYearAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, carModelYearsList);
        carYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleYear.setAdapter(carYearAdapter);

        ArrayAdapter carColorAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, carModelColorsList);
        carColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleColor.setAdapter(carColorAdapter);


        ArrayAdapter aa = new ArrayAdapter(getActivity(),
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
                    ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                            android.R.layout.simple_spinner_item, selectedCarModels);
                    carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vehicleModelEt.setAdapter(carModelAdapter);
                } else {
                    Log.e("TAG", "onItemSelected: " + carsList.get(i));
                    if (item.equals("Audi")) {
                        selectedCarModels = cars.audiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("GMC")) {
                        selectedCarModels = cars.gmcModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Hyundai")) {
                        selectedCarModels = cars.hyundaiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Honda")) {
                        selectedCarModels = cars.hondaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Mercedes-Benz")) {
                        selectedCarModels = cars.mercedesBenzModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Nissan")) {
                        selectedCarModels = cars.nissanModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Volkswagen")) {
                        selectedCarModels = cars.volkswagenModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Subaru")) {
                        selectedCarModels = cars.subaruModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Cadillac")) {
                        selectedCarModels = cars.subaruModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Toyota")) {
                        selectedCarModels = cars.toyotaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lamborghini")) {
                        selectedCarModels = cars.lamborghiniModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lincoln")) {
                        selectedCarModels = cars.lincolnModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Bentley")) {
                        selectedCarModels = cars.bentleyModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Chevrolet")) {
                        selectedCarModels = cars.chevroletModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ford")) {
                        selectedCarModels = cars.fordModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Kia")) {
                        selectedCarModels = cars.kiaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Buick")) {
                        selectedCarModels = cars.buickModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Porsche")) {
                        selectedCarModels = cars.porscheModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Dodge")) {
                        selectedCarModels = cars.dodgeModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Jeep")) {
                        selectedCarModels = cars.jeepModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("MINI")) {
                        selectedCarModels = cars.miniModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Rolls-Royce")) {
                        selectedCarModels = cars.rollsRoyceModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("MAZDA")) {
                        selectedCarModels = cars.mazdaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Aston Martin")) {
                        selectedCarModels = cars.astonMartinModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Land Rover")) {
                        selectedCarModels = cars.landRoverModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Mitsubishi")) {
                        selectedCarModels = cars.mitsubhishiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Jaguar")) {
                        selectedCarModels = cars.jaguarModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lexus")) {
                        selectedCarModels = cars.lexusModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("smart")) {
                        selectedCarModels = cars.smartModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Genesis")) {
                        selectedCarModels = cars.genesisModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Maserati")) {
                        selectedCarModels = cars.maseratiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Alfa Romeo")) {
                        selectedCarModels = cars.alfaRomeoModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ferrari")) {
                        selectedCarModels = cars.ferarriModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("BMW")) {
                        selectedCarModels = cars.bmwModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Acura")) {
                        selectedCarModels = cars.acuraModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Tesla")) {
                        selectedCarModels = cars.teslaModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Chrysler")) {
                        selectedCarModels = cars.chrylerModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ram")) {
                        selectedCarModels = cars.ramModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("INFINITI")) {
                        selectedCarModels = cars.infinitiModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Rivian")) {
                        selectedCarModels = cars.rivianModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Volvo")) {
                        selectedCarModels = cars.volvoModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("FIAT")) {
                        selectedCarModels = cars.fiatModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("McLaren")) {
                        selectedCarModels = cars.macLarenModels();
                        ArrayAdapter carModelAdapter = new ArrayAdapter(getActivity(),
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}