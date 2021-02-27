package com.doozycod.roadsidegenius.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.FileUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

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

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

public class UploadDriverFragment extends Fragment {

    CountDownTimer mCountDownTimer, mCountDownTimer2, mCountDownTimer3;
    int i = 0;
    int i1 = 0;
    int i2 = 0;
    private List<Uri> docPaths;
    private List<Uri> docPaths2;
    private List<Uri> docPaths3;
    private String i9FormsPath = "", w9FormPath = "", coiPath = "";
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;

    public UploadDriverFragment() {
        // Required empty public constructor
    }

    RoundCornerProgressBar roundCornerProgressBar, roundCornerProgressBar2, roundCornerProgressBar3;
    LinearLayout selectPicker, selectPicker2, selectPicker3;
    TextView fileName, fileName2, fileName3;
    TextView selectTxt, selectTxt2, selectTxt3;

    void initUI(View view) {
        selectPicker = view.findViewById(R.id.selectPicker);
        selectPicker2 = view.findViewById(R.id.selectPicker2);
        selectPicker3 = view.findViewById(R.id.selectPicker3);
        selectTxt = view.findViewById(R.id.selectTxt);
        selectTxt2 = view.findViewById(R.id.selectTxt2);
        selectTxt3 = view.findViewById(R.id.selectTxt3);

        roundCornerProgressBar = view.findViewById(R.id.progress_bar);
        roundCornerProgressBar2 = view.findViewById(R.id.progress_bar2);
        roundCornerProgressBar3 = view.findViewById(R.id.progress_bar3);

        fileName = view.findViewById(R.id.fileNameTxt);
        fileName2 = view.findViewById(R.id.fileNameTxt2);
        fileName3 = view.findViewById(R.id.fileNameTxt3);
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
        View view = localInflater.inflate(R.layout.fragment_upload_driver, container, false);

        initUI(view);
        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

        selectPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme)//optional
                        .pickFile(getActivity(), 201);
            }
        });
        selectPicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickFile(getActivity(), 202);
            }
        });
        selectPicker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.Companion.getInstance()
                        .setMaxCount(1) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickFile(getActivity(), 203);
            }
        });


        return view;
    }

    void uploadScaForm() {
        Log.e(TAG, "uploadScaForm: COI");

        RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferenceMethod.getTokenJWT());
        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"),
                i9FormsPath);
        MultipartBody.Part files = MultipartBody.Part.createFormData("sca", i9FormsPath, fbody);
        apiService.driverUploadSCAFiles(token, files).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    selectTxt.setVisibility(View.VISIBLE);

                    roundCornerProgressBar.setProgress(100);
                    fileName.setVisibility(View.VISIBLE);
                    selectTxt.setText("File Uploaded");
                    fileName.setText(new File(i9FormsPath).getName());
                    mCountDownTimer.cancel();
                    roundCornerProgressBar.setVisibility(GONE);

                }
                i = 0;
                roundCornerProgressBar.setProgress(0);
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    void uploadCOIForm() {
        Log.e(TAG, "uploadCOIForm: COI");
        RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferenceMethod.getTokenJWT());
        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"),
                coiPath);
        MultipartBody.Part files = MultipartBody.Part.createFormData("coi", coiPath, fbody);
        apiService.driverUploadCOIFiles(token, files).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    selectTxt2.setVisibility(View.VISIBLE);

                    roundCornerProgressBar2.setProgress(100);
                    selectTxt2.setText("File Uploaded");
                    fileName2.setText(new File(coiPath).getName());
                    mCountDownTimer2.cancel();
                    roundCornerProgressBar2.setVisibility(GONE);
                    fileName2.setVisibility(View.VISIBLE);

                }
                i1 = 0;
                roundCornerProgressBar2.setProgress(0);
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    void uploadW9Form() {
        Log.e(TAG, "uploadW9Form: COI");

        RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferenceMethod.getTokenJWT());
        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"),
                w9FormPath);
        MultipartBody.Part files = MultipartBody.Part.createFormData("w_9_forms", w9FormPath, fbody);
        apiService.driverUploadW9Files(token, files).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    selectTxt3.setVisibility(View.VISIBLE);

                    roundCornerProgressBar3.setProgress(100);
                    selectTxt3.setText("File Uploaded");
                    fileName3.setVisibility(View.VISIBLE);
                    fileName3.setText(new File(w9FormPath).getName());
                    mCountDownTimer3.cancel();
                    roundCornerProgressBar3.setVisibility(GONE);
                }
                i2 = 0;
                roundCornerProgressBar3.setProgress(0);
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 201:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
//                    String file = ;
                    fileName.setText("Uploading...");
                    selectTxt.setVisibility(GONE);
                    i9FormsPath = "file://" + new File(FileUtils.getPath(getActivity(), docPaths.get(0))).getPath();
                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(getActivity(), docPaths.get(0))).getPath());
                    roundCornerProgressBar.setVisibility(View.VISIBLE);
                    roundCornerProgressBar.setProgress(i);
                    mCountDownTimer = new CountDownTimer(3000, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
//                            Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                            i++;
                            if (roundCornerProgressBar.getProgress() <= 70) {
                                roundCornerProgressBar.setProgress((int) i * 10 / (5000 / 1000));
                            }
                        }

                        @Override
                        public void onFinish() {
                            i++;
                            roundCornerProgressBar.setProgress(70);
                            uploadScaForm();

                        }
                    };
                    mCountDownTimer.start();
                }
                break;
            case 203:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths3 = new ArrayList<>();
                    docPaths3.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    fileName3.setText("Uploading...");
                    selectTxt3.setVisibility(GONE);

                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(getActivity(), docPaths.get(0))).getPath());
                    w9FormPath = "file://" + new File(FileUtils.getPath(getActivity(), docPaths3.get(0))).getPath();
                    roundCornerProgressBar3.setVisibility(View.VISIBLE);
                    roundCornerProgressBar3.setProgress(i2);
                    mCountDownTimer3 = new CountDownTimer(5000, 10) {

                        @Override
                        public void onTick(long millisUntilFinished) {
//                            Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                            i2++;
                            if (roundCornerProgressBar3.getProgress() <= 70) {
                                roundCornerProgressBar3.setProgress((int) i2 * 10 / (5000 / 1000));
                            }
                        }

                        @Override
                        public void onFinish() {
                            i2++;
                            roundCornerProgressBar3.setProgress(70);
                        }
                    };
                    mCountDownTimer3.start();
                    uploadW9Form();
                }
                break;
            case 202:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths2 = new ArrayList<>();
                    docPaths2.addAll(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    fileName2.setText("Uploading...");
                    selectTxt2.setVisibility(GONE);

                    Log.e("TAG", "onActivityResult: " + new File(FileUtils.getPath(getActivity(), docPaths2.get(0))).getPath());
                    coiPath = "file://" + new File(FileUtils.getPath(getActivity(), docPaths2.get(0))).getPath();
                    roundCornerProgressBar2.setVisibility(View.VISIBLE);
                    roundCornerProgressBar2.setProgress(i1);
                    mCountDownTimer2 = new CountDownTimer(5000, 10) {

                        @Override
                        public void onTick(long millisUntilFinished) {
//                            Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                            i++;
                            if (roundCornerProgressBar2.getProgress() <= 70) {
                                roundCornerProgressBar2.setProgress((int) i1 * 10 / (5000 / 1000));
                            }
                        }

                        @Override
                        public void onFinish() {
                            i1++;
                            roundCornerProgressBar2.setProgress(70);
                        }
                    };
                    mCountDownTimer2.start();
                    uploadCOIForm();
                }
                break;
        }
    }
}