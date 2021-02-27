package com.doozycod.roadsidegenius.Activities.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.ServiceList.Service;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.Constants;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

public class EditServiceActivity extends AppCompatActivity {
    EditText serviceTypeET, costET, descriptionET;
    Button addServiceButton;

    private SharedPreferenceMethod sharedPreferenceMethod;
    private ApiService apiService;
    private CustomProgressBar customProgressBar;
    Toolbar toolbar;
    TextView selectImageTxt;
    ImageView serviceImage;
    File file;
    Service service;
    File pictureFile;

    void initUI() {
        selectImageTxt = findViewById(R.id.selectImageTxt);
        serviceImage = findViewById(R.id.serviceImage);
        addServiceButton = findViewById(R.id.addServiceButton);
        costET = findViewById(R.id.costET);
        serviceTypeET = findViewById(R.id.serviceTypeET);
        descriptionET = findViewById(R.id.descriptionET);
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
        setContentView(R.layout.activity_edit_service);
//        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();

        initUI();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        service = (Service) getIntent().getExtras().getSerializable("service");
        serviceTypeET.setText(service.getServiceType());
        costET.setText(service.getServiceCost());
        descriptionET.setText(service.getServiceDescription());
        if (service.getServiceImage().equals("")) {

        } else {
            selectImageTxt.setVisibility(GONE);
            Glide.with(this).load(Constants.FILES_BASE_URL + service.getServiceImage()).into(serviceImage);
        }
        serviceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(EditServiceActivity.this)
                        .compress(512)
                        .start();
            }
        });


        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceTypeET.getText().toString().equals("")) {
                    Toast.makeText(EditServiceActivity.this, "Please Enter Service Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (costET.getText().toString().equals("")) {
                    Toast.makeText(EditServiceActivity.this, "Please enter service cost", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (file == null) {
                        editServiceAPI(serviceTypeET.getText().toString(), costET.getText().toString(), descriptionET.getText().toString());
                    } else {
                        editServiceAPIWithFile(serviceTypeET.getText().toString(), costET.getText().toString(), descriptionET.getText().toString());
                    }
                }
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
            Glide.with(this).load(file).into(serviceImage);
            selectImageTxt.setVisibility(GONE);
            Bitmap original = BitmapFactory.decodeFile(file.getPath());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            storeImage(decoded);
            //            selectImageTxt.setText(file.getName());
            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);
            Log.e(TAG, "onActivityResult: " + filePath);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(EditServiceActivity.this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditServiceActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    void editServiceAPI(String type, String cost, String description) {
        customProgressBar.showProgress();


        RequestBody token = RequestBody.create(sharedPreferenceMethod.getTokenJWT(), okhttp3.MultipartBody.FORM);
        RequestBody id = RequestBody.create(service.getId(), okhttp3.MultipartBody.FORM);
        RequestBody descriptionBody = RequestBody.create(description, okhttp3.MultipartBody.FORM);
        RequestBody costBody = RequestBody.create(cost, okhttp3.MultipartBody.FORM);
        RequestBody typeBody = RequestBody.create(type, okhttp3.MultipartBody.FORM);

        apiService.editService(token, id, typeBody, costBody, descriptionBody).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(EditServiceActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditServiceActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void storeImage(Bitmap image) {

        pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error while creating media file, Please ask for storage permission");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile() {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "RG_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    void editServiceAPIWithFile(String type, String cost, String description) {
        customProgressBar.showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(EditServiceActivity.this).clearDiskCache();
//                Glide.get(EditServiceActivity.this).clearMemory();
            }
        }).start();
        RequestBody token = RequestBody.create(sharedPreferenceMethod.getTokenJWT(), okhttp3.MultipartBody.FORM);
        RequestBody id = RequestBody.create(service.getId(), okhttp3.MultipartBody.FORM);
        RequestBody descriptionBody = RequestBody.create(description, okhttp3.MultipartBody.FORM);
        RequestBody costBody = RequestBody.create(cost, okhttp3.MultipartBody.FORM);
        RequestBody typeBody = RequestBody.create(type, okhttp3.MultipartBody.FORM);

        RequestBody fbody = RequestBody.create(
                MediaType.parse("multipart/form-data"), pictureFile);
        MultipartBody.Part files = MultipartBody.Part.createFormData("image", pictureFile.getName(), fbody);

        apiService.editServiceWithFile(token, id, typeBody, costBody, descriptionBody, files).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(EditServiceActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditServiceActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}