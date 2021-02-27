package com.doozycod.roadsidegenius.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.AddCustomerNumberModel.VerifyOTPModel;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.FileUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCompanyFragment extends Fragment {
    private static final String EXTRA_TEXT = "text";

    EditText nameET, emailET, numberET, vendorIdET, driverAddressET, companyCityET, stateET, zipcodeET,
            primaryServiceET;
    Spinner w9FormSpinner, l9formSpinner;
    List<String> list = new ArrayList<>();
    Button addCompanyButton;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    ImageView selctPicker1, filePicker2, filePicker3;
    private List<Uri> docPaths;
    private List<Uri> docPaths2;
    private List<Uri> docPaths3;
    TextView selectfile, selectfile2, selectfile3;
    EditText primaryNumberET, secondaryNumberET;
    File i9FormsPath, w9FormPath, coiPath;
    String countryCode = "";
    String countryCode2 = "";
    String otpString = "";
    boolean isSent = false;
    ImageView contactDialogButton, contactDialogButton2;
    private String number = "";
    CountryCodePicker countryCodePickerPrimary, countryCodePickerSecondary;

    public AddCompanyFragment() {
        // Required empty public constructor
    }

    void initUI(View view) {
        countryCodePickerPrimary = view.findViewById(R.id.countryCodep);
        countryCodePickerSecondary = view.findViewById(R.id.countryCodesecond);
//        contactDialogButton2 = view.findViewById(R.id.contactDialogButton2);
//        contactDialogButton = view.findViewById(R.id.contactDialogButton);
        secondaryNumberET = view.findViewById(R.id.secondaryNumberET);
        primaryNumberET = view.findViewById(R.id.primaryNumberET);
        primaryServiceET = view.findViewById(R.id.primaryServiceET);
        zipcodeET = view.findViewById(R.id.zipcodeET);
        stateET = view.findViewById(R.id.stateET);
        companyCityET = view.findViewById(R.id.companyCityET);
        driverAddressET = view.findViewById(R.id.driverAddressET);
        selectfile3 = view.findViewById(R.id.selectfile3);
        filePicker3 = view.findViewById(R.id.filePicker3);
        selectfile = view.findViewById(R.id.selectfile);
        selectfile2 = view.findViewById(R.id.selectfile2);
        filePicker2 = view.findViewById(R.id.filePicker2);
        selctPicker1 = view.findViewById(R.id.selctPicker1);
        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        numberET = view.findViewById(R.id.numberET);
        vendorIdET = view.findViewById(R.id.vendorET);
//        w9FormSpinner = view.findViewById(R.id.w9formsSpinner);
//        l9formSpinner = view.findViewById(R.id.l9formsSpinner);
        addCompanyButton = view.findViewById(R.id.addCompanyButton);
    }

    public static AddCompanyFragment createFor(String text) {
        AddCompanyFragment fragment = new AddCompanyFragment();
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
        View view = localInflater.inflate(R.layout.fragment_add_company, container, false);

        sharedPreferenceMethod = new SharedPreferenceMethod(getContext());
        customProgressBar = new CustomProgressBar(getContext());
        apiService = ApiUtils.getAPIService();
        initUI(view);

        countryCodePickerPrimary.setAutoDetectedCountry(true);
        countryCodePickerPrimary.setCountryForNameCode("US");

        countryCode = countryCodePickerPrimary.getSelectedCountryCode();
        countryCodePickerPrimary.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePickerPrimary.getSelectedCountryCode();
            }
        });


        countryCodePickerSecondary.setAutoDetectedCountry(true);
        countryCodePickerSecondary.setCountryForNameCode("US");

        countryCode2 = countryCodePickerSecondary.getSelectedCountryCode();
        countryCodePickerSecondary.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode2 = countryCodePickerSecondary.getSelectedCountryCode();
            }
        });


        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
//        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, list);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        w9FormSpinner.setAdapter(aa);
//        l9formSpinner.setAdapter(aa);
        onClickEvents();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
//                    String file = ;
                    selectfile.setText(new File(FileUtils.getPath(getActivity(), docPaths.get(0))).getName() + "");
                    i9FormsPath = new File(FileUtils.getPath(getActivity(), docPaths.get(0)));
                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(getActivity(), docPaths.get(0))).getPath());
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths2 = new ArrayList<>();
                    docPaths2.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    selectfile2.setText(new File(FileUtils.getPath(getActivity(), docPaths2.get(0))).getName() + "");
                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(getActivity(), docPaths.get(0))).getPath());
                    w9FormPath = new File(FileUtils.getPath(getActivity(), docPaths2.get(0)));
                }
                break;
            case 103:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths3 = new ArrayList<>();
                    docPaths3.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    selectfile3.setText(new File(FileUtils.getPath(getActivity(), docPaths3.get(0))).getName() + "");
                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(getActivity(), docPaths.get(0))).getPath());
                    coiPath = new File(FileUtils.getPath(getActivity(), docPaths3.get(0)));
                }
                break;
        }
    }

    private void onClickEvents() {
//        contactDialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showContactDialog();
//            }
//        });
//        contactDialogButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showContactDialog2();
//            }
//        });
        selctPicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme)//optional
                        .pickFile(getActivity(), 101);
            }
        });
        filePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickFile(getActivity(), 102);
            }
        });
        filePicker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickFile(getActivity(), 103);
            }
        });
        addCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Company Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Company Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(emailET.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (numberET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Company Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (primaryNumberET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter primary number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (companyCityET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stateET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter state", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (driverAddressET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter company address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zipcodeET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter zipcode", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (w9FormPath == null) {
                    Toast.makeText(getActivity(), "Please upload w9 form!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (i9FormsPath == null) {
                    Toast.makeText(getActivity(), "Please upload i 9 form!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (coiPath == null) {
                    Toast.makeText(getActivity(), "Please upload i 9 form!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addCompanyAPI(emailET.getText().toString(),
                            numberET.getText().toString(),
                            countryCode + primaryNumberET.getText().toString(),
                            countryCode + secondaryNumberET.getText().toString(),
                            nameET.getText().toString(), driverAddressET.getText().toString(),
                            companyCityET.getText().toString(), stateET.getText().toString(),
                            zipcodeET.getText().toString(), primaryServiceET.getText().toString());
                }
            }
        });


    }

    void addCompanyAPI(String email, String number, String primaryNumber, String secondaryNumber, String name,
                       String address, String city, String state, String zipcode, String primary_service_area) {
        customProgressBar.showProgress();
        RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferenceMethod.getTokenJWT());
        RequestBody emailBody = RequestBody.create(okhttp3.MultipartBody.FORM, email);
        RequestBody numberBody = RequestBody.create(okhttp3.MultipartBody.FORM, number);
        RequestBody primaryNumberBody = RequestBody.create(okhttp3.MultipartBody.FORM, primaryNumber);
        RequestBody secondaryNumberBody = RequestBody.create(okhttp3.MultipartBody.FORM, secondaryNumber);
        RequestBody nameBody = RequestBody.create(okhttp3.MultipartBody.FORM, name);
        RequestBody addressBody = RequestBody.create(okhttp3.MultipartBody.FORM, address);
        RequestBody cityBody = RequestBody.create(okhttp3.MultipartBody.FORM, city);
        RequestBody stateBody = RequestBody.create(okhttp3.MultipartBody.FORM, state);
        RequestBody zipcodeBody = RequestBody.create(okhttp3.MultipartBody.FORM, zipcode);
        RequestBody primaryServiceAreaBody = RequestBody.create(okhttp3.MultipartBody.FORM, primary_service_area);

        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"),
                i9FormsPath);
        MultipartBody.Part files = MultipartBody.Part.createFormData("i_9_forms", i9FormsPath.getName(), fbody);
        RequestBody fbody2 = RequestBody.create(MediaType.parse("multipart/form-data"),
                w9FormPath);
        MultipartBody.Part files2 = MultipartBody.Part.createFormData("w_9_forms", w9FormPath.getName(), fbody2);
        RequestBody fbody3 = RequestBody.create(MediaType.parse("multipart/form-data"),
                coiPath);
        MultipartBody.Part files3 = MultipartBody.Part.createFormData("coi", coiPath.getName(), fbody3);

        apiService.registerCompany(token, emailBody, numberBody, nameBody, addressBody, cityBody, stateBody, zipcodeBody,
                primaryNumberBody, secondaryNumberBody, primaryServiceAreaBody, files, files2, files3).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    emailET.setText("");
                    nameET.setText("");
                    numberET.setText("");
                    vendorIdET.setText("");
//                    w9FormSpinner.setSelection(0);
//                    l9formSpinner.setSelection(0);
                } else {
                    Toast.makeText(getActivity(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(getActivity(), "Something went Wrong!\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
        countryCode = countryCodePicker.getDefaultCountryCode();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCode();
            }
        });
        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSent) {
                    getOTP(countryCode + contactNumber.getText().toString(), sendOtpButton);
                    showOTP.setVisibility(View.VISIBLE);
                } else {
                    if (enteredOtp[0].equals(otpString)) {
                        Toast.makeText(getActivity(), "Phone number verified!", Toast.LENGTH_SHORT).show();
                        primaryNumberET.setText("+" + countryCode + contactNumber.getText().toString());
                        dialog.dismiss();
                    }
                }

            }
        });
        dialog.show();
    }

    void showContactDialog2() {
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
        countryCode = countryCodePicker.getDefaultCountryCode();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCode();
            }
        });
        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSent) {
                    getOTP(countryCode + contactNumber.getText().toString(), sendOtpButton);
                    showOTP.setVisibility(View.VISIBLE);
                } else {
                    if (enteredOtp[0].equals(otpString)) {
                        Toast.makeText(getActivity(), "Phone number verified!", Toast.LENGTH_SHORT).show();
                        secondaryNumberET.setText("+" + countryCode + contactNumber.getText().toString());
                        dialog.dismiss();
                    }
                }

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


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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
}