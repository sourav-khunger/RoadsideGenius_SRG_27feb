
package com.doozycod.roadsidegenius.Model.DriverLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("driver_email")
    @Expose
    private String driverEmail;
    @SerializedName("driver_password")
    @Expose
    private String driverPassword;
    @SerializedName("driver_number")
    @Expose
    private String driverNumber;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("driver_address")
    @Expose
    private String driverAddress;
    @SerializedName("driver_zipcode")
    @Expose
    private String driverZipcode;
    @SerializedName("service_area")
    @Expose
    private String serviceArea;
    @SerializedName("pay_per_job")
    @Expose
    private String payPerJob;
    @SerializedName("service_vehicle_type")
    @Expose
    private String serviceVehicleType;
    @SerializedName("service_vehicle_model")
    @Expose
    private String serviceVehicleModel;
    @SerializedName("service_vehicle_year")
    @Expose
    private String serviceVehicleYear;
    @SerializedName("service_vehicle_make")
    @Expose
    private String serviceVehicleMake;
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

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public void setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverZipcode() {
        return driverZipcode;
    }

    public void setDriverZipcode(String driverZipcode) {
        this.driverZipcode = driverZipcode;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getPayPerJob() {
        return payPerJob;
    }

    public void setPayPerJob(String payPerJob) {
        this.payPerJob = payPerJob;
    }

    public String getServiceVehicleType() {
        return serviceVehicleType;
    }

    public void setServiceVehicleType(String serviceVehicleType) {
        this.serviceVehicleType = serviceVehicleType;
    }

    public String getServiceVehicleModel() {
        return serviceVehicleModel;
    }

    public void setServiceVehicleModel(String serviceVehicleModel) {
        this.serviceVehicleModel = serviceVehicleModel;
    }

    public String getServiceVehicleYear() {
        return serviceVehicleYear;
    }

    public void setServiceVehicleYear(String serviceVehicleYear) {
        this.serviceVehicleYear = serviceVehicleYear;
    }

    public String getServiceVehicleMake() {
        return serviceVehicleMake;
    }

    public void setServiceVehicleMake(String serviceVehicleMake) {
        this.serviceVehicleMake = serviceVehicleMake;
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
