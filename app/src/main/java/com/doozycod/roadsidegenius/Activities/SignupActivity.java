package com.doozycod.roadsidegenius.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.doozycod.roadsidegenius.R;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    List<String> serviceList = new ArrayList<>();
    Spinner spinner;
    TextView requestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        spinner = findViewById(R.id.serviceTypeSpinner);
        requestButton = findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, RequestActivity.class));
            }
        });
    }
}