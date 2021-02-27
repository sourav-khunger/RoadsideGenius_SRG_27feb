package com.doozycod.roadsidegenius.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
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
import com.doozycod.roadsidegenius.Model.ServiceList.ServiceListModel;
import com.doozycod.roadsidegenius.Activities.PaymentActivity;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.Cars;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestServiceFragment extends Fragment {
    TextView customerName, contactNumberTxt, customerLocation;
    EditText fullNameET, customerEmailET, amount_quoted, notesET;
    AutoCompleteTextView getLocationET, getDropOffLocation;
    AutoCompleteAdapter adapter;
    AutoCompleteAdapter adapter2;
    PlacesClient placesClient;
    PlacesClient placesClient2;
    Spinner serviceTypeSpinner, vendorIDSpinner, vehicleMakeEt, vehicleModelEt,
            vehicleYearEt, vehicleColor;
    ;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    Button requestButton;
    List<Service> serviceList = new ArrayList<>();
    List<String> serviceIdList = new ArrayList<>();
    List<String> serviceTypeList = new ArrayList<>();
    Spinner paymentMethodSpinner;
    List<String> paymentType = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> aa;
    ImageView contactDialogButton;
    String countryCode = "";
    String otpString = "";
    boolean isSent = false;
    String number = "",pickupLatLong="",dropOffLatLong="";
    Toolbar toolbar;

    Cars cars;
    List<String> selectedCarModels = new ArrayList<>();
    List<String> carModelYearsList = new ArrayList<>();
    List<String> carModelColorsList = new ArrayList<>();

    public RequestServiceFragment() {
        // Required empty public constructor
    }


    private void initAutoCompleteTextView(View view) {
        paymentMethodSpinner = view.findViewById(R.id.paymentMethodSpinner);
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

    void initUI(View view) {
        vehicleColor = view.findViewById(R.id.vehicleColor);
        vehicleMakeEt = view.findViewById(R.id.vehicleMakeEt);
        vehicleYearEt = view.findViewById(R.id.vehicleYearEt);
        vehicleModelEt = view.findViewById(R.id.vehicleModelEt);
        notesET = view.findViewById(R.id.notesET);
        amount_quoted = view.findViewById(R.id.amount_quoted);
        requestButton = view.findViewById(R.id.requestButton);
        contactDialogButton = view.findViewById(R.id.contactDialogButton);
        customerEmailET = view.findViewById(R.id.customerEmailET);
        vendorIDSpinner = view.findViewById(R.id.vendorIDSpinner);
        serviceTypeSpinner = view.findViewById(R.id.serviceTypeSpinner);
        contactNumberTxt = view.findViewById(R.id.contactNumberTxt);
        fullNameET = view.findViewById(R.id.fullNameET);
        String apiKey = getString(R.string.google_maps_key);
        if (apiKey.isEmpty()) {
            Toast.makeText(getActivity(), "Api key is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }
        placesClient = Places.createClient(getActivity());
        initAutoCompleteTextView(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        View view = localInflater.inflate(R.layout.fragment_request_service, container, false);
        initUI(view);
        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());

//        serviceTypeList.add("Select Service");


        paymentType.add("Credit Card");

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, paymentType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(arrayAdapter);

        cars = new Cars();

        getServiceList();
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

        serviceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    amount_quoted.setText(serviceList.get((i - 1)).getServiceCost());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullNameET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (customerEmailET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(customerEmailET.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (contactNumberTxt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter contact number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getLocationET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter contact number", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (serviceTypeSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select a service", Toast.LENGTH_SHORT).show();
                    return;

                }
                /*if (paymentMethodSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select payment type", Toast.LENGTH_SHORT).show();
                    return;

                }*/
                else {
                    if (sharedPreferenceMethod.getLogin().equals("customer")) {
                        Intent intent = new Intent(getActivity(), PaymentActivity.class);
                        intent.putExtra("amount", amount_quoted.getText().toString());
                        intent.putExtra("name", fullNameET.getText().toString());
                        intent.putExtra("email", customerEmailET.getText().toString());
                        intent.putExtra("notes", notesET.getText().toString());
                        intent.putExtra("serviceId", serviceList.get(serviceTypeSpinner.getSelectedItemPosition() - 1).getId());
                        intent.putExtra("number", contactNumberTxt.getText().toString().replaceAll("\\+", ""));
                        intent.putExtra("getLocationET", getLocationET.getText().toString());
                        intent.putExtra("getDropOffLocation", getDropOffLocation.getText().toString());
//                        intent.putExtra("amount_quoted", amount_quoted.getText().toString());
                        startActivity(intent);

//                        createRequestCustomer(fullNameET.getText().toString(), customerEmailET.getText().toString(),
//                                contactNumberTxt.getText().toString().replaceAll("\\+", ""),
//                                getLocationET.getText().toString(), getDropOffLocation.getText().toString());
                        return;
                    } else {
                        if (vehicleMakeEt.getSelectedItemPosition() == 0) {
                            createRequest(fullNameET.getText().toString(), customerEmailET.getText().toString(),
                                    countryCode + contactNumberTxt.getText().toString(),
                                    pickupLatLong, dropOffLatLong,
                                    "",
                                    "",
                                    "",
                                    "");

                        } else {

                            createRequest(fullNameET.getText().toString(), customerEmailET.getText().toString(),
                                    countryCode + contactNumberTxt.getText().toString(),
                                    pickupLatLong, dropOffLatLong,
                                    vehicleYearEt.getSelectedView().toString(),
                                    vehicleColor.getSelectedView().toString(),
                                    vehicleMakeEt.getSelectedView().toString(),
                                    vehicleModelEt.getSelectedView().toString());

                        }
                    }
                }
            }
        });
        getCarSelected();
        return view;
    }

    void createRequest(String fullName, String email, String contactNumber, String customerPickup,
                       String customerDropOff, String vehicle_year, String vehicle_color,
                       String vehicle_make, String vehicle_model) {
        customProgressBar.showProgress();
        apiService.createJob(sharedPreferenceMethod.getTokenJWT(),
                fullName, contactNumber, customerPickup, customerDropOff,
                vehicle_make, vehicle_model, vehicle_color,
                vehicle_year, email,
                serviceList.get(serviceTypeSpinner.getSelectedItemPosition() - 1).getId(),
                notesET.getText().toString(),
                amount_quoted.getText().toString()).enqueue(new Callback<JobRequestModel>() {
            @Override
            public void onResponse(Call<JobRequestModel> call, Response<JobRequestModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JobRequestModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

//    void createRequestCustomer(String fullName, String email, String contactNumber, String customerPickup,
//                               String customerDropOff) {
//        customProgressBar.showProgress();
//        apiService.createJobRequest(sharedPreferenceMethod.getTokenJWT(), sharedPreferenceMethod.getCustomerID(),
//                fullName, contactNumber, customerPickup, customerDropOff, email,
//                serviceList.get(serviceTypeSpinner.getSelectedItemPosition() - 1).getId(),
//                notesET.getText().toString(),
//                amount_quoted.getText().toString()).enqueue(new Callback<AdminRegisterModel>() {
//            @Override
//            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
//                customProgressBar.hideProgress();
//
//                if (response.body().getResponse().getStatus().equals("Success")) {
//                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
//                customProgressBar.hideProgress();
//
//            }
//        });
//    }

    void showContactDialog() {
        final String[] enteredOtp = new String[1];
        Dialog dialog = new Dialog(getActivity());
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
                            Toast.makeText(getActivity(), "Phone number verified!", Toast.LENGTH_SHORT).show();
                            contactNumberTxt.setText("+" + countryCode + contactNumber.getText().toString());
                            dialog.dismiss();
                        }
                    }
                } else
                    Toast.makeText(getActivity(), "Enter Valid Number", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    void getServiceList() {
        customProgressBar.showProgress();
        apiService.serviceList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<ServiceListModel>() {
            @Override
            public void onResponse(Call<ServiceListModel> call, Response<ServiceListModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    serviceList = response.body().getResponse().getServices();
                    for (int i = 0; i < response.body().getResponse().getServices().size(); i++) {
                        serviceIdList.add(response.body().getResponse().getServices().get(i).getId());
                        serviceTypeList.add(response.body().getResponse().getServices().get(i).getServiceType());
                    }

                    aa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, serviceTypeList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    serviceTypeSpinner.setAdapter(aa);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ServiceListModel> call, Throwable t) {
                customProgressBar.hideProgress();
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
                            getLocationET.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                            pickupLatLong= task.getPlace().getLatLng().latitude+task.getPlace().getLatLng().longitude+"";
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
//                            mMap2.moveCamera(CameraUpdateFactory.newLatLng(task.getPlace().getLatLng()));
//                            mMap2.animateCamera(CameraUpdateFactory.zoomTo(17));
//                            mMap2.addMarker(new MarkerOptions().position(task.getPlace().getLatLng())
//                                    .title(task.getPlace().getName()));
                            Log.e("MAP SDK", "onSuccess: " + task.getPlace().getLatLng());


                            getDropOffLocation.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                            dropOffLatLong= task.getPlace().getLatLng().latitude+task.getPlace().getLatLng().longitude+"";
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

    void getCarSelected() {
        carModelYearsList = cars.getCarModelYearList();
        carModelColorsList = cars.carColorsList();
        List<String> carsList = cars.getCarBrands();


        ArrayAdapter<String> carYearAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, carModelYearsList);
        carYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleYearEt.setAdapter(carYearAdapter);

        ArrayAdapter<String> carColorAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, carModelColorsList);
        carColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleColor.setAdapter(carColorAdapter);


        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(),
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
                    ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, selectedCarModels);
                    carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vehicleModelEt.setAdapter(carModelAdapter);
                } else {
                    Log.e("TAG", "onItemSelected: " + carsList.get(i));
                    if (item.equals("Audi")) {
                        selectedCarModels = cars.audiModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("GMC")) {
                        selectedCarModels = cars.gmcModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Hyundai")) {
                        selectedCarModels = cars.hyundaiModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Honda")) {
                        selectedCarModels = cars.hondaModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Mercedes-Benz")) {
                        selectedCarModels = cars.mercedesBenzModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Nissan")) {
                        selectedCarModels = cars.nissanModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Volkswagen")) {
                        selectedCarModels = cars.volkswagenModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Subaru")) {
                        selectedCarModels = cars.subaruModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Cadillac")) {
                        selectedCarModels = cars.subaruModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Toyota")) {
                        selectedCarModels = cars.toyotaModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lamborghini")) {
                        selectedCarModels = cars.lamborghiniModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lincoln")) {
                        selectedCarModels = cars.lincolnModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Bentley")) {
                        selectedCarModels = cars.bentleyModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Chevrolet")) {
                        selectedCarModels = cars.chevroletModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ford")) {
                        selectedCarModels = cars.fordModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Kia")) {
                        selectedCarModels = cars.kiaModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Buick")) {
                        selectedCarModels = cars.buickModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Porsche")) {
                        selectedCarModels = cars.porscheModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Dodge")) {
                        selectedCarModels = cars.dodgeModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Jeep")) {
                        selectedCarModels = cars.jeepModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("MINI")) {
                        selectedCarModels = cars.miniModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Rolls-Royce")) {
                        selectedCarModels = cars.rollsRoyceModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("MAZDA")) {
                        selectedCarModels = cars.mazdaModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Aston Martin")) {
                        selectedCarModels = cars.astonMartinModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Land Rover")) {
                        selectedCarModels = cars.landRoverModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Mitsubishi")) {
                        selectedCarModels = cars.mitsubhishiModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Jaguar")) {
                        selectedCarModels = cars.jaguarModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Lexus")) {
                        selectedCarModels = cars.lexusModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("smart")) {
                        selectedCarModels = cars.smartModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Genesis")) {
                        selectedCarModels = cars.genesisModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Maserati")) {
                        selectedCarModels = cars.maseratiModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Alfa Romeo")) {
                        selectedCarModels = cars.alfaRomeoModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ferrari")) {
                        selectedCarModels = cars.ferarriModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("BMW")) {
                        selectedCarModels = cars.bmwModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Acura")) {
                        selectedCarModels = cars.acuraModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Tesla")) {
                        selectedCarModels = cars.teslaModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Chrysler")) {
                        selectedCarModels = cars.chrylerModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Ram")) {
                        selectedCarModels = cars.ramModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("INFINITI")) {
                        selectedCarModels = cars.infinitiModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Rivian")) {
                        selectedCarModels = cars.rivianModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("Volvo")) {
                        selectedCarModels = cars.volvoModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("FIAT")) {
                        selectedCarModels = cars.fiatModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, selectedCarModels);
                        carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehicleModelEt.setAdapter(carModelAdapter);
                    }
                    if (item.equals("McLaren")) {
                        selectedCarModels = cars.macLarenModels();
                        ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(getActivity(),
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