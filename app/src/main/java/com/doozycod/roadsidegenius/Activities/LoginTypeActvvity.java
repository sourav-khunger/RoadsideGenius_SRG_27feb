package com.doozycod.roadsidegenius.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.doozycod.roadsidegenius.Activities.Admin.AdminLoginActivity;
import com.doozycod.roadsidegenius.Activities.Customer.CustomerLoginActivity;
import com.doozycod.roadsidegenius.Activities.Driver.DriverLoginActvity;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginTypeActvvity extends AppCompatActivity {
    Button driverButton, customerButton, adminButton;
    SharedPreferenceMethod sharedPreferenceMethod;
    String token;
    private String android_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setTheme(R.style.DarkTheme);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
//        sharedPreferenceMethod.setTheme("dark");
        if (sharedPreferenceMethod.getTheme().equals("")) {
            setTheme(R.style.LightTheme);
            sharedPreferenceMethod.setTheme("light");
        } else {
            setTheme(sharedPreferenceMethod.getTheme().equals("light") ? R.style.LightTheme : R.style.DarkTheme);
        }
        setContentView(R.layout.activity_login_type_actvvity);
        adminButton = findViewById(R.id.adminButton);
        driverButton = findViewById(R.id.driverButton);
        customerButton = findViewById(R.id.customerButton);
        if (sharedPreferenceMethod.getFCMToken().equals("")) {
            generatePushToken();
//            registerReceiver(new FirebaseBroadcastReceiver(),new IntentFilter("android.intent.category.LAUNCHER"));
        }
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (sharedPreferenceMethod.getDeviceId().equals("")) {

            sharedPreferenceMethod.saveDeviceID(android_id);
        }
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginTypeActvvity.this, AdminLoginActivity.class));
            }
        });
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginTypeActvvity.this, CustomerLoginActivity.class));
            }
        });
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginTypeActvvity.this, DriverLoginActvity.class));
            }
        });


    }

    public String generatePushToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.e("TAG", token);
                        sharedPreferenceMethod.saveToken(token);
//                        Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("TOKEN", "getInstanceId failed", task.getException());
//                            Toast.makeText(SplashActivity.this, "Unable to connect!\nCheck Connectivity...", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        // Get new Instance ID token
//                        token = task.getResult().getToken();

//                    }
//                });
        return token;
    }

}