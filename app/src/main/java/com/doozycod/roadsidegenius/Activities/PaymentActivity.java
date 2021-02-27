package com.doozycod.roadsidegenius.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Activities.Customer.DashboardCustomerActivity;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.JobRequestModel.JobRequestModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.Constants;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentActivity extends AppCompatActivity {
    private static final String BACKEND_URL = "https://mighty-lake-03639.herokuapp.com/";
    private static String jobId = "";
    CardMultilineWidget cardMultilineWidget;
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    String price = "0.00", payment = "";
    EditText nameET, addressET, cityET, countryET, postalCodeET, bonus;
    TextView priceTXT;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    String name, email, service, number, getLocation, getDropOffLocation, notes,vehicleMake,model,color,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        bonus = findViewById(R.id.bonusAmountTxt);
        priceTXT = findViewById(R.id.priceTXT);
        postalCodeET = findViewById(R.id.postalCodeET);
        nameET = findViewById(R.id.nameET);
        addressET = findViewById(R.id.addressET);
        countryET = findViewById(R.id.countryET);
        cityET = findViewById(R.id.cityET);

        sharedPreferenceMethod = new SharedPreferenceMethod(this);
//        sharedPreferenceMethod.setTheme("dark");
        if (sharedPreferenceMethod != null) {
            setTheme(sharedPreferenceMethod.getTheme().equals("light") ? R.style.LightTheme : R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        customProgressBar = new CustomProgressBar(this);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        apiService = ApiUtils.getAPIService();

        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull(Constants.STRIPE_CLIENT_ID_TEST)
        );
        year = getIntent().getStringExtra("year");
        color = getIntent().getStringExtra("color");
        model = getIntent().getStringExtra("model");
        vehicleMake = getIntent().getStringExtra("vehicleMake");
        price = getIntent().getStringExtra("amount");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        notes = getIntent().getStringExtra("notes");
        service = getIntent().getStringExtra("serviceId");
        number = getIntent().getStringExtra("number");
        getLocation = getIntent().getStringExtra("getLocationET");
        getDropOffLocation = getIntent().getStringExtra("getDropOffLocation");
        Log.e("TAG", "onCreate: 0"+vehicleMake+model+color+year );
//        payment = getIntent().getStringExtra("payment");
        priceTXT.setText("$" + price);
        bonus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    priceTXT.setText("$" + Double.parseDouble(price));
                }else{
                    priceTXT.setText("$" +(Double.parseDouble(price) + Double.parseDouble(bonus.getText().toString())));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
//            double amount = (Double.parseDouble(price) + Double.parseDouble(bonus.getText().toString())) * 100;
//            Log.e("TAG", "onCreate: "+amount );
            startCheckout();

        });
    }


    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.

//        price = price + bonus.getText().toString();
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        double amount = (Double.parseDouble(price) + Double.parseDouble(bonus.getText().toString())) * 100;

        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        Map<String, Object> address = new HashMap<>();
        Map<String, Object> shipping = new HashMap<>();
//        Map<String, Object> itemList = new ArrayList<>();
        payMap.put("shipping", shipping);
        payMap.put("name", nameET.getText().toString()); //dont change currency in testing phase otherwise it won't work
        payMap.put("currency", "usd"); //dont change currency in testing phase otherwise it won't work
//        itemMap.put("id", "photo_subscription");
        payMap.put("amount", amount);
        payMap.put("line1", addressET.getText().toString());
        payMap.put("postal_code", postalCodeET.getText().toString());
        payMap.put("city", cityET.getText().toString());
        payMap.put("state", "UP");
        payMap.put("country", countryET.getText().toString());


        customProgressBar.showProgress();

//        shipping.put("address", address);
//        payMap.put("items", itemList);
//        itemList.add(itemMap);

        String json = new Gson().toJson(payMap);
        Log.e("TAG", "startCheckout: " + json);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
//                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
//                .readTimeout(5, TimeUnit.MINUTES); // read timeout
//        httpClient = builder.build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(PaymentActivity.this));
        // Hook up the pay button to the card widget and stripe instance

    }

    public static String getJobId() {
        return jobId;
    }

    void createRequestCustomer(PaymentMethodCreateParams params) {
        apiService.createJobRequest(sharedPreferenceMethod.getTokenJWT(),
                sharedPreferenceMethod.getCustomerID(),
                name, number, getLocation, getDropOffLocation, email, service, notes, price,vehicleMake,model,color,year)
                .enqueue(new retrofit2.Callback<JobRequestModel>() {
                    @Override
                    public void onResponse(retrofit2.Call<JobRequestModel> call, retrofit2.Response<JobRequestModel> response) {

                        if (response.body().getResponse().getStatus().equals("Success")) {
                            jobId = response.body().getResponse().getJobId();
                            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                            stripe.confirmPayment(PaymentActivity.this, confirmParams);
                        }

                    }

                    @Override
                    public void onFailure(retrofit2.Call<JobRequestModel> call, Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getMessage());
                        Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");

        CardMultilineWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null && paymentIntentClientSecret != null) {
            createRequestCustomer(params);
        }

        Log.e("TAG", "onPaymentSuccess: " + paymentIntentClientSecret);
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<PaymentActivity> activityRef;

        PayCallback(@NonNull PaymentActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show();
                    Log.e("TAG", "run: " + e.getMessage());
                    activity.customProgressBar.hideProgress();
                }
            });
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show();
                        Log.e("TAG", "run: " + response.toString());
                    }
                });
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<PaymentActivity> activityRef;
        CustomProgressBar customProgressBar;
        ApiService apiService;
        SharedPreferenceMethod sharedPreferenceMethod;
        TextView textView;
        String price = "0.00", payment = "", job = "";
        PaymentActivity paymentActivity;

        PaymentResultCallback(@NonNull PaymentActivity activity) {
            paymentActivity = activity;
            activityRef = new WeakReference<>(activity);
            sharedPreferenceMethod = new SharedPreferenceMethod(activity);
            apiService = ApiUtils.getAPIService();
            customProgressBar = new CustomProgressBar(activity);
            job = getJobId();
            price = activity.getIntent().getStringExtra("amount");
            payment = activity.getIntent().getStringExtra("payment");
            textView = activity.findViewById(R.id.bonusAmountTxt);
        }

        void paymentSaveAPI(String paymentJson) {
            String clientSecret = "", created = "", currency = "", id = "", paymentMethodId = "", status = "";

            try {
                JSONObject jsonObject = new JSONObject(paymentJson);
                clientSecret = jsonObject.getString("clientSecret");
                created = String.valueOf(jsonObject.getLong("created"));
                currency = jsonObject.getString("currency");
                id = jsonObject.getString("id");
                paymentMethodId = jsonObject.getString("paymentMethodId");
                status = jsonObject.getString("status");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("TAG", "paymentSaveAPI: " + job);
            apiService.paymentResponse(sharedPreferenceMethod.getTokenJWT(), job, price,
                    textView.getText().toString(), "Card",
                    clientSecret, created, currency, status, paymentMethodId, id).enqueue(new retrofit2.Callback<AdminRegisterModel>() {
                @Override
                public void onResponse(retrofit2.Call<AdminRegisterModel> call, retrofit2.Response<AdminRegisterModel> response) {
                    if (response.body().getResponse().getStatus().equals("Success")) {
                        Toast.makeText(paymentActivity, "Payment Successful!", Toast.LENGTH_SHORT).show();
                        paymentActivity.startActivity(new Intent(paymentActivity, DashboardCustomerActivity.class));
                        paymentActivity.finishAffinity();
                    }
                    customProgressBar.hideProgress();
                }

                @Override
                public void onFailure(retrofit2.Call<AdminRegisterModel> call, Throwable t) {
                    Log.e("TAG", "onFailure: " + t.getMessage());
                    customProgressBar.hideProgress();
                    Toast.makeText(paymentActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                activity.displayAlert(
//                        "Payment completed",
//                        gson.toJson(paymentIntent)
//                );
                Log.e("TAG", "onSuccess: " + gson.toJson(paymentIntent));
                paymentSaveAPI(gson.toJson(paymentIntent));
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            customProgressBar.hideProgress();
            final PaymentActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }

}