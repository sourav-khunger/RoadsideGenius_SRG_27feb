package com.doozycod.roadsidegenius.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceMethod {
    Context context;
    SharedPreferences sp;
    public static String spUser_id;

    public SharedPreferenceMethod(Context context) {
        this.context = context;
    }

    public void saveUser(String userIdLogin, String admin_name, String admin_email, String phone, String role, String jwt) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("userIdLogin", userIdLogin);
        sp_editior.putString("admin_name", admin_name);
        sp_editior.putString("admin_email", admin_email);
        sp_editior.putString("admin_contact", phone);
        sp_editior.putString("type", role);
        sp_editior.putString("jwt", jwt);
        sp_editior.apply();

    }

    public void setTheme(String themeType) {
        sp = context.getSharedPreferences("roadsideGeniusTHEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("themeType", themeType);
        sp_editior.apply();

    }

    public String getTheme() {
        sp = context.getSharedPreferences("roadsideGeniusTHEME", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("themeType", "");
    }

    public void saveUserType(String userIdLogin) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("userType", userIdLogin);
        sp_editior.apply();
    }

    public void saveLoginPassword(String userEmail, String userPassword) {
        sp = context.getSharedPreferences("roadsideGeniusLOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("userEmail", userEmail);
        sp_editior.putString("userPassword", userPassword);
        sp_editior.apply();
    }

    public void saveDriverLoginPassword(String userEmail, String userPassword) {
        sp = context.getSharedPreferences("roadsideGeniusDriverLOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("userEmail", userEmail);
        sp_editior.putString("userPassword", userPassword);
        sp_editior.apply();
    }

    public String getDriverPassword() {
        sp = context.getSharedPreferences("roadsideGeniusDriverLOGIN", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("userPassword", "");
    }


    public String getDriverEmail() {
        sp = context.getSharedPreferences("roadsideGeniusDriverLOGIN", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("userEmail", "");
    }

    public String getLoginPassword() {
        sp = context.getSharedPreferences("roadsideGeniusLOGIN", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("userPassword", "");
    }

    public String getLoginEmail() {
        sp = context.getSharedPreferences("roadsideGeniusLOGIN", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("userEmail", "");
    }

    public void saveCustomerContact(String CustomerContact) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("CustomerContact", CustomerContact);
        sp_editior.apply();
    }

    public String getCustomerContact() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("CustomerContact", "");
    }

    public String getLogin() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("userType", "");
    }

    public String getFCMToken() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
//        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("fcm", "");
    }

    public void saveDriverId(String saveDriverId) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("saveDriverId", saveDriverId);
        sp_editior.apply();

    }

    public void saveCustomerId(String saveCustomerId) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("saveCustomerId", saveCustomerId);
        sp_editior.apply();

    }

    public void saveToken(String s) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("fcm", s);
        sp_editior.apply();

    }

    public void setTokenJWT(String jwt) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("jwt", jwt);
        sp_editior.apply();

    }

    public void saveDeviceID(String deviceID) {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("deviceID", deviceID);
        sp_editior.apply();

    }

    public String getCustomerID() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("saveCustomerId", "");
    }

    public String getsaveDriverId() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("saveDriverId", "");
    }

    public String getDeviceId() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("deviceID", "");
    }

    public void removeLogin() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.clear().commit();
//        return sp.getString("userIdLogin", "");
    }

    public String getTokenJWT() {
        sp = context.getSharedPreferences("roadsideGenius", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        return sp.getString("jwt", "");

    }
}