package com.doozycod.roadsidegenius.Activities.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Model.OTP.OTPModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity {
    private OtpView otpView;
    SmsVerifyCatcher smsVerifyCatcher;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    LinearLayout resendOTPView;
    Timer timer;
    TextView resendButton;

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
        setContentView(R.layout.activity_verify_o_t_p);
        otpView = findViewById(R.id.otp_view);
        resendButton = findViewById(R.id.resendButton);
        resendOTPView = findViewById(R.id.resendOTPView);

        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                otpView.setText(code);
            }
        });
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        resendOTPView.setVisibility(View.VISIBLE);

                        // Stuff that updates the UI

                    }
                });
            }
        }, 30 * 1000);
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Log.d("onOtpCompleted=>", otp);
                verifyOTPAPI(otp);
            }
        });

    }

    private String parseCode(String message) {
//        Pattern p = Pattern.compile("\\b\\d{4}\\b");
//        Matcher m = p.matcher(message);
        String code = message.replaceAll("[^0-9]", "");
//        while (m.find()) {
//            code = m.group(0);
//        }
        return code;
    }

    void verifyOTPAPI(String otp) {
        customProgressBar.showProgress();
        apiService.verifyOTP(sharedPreferenceMethod.getTokenJWT(), otp,
                sharedPreferenceMethod.getDeviceId(), sharedPreferenceMethod.getFCMToken()).enqueue(new Callback<OTPModel>() {
            @Override
            public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {
                customProgressBar.hideProgress();
                timer.cancel();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    sharedPreferenceMethod.saveUserType("customer");
                    sharedPreferenceMethod.saveCustomerContact(response.body().getResponse().getUserData().getCustomerContact());
                    sharedPreferenceMethod.saveCustomerId(response.body().getResponse().getUserData().getId());
                    Toast.makeText(VerifyOTPActivity.this, "Verified!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VerifyOTPActivity.this, DashboardCustomerActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(VerifyOTPActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<OTPModel> call, Throwable t) {

                customProgressBar.hideProgress();
                Toast.makeText(VerifyOTPActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}