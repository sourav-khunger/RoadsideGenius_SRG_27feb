package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.Company.Company;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.FileUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.hbb20.CountryCodePicker;

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

public class CompanyDetailsActvity extends AppCompatActivity {

    EditText nameET, emailET, numberET, vendorIdET, driverAddressET, companyCityET, stateET, zipcodeET,
            primaryServiceET;
    //    Spinner w9FormSpinner, l9formSpinner;
    List<String> list = new ArrayList<>();
    TextView selectfile, selectfile2, selectfile3;
    EditText primaryNumberET, secondaryNumberET;
    Company company;
    Toolbar toolbar;
    SharedPreferenceMethod sharedPreferenceMethod;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    File i9FormsPath, w9FormPath, coiPath;
    private List<Uri> docPaths;
    private List<Uri> docPaths2;
    private List<Uri> docPaths3;
    ImageView selctPicker1, filePicker2, filePicker3;
    Button updateCompanyButton;

    void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateCompanyButton = findViewById(R.id.updateCompanyButton);
        secondaryNumberET = findViewById(R.id.secondaryNumberET);
        primaryNumberET = findViewById(R.id.primaryNumberET);
        primaryServiceET = findViewById(R.id.primaryServiceET);
        zipcodeET = findViewById(R.id.zipcodeET);
        stateET = findViewById(R.id.stateET);
        companyCityET = findViewById(R.id.companyCityET);
        driverAddressET = findViewById(R.id.driverAddressET);

        selectfile3 = findViewById(R.id.selectfile3);
        filePicker3 = findViewById(R.id.filePicker3);
        selectfile = findViewById(R.id.selectfile);
        selectfile2 = findViewById(R.id.selectfile2);
        filePicker2 = findViewById(R.id.filePicker2);
        selctPicker1 = findViewById(R.id.selctPicker1);
        selectfile3 = findViewById(R.id.selectfile3);
        selectfile = findViewById(R.id.selectfile);
        selectfile2 = findViewById(R.id.selectfile2);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        vendorIdET = findViewById(R.id.vendorET);
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
        setContentView(R.layout.activity_company_details_screen);
        initUI();
        customProgressBar = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();
        if (getIntent() != null) {
            company = (Company) getIntent().getExtras().getSerializable("company");
            selectfile3.setText(company.getCoi());
            selectfile2.setText(company.getW9Forms());
            selectfile.setText(company.getI9Forms());
            secondaryNumberET.setText("+" + company.getSecondaryPhone());
            primaryNumberET.setText("+" + company.getPrimaryPhone());
            primaryServiceET.setText(company.getPrimaryServiceArea());
            zipcodeET.setText(company.getCompanyZipcode());
            companyCityET.setText(company.getCompanyCity());
            stateET.setText(company.getCompanyState());
            driverAddressET.setText(company.getCompanyAddress());
            nameET.setText(company.getCompanyName());
            numberET.setText(company.getCompanyNumber());
            emailET.setText(company.getCompanyEmail());

        }
        onClickEvents();
    }

    private void onClickEvents() {
        selctPicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme)//optional
                        .pickFile(CompanyDetailsActvity.this, 101);
            }
        });
        filePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickFile(CompanyDetailsActvity.this, 102);
            }
        });
        filePicker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickFile(CompanyDetailsActvity.this, 103);
            }
        });
        updateCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter Company Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter Company Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(emailET.getText().toString())) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (numberET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter Company Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (primaryNumberET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter primary number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (companyCityET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stateET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter state", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (driverAddressET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter company address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (zipcodeET.getText().toString().equals("")) {
                    Toast.makeText(CompanyDetailsActvity.this, "Please enter zipcode", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    updateCompanyAPI(company.getId(), emailET.getText().toString(),
                            numberET.getText().toString(),
                            primaryNumberET.getText().toString().replaceAll("\\+", ""),
                            secondaryNumberET.getText().toString().replaceAll("\\+", ""),
                            nameET.getText().toString(), driverAddressET.getText().toString(),
                            companyCityET.getText().toString(), stateET.getText().toString(),
                            zipcodeET.getText().toString(), primaryServiceET.getText().toString());
                }
            }
        });
    }

    void updateCompanyAPI(String id, String email, String number, String primaryNumber, String secondaryNumber, String name,
                          String address, String city, String state, String zipcode, String primary_service_area) {
        customProgressBar.showProgress();
        RequestBody companyId = RequestBody.create(okhttp3.MultipartBody.FORM, id);
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
        RequestBody fbody = null, fbody2 = null, fbody3 = null;
        MultipartBody.Part files = null;
        MultipartBody.Part files2 = null;
        MultipartBody.Part files3 = null;
        if (i9FormsPath == null) {
            fbody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    "");
            files = MultipartBody.Part.createFormData("i_9_forms", "", fbody);

        } else {
            fbody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    i9FormsPath);
            files = MultipartBody.Part.createFormData("i_9_forms", i9FormsPath.getName(), fbody);

        }
        if (w9FormPath == null) {
            fbody2 = RequestBody.create(MediaType.parse("multipart/form-data"),
                    "");
            files2 = MultipartBody.Part.createFormData("w_9_forms", "", fbody2);
        } else {
            fbody2 = RequestBody.create(MediaType.parse("multipart/form-data"),
                    w9FormPath);
            files2 = MultipartBody.Part.createFormData("w_9_forms", w9FormPath.getName(), fbody2);

        }
        if (coiPath == null) {
            fbody3 = RequestBody.create(MediaType.parse("multipart/form-data"),
                    "");
            files3 = MultipartBody.Part.createFormData("coi", "", fbody3);
        } else {
            fbody3 = RequestBody.create(MediaType.parse("multipart/form-data"),
                    coiPath);
            files3 = MultipartBody.Part.createFormData("coi", coiPath.getName(), fbody3);

        }
//        =MultipartBody.Part.createFormData("i_9_forms", i9FormsPath.getName(), fbody);
//         =MultipartBody.Part.createFormData("w_9_forms", w9FormPath.getName(), fbody2);
//         =MultipartBody.Part.createFormData("coi", coiPath.getName(), fbody3);

        apiService.editCompany(token, companyId, emailBody, numberBody, nameBody, addressBody, cityBody,
                stateBody, zipcodeBody,
                primaryNumberBody, secondaryNumberBody, primaryServiceAreaBody, files, files2, files3).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(CompanyDetailsActvity.this, response.body().getResponse().getMessage(),
                            Toast.LENGTH_SHORT).show();

//                    w9FormSpinner.setSelection(0);
//                    l9formSpinner.setSelection(0);
                } else {
                    Toast.makeText(CompanyDetailsActvity.this, response.body().getResponse().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();
                Log.e("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(CompanyDetailsActvity.this, "Something went Wrong!\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
//                    String file = ;
                    selectfile.setText(new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths.get(0))).getName() + "");
                    i9FormsPath = new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths.get(0)));
                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths.get(0))).getPath());
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths2 = new ArrayList<>();
                    docPaths2.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    selectfile2.setText(new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths2.get(0))).getName() + "");
                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths.get(0))).getPath());
                    w9FormPath = new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths2.get(0)));
                }
                break;
            case 103:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths3 = new ArrayList<>();
                    docPaths3.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    selectfile3.setText(new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths3.get(0))).getName() + "");
                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths.get(0))).getPath());
                    coiPath = new File(FileUtils.getPath(CompanyDetailsActvity.this, docPaths3.get(0)));
                }
                break;
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}