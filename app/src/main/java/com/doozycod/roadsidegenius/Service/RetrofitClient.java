package com.doozycod.roadsidegenius.Service;


import com.doozycod.roadsidegenius.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static Retrofit.Builder retrofitBuilder = null;
    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS);

    public static Retrofit getClient(String baseUrl) {
        Retrofit retrofit = null;
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(logging);
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build());
        }
        retrofit = retrofitBuilder.build();
        return retrofit;
    }
}


