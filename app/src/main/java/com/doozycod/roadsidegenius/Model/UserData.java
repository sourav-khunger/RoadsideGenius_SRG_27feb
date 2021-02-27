
package com.doozycod.roadsidegenius.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("admin_name")
    @Expose
    private String adminName;
    @SerializedName("admin_email")
    @Expose
    private String adminEmail;
    @SerializedName("admin_contact")
    @Expose
    private String adminContact;
    @SerializedName("admin_password")
    @Expose
    private String adminPassword;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("timestamps")
    @Expose
    private String timestamps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(String adminContact) {
        this.adminContact = adminContact;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }

}
