package com.doozycod.roadsidegenius.Activities.Driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.WalletData.PendingWithdrawlRequest;
import com.doozycod.roadsidegenius.Model.WalletData.WalletDataModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawDriverActivity extends AppCompatActivity {
    MaterialCheckBox materialCheckBox;
    TextInputEditText textInputEditText;
    Button withdraw;
    TextInputLayout textInput;
    ImageView backButton;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    TextView totalWalletTXT;
    PendingWithdrawlRequest pendingWithdrawlRequest;
    String amountString = "";

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
        setContentView(R.layout.activity_withdraw_driver);
        totalWalletTXT = findViewById(R.id.totalWalletTXT);
        textInput = findViewById(R.id.textInput);
        materialCheckBox = findViewById(R.id.checkBox);
        textInputEditText = findViewById(R.id.amount);
        withdraw = findViewById(R.id.withdrawButton);
        backButton = findViewById(R.id.backButton);

        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        customProgressBar = new CustomProgressBar(this);
        apiService = ApiUtils.getAPIService();

        if (materialCheckBox.isChecked()) {
            textInput.setEnabled(false);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (materialCheckBox.isChecked()) {
                    textInput.setEnabled(false);

                } else {
                    textInput.setEnabled(true);

                }
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (materialCheckBox.isChecked()) {
                    if (pendingWithdrawlRequest != null) {
                        Toast.makeText(WithdrawDriverActivity.this, "Your request of $"
                                + pendingWithdrawlRequest.getAmount() + " is still under process.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    requestPayment(amountString);

                } else {
                    if (pendingWithdrawlRequest != null) {
                        Toast.makeText(WithdrawDriverActivity.this, "Your request of $"
                                + pendingWithdrawlRequest.getAmount() + " is still under process.", Toast.LENGTH_SHORT).show();

                    } else {
                        if (textInputEditText.getText().toString().equals("")) {
                            Toast.makeText(WithdrawDriverActivity.this, "Please enter amount you want to withdraw", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        requestPayment(textInputEditText.getText().toString());

                    }

                }
            }
        });
        getWalletData();
    }

    void requestPayment(String amount) {
        customProgressBar.showProgress();
        apiService.requestPaymentDriver(sharedPreferenceMethod.getTokenJWT(), amount).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {

                    Toast.makeText(WithdrawDriverActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    void getWalletData() {
        customProgressBar.showProgress();
        apiService.driverWalletData(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<WalletDataModel>() {
            @Override
            public void onResponse(Call<WalletDataModel> call, Response<WalletDataModel> response) {
                customProgressBar.hideProgress();
                if (response.body().getResponse().getStatus().equals("Success")) {
                    if (response.body().getResponse().getWalletData() != null) {
//                        bundle.putSerializable("wallet",response.body().getResponse().getWalletData());
                        amountString = response.body().getResponse().getWalletData().getClosingBalance();
                        totalWalletTXT.setText("$" + response.body().getResponse().getWalletData().getClosingBalance());

                    } else {
                        totalWalletTXT.setText("$0.00");
                    }
                }
                // show pending request here

//                if (response.body().getResponse().getPendingWithdrawlRequest() != null) {
//                    pendingWithdrawlRequest = response.body().getResponse().getPendingWithdrawlRequest();
//                    int amount = 0;
//                    int wallet = Integer.parseInt(response.body().getResponse().getWalletData().getClosingBalance());
//                    if(!pendingWithdrawlRequest.getAmount().equals("")){
//                        int pending = Integer.parseInt(pendingWithdrawlRequest.getAmount());
//                        amount = wallet - pending;
//                    }
//
//                    totalWalletTXT.setText("$" + amount);
//
//                }
            }

            @Override
            public void onFailure(Call<WalletDataModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

}