package com.doozycod.roadsidegenius.Service;

public class ApiUtils {
    //      api service for webservices
    private ApiUtils() {
    }

    public static final String BASE_URL = "http://geniusroadsideusa.com/api/";

    public static ApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
