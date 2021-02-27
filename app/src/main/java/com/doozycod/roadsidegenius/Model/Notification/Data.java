
package com.doozycod.roadsidegenius.Model.Notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("job_id")
    @Expose
    private String jobId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("customer_number")
    @Expose
    private String customerNumber;
    @SerializedName("customer_pickup")
    @Expose
    private String customerPickup;
    @SerializedName("customer_dropoff")
    @Expose
    private String customerDropoff;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("amount_quoted")
    @Expose
    private String amountQuoted;
    @SerializedName("dispatch_date")
    @Expose
    private String dispatchDate;
    @SerializedName("truck")
    @Expose
    private String truck;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("eta")
    @Expose
    private String eta;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("vehicle_make")
    @Expose
    private String vehicleMake;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;
    @SerializedName("dipatch")
    @Expose
    private String dipatch;
    @SerializedName("total_job_time")
    @Expose
    private String totalJobTime;
    @SerializedName("total_miles")
    @Expose
    private String totalMiles;
    @SerializedName("invoice_total")
    @Expose
    private String invoiceTotal;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerPickup() {
        return customerPickup;
    }

    public void setCustomerPickup(String customerPickup) {
        this.customerPickup = customerPickup;
    }

    public String getCustomerDropoff() {
        return customerDropoff;
    }

    public void setCustomerDropoff(String customerDropoff) {
        this.customerDropoff = customerDropoff;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getAmountQuoted() {
        return amountQuoted;
    }

    public void setAmountQuoted(String amountQuoted) {
        this.amountQuoted = amountQuoted;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getDipatch() {
        return dipatch;
    }

    public void setDipatch(String dipatch) {
        this.dipatch = dipatch;
    }

    public String getTotalJobTime() {
        return totalJobTime;
    }

    public void setTotalJobTime(String totalJobTime) {
        this.totalJobTime = totalJobTime;
    }

    public String getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(String totalMiles) {
        this.totalMiles = totalMiles;
    }

    public String getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(String invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
