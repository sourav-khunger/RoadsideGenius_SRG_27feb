package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.DriverList.Driver;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.Company.Company;
import com.doozycod.roadsidegenius.Model.Company.CompanyModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddDriverFragment extends Fragment {

    private static final String EXTRA_TEXT = "text";
    EditText nameET, emailET, numberET, driverET, driverAddressET, driverZipcodeET, payperJobET, serviceAreaET, serviceTypeET,
            serviceModelET, serviceMakeET;
    Spinner vendorIDSpinner, serviceYearET;
    Button addDriverButton;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    //    HashMap<String, String> hashMap = new HashMap<>();
    List<Company> companyList = new ArrayList<>();
    List<String> vendorIdList = new ArrayList<>();
    List<String> companyIdList = new ArrayList<>();
    CustomProgressBar customProgressBar;
    ArrayAdapter aa;
    Driver driver;
    List<String> yearList = new ArrayList<>();

    int year = Calendar.getInstance().get(Calendar.YEAR);

    public AddDriverFragment() {
        // Required empty public constructor
    }


    void getYears() {
        yearList.add(year + "");
        for (int i = 12; i > 0; i--) {
            yearList.add((year - 1) + "");
            year--;
        }
    }

    void initUI(View view) {
        addDriverButton = view.findViewById(R.id.addDriverButton);
        vendorIDSpinner = view.findViewById(R.id.vendorIDSpinner);
        serviceMakeET = view.findViewById(R.id.serviceMakeET);
        serviceYearET = view.findViewById(R.id.serviceYearET);
        serviceModelET = view.findViewById(R.id.serviceModelET);
        serviceTypeET = view.findViewById(R.id.serviceTypeET);
        serviceAreaET = view.findViewById(R.id.serviceAreaET);
        payperJobET = view.findViewById(R.id.payperJobET);
        driverZipcodeET = view.findViewById(R.id.driverZipcodeET);
        driverAddressET = view.findViewById(R.id.driverAddressET);
        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        numberET = view.findViewById(R.id.numberET);
        driverET = view.findViewById(R.id.driverET);
        getYears();
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, yearList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceYearET.setAdapter(aa);
    }

    void setData() {
        for (int i = 0; i < vendorIdList.size(); i++) {
            if (vendorIdList.get(i) == driver.getVendorId()) {
                vendorIDSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < yearList.size(); i++) {
            if (yearList.get(i).equals(driver.getServiceVehicleYear())) {
                serviceYearET.setSelection(i);
                break;
            }
        }
//        serviceYearET.setText(driver.getServiceVehicleYear());
        serviceModelET.setText(driver.getServiceVehicleModel());
        serviceTypeET.setText(driver.getServiceVehicleType());
        serviceAreaET.setText(driver.getServiceArea());
        payperJobET.setText(driver.getPayPerJob());
        driverZipcodeET.setText(driver.getDriverZipcode());
        driverAddressET.setText(driver.getDriverAddress());
        numberET.setText(driver.getDriverNumber());
        emailET.setText(driver.getDriverEmail());
        nameET.setText(driver.getDriverName());
    }

    private void onClickEvents() {

        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Driver Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(emailET.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (numberET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Driver Number", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (driverET.getText().toString().equals("")) {
//                    Toast.makeText(getActivity(), "Please enter Driver ID", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (driverAddressET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Driver Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (driverZipcodeET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Driver Zipcode", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (payperJobET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Pay Per Job", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceAreaET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Service Area", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceTypeET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Service Vehicle Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serviceModelET.getText().toString().equals("")) {

                    Toast.makeText(getActivity(), "Please enter Service Vehicle Model", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (serviceMakeET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Service Vehicle Make", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addDriverAPI(emailET.getText().toString(), numberET.getText().toString(), nameET.getText().toString(),
                            driverET.getText().toString(), driverAddressET.getText().toString(), driverZipcodeET.getText().toString(),
                            serviceAreaET.getText().toString(), payperJobET.getText().toString(), serviceTypeET.getText().toString(),
                            serviceModelET.getText().toString(), yearList.get(serviceYearET.getSelectedItemPosition()), serviceMakeET.getText().toString());
                }
            }
        });
    }


    public static AddDriverFragment createFor(String text) {
        AddDriverFragment fragment = new AddDriverFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        View view = localInflater.inflate(R.layout.fragment_add_driver, container, false);
        apiService = ApiUtils.getAPIService();
        customProgressBar = new CustomProgressBar(getActivity());
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        initUI(view);
        vendorIdList.add("Select Company");
        getCompanyList();
        if (getArguments().containsKey("driver")) {
            driver = getArguments().getParcelable("driver");
//            onFragmentChangeListener.OnTitleChangeListener("Edit Driver Details");
        }
        onClickEvents();
        return view;
    }


    private void getCompanyList() {
        customProgressBar.showProgress();
        apiService.getCompanyLists(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<CompanyModel>() {
            @Override
            public void onResponse(Call<CompanyModel> call, Response<CompanyModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    companyList = response.body().getResponse().getCompanies();
                    for (int i = 0; i < response.body().getResponse().getCompanies().size(); i++) {
                        companyIdList.add(response.body().getResponse().getCompanies().get(i).getId());
                        vendorIdList.add(response.body().getResponse().getCompanies().get(i).getCompanyName());
                    }

                    aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, vendorIdList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    vendorIDSpinner.setAdapter(aa);
                    if (getArguments().containsKey("driver")) {
                        setData();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Companies Found!", Toast.LENGTH_SHORT).show();
                }

//                companyIdList = new ArrayList<>();
//                vendorIdList = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<CompanyModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    void addDriverAPI(String email, String number, String name, String driverId, String driverAddress,
                      String driverZipCode, String serviceArea, String payPerJob, String serviceVehicleType,
                      String serviceVehicleModel, String serviceVehicleYear, String serviceVehicleMake) {

        customProgressBar.showProgress();
        apiService.registerDriver(sharedPreferenceMethod.getTokenJWT(), email, number, name,
                companyIdList.get(vendorIDSpinner.getSelectedItemPosition()), driverAddress, driverZipCode, serviceArea,
                payPerJob, serviceVehicleType, serviceVehicleModel, serviceVehicleYear, serviceVehicleMake).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    emailET.setText("");
                    nameET.setText("");
                    numberET.setText("");
                    driverET.setText("");
                    driverAddressET.setText("");
                    driverZipcodeET.setText("");
                    serviceAreaET.setText("");
                    serviceTypeET.setText("");
                    serviceMakeET.setText("");
                    serviceModelET.setText("");
                    payperJobET.setText("");
                } else {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
//        final String text = args != null ? args.getString(EXTRA_TEXT) : "";
//        TextView textView = view.findViewById(R.id.text);
//        textView.setText(text);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}