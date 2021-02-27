package com.doozycod.roadsidegenius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.doozycod.roadsidegenius.Activities.Customer.DashboardCustomerActivity;
import com.doozycod.roadsidegenius.Activities.Driver.DriverDashboardActivity;
import com.doozycod.roadsidegenius.Activities.Admin.DashboardAdminActivity;
import com.doozycod.roadsidegenius.Activities.LoginTypeActvvity;
import com.doozycod.roadsidegenius.Activities.CustomerDetailsActivity;
import com.doozycod.roadsidegenius.Utils.LocationService;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    SharedPreferenceMethod sharedPreferenceMethod;
    String token;
    private String android_id = "";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().hasExtra("jsonObject")) {
            Intent intent1 = new Intent(this, CustomerDetailsActivity.class);
            intent1.putExtra("jsonObject", intent.getStringExtra("jsonObject"));
            startActivity(intent1);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.e("MainActivity: ", "Key: " + key + " Value: " + value);
            }
        }
        Log.e("TAG", "onCreate: " + sharedPreferenceMethod.getLogin());
        if (sharedPreferenceMethod.getFCMToken().equals("")) {
            generatePushToken();
//            registerReceiver(new FirebaseBroadcastReceiver(),new IntentFilter("android.intent.category.LAUNCHER"));
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (sharedPreferenceMethod.getLogin().equals("admin")) {
                    startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                    finish();
                    return;
                }
                if (sharedPreferenceMethod.getLogin().equals("driver")) {
                    stopService(new Intent(getApplicationContext(), LocationService.class));
                    startService(new Intent(getApplicationContext(), LocationService.class));
                    startActivity(new Intent(SplashActivity.this, DriverDashboardActivity.class));

                    finish();

                    return;

                }
                if (sharedPreferenceMethod.getLogin().equals("customer")) {
                    startActivity(new Intent(SplashActivity.this, DashboardCustomerActivity.class));
                    finish();
                    return;

                } else {
                    startActivity(new Intent(SplashActivity.this, LoginTypeActvvity.class));
                    finish();
                }

            }
        }, 3000);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String CHANNEL_ID = "my_channel_01";
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle("")
//                    .setContentText("").build();
//
//            startForeground(1, notification);
//        } else {
//            startService(new Intent(this, MyFirebaseMessagingService.class));
//        }
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (sharedPreferenceMethod.getDeviceId().equals("")) {

            sharedPreferenceMethod.saveDeviceID(android_id);
        }

//        generatePushToken();
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