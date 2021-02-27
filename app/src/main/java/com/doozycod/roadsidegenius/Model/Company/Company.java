
package com.doozycod.roadsidegenius.Model.Company;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Company implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_number")
    @Expose
    private String companyNumber;
    @SerializedName("company_email")
    @Expose
    private String companyEmail;
    @SerializedName("company_address")
    @Expose
    private String companyAddress;
    @SerializedName("company_city")
    @Expose
    private String companyCity;
    @SerializedName("company_state")
    @Expose
    private String companyState;
    @SerializedName("company_zipcode")
    @Expose
    private String companyZipcode;
    @SerializedName("primary_phone")
    @Expose
    private String primaryPhone;
    @SerializedName("secondary_phone")
    @Expose
    private String secondaryPhone;
    @SerializedName("primary_service_area")
    @Expose
    private String primaryServiceArea;
    @SerializedName("i_9_forms")
    @Expose
    private String i9Forms;
    @SerializedName("w_9_forms")
    @Expose
    private String w9Forms;
    @SerializedName("coi")
    @Expose
    private String coi;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("timestamps")
    @Expose
    private String timestamps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getCompanyZipcode() {
        return companyZipcode;
    }

    public void setCompanyZipcode(String companyZipcode) {
        this.companyZipcode = companyZipcode;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getPrimaryServiceArea() {
        return primaryServiceArea;
    }

    public void setPrimaryServiceArea(String primaryServiceArea) {
        this.primaryServiceArea = primaryServiceArea;
    }

    public String getI9Forms() {
        return i9Forms;
    }

    public void setI9Forms(String i9Forms) {
        this.i9Forms = i9Forms;
    }

    public String getW9Forms() {
        return w9Forms;
    }

    public void setW9Forms(String w9Forms) {
        this.w9Forms = w9Forms;
    }

    public String getCoi() {
        return coi;
    }

    public void setCoi(String coi) {
        this.coi = coi;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }

}
