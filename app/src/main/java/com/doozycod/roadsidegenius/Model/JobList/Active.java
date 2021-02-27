
package com.doozycod.roadsidegenius.Model.JobList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Active implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("job_id")
    @Expose
    private String jobId;
    @SerializedName("start_timestamps")
    @Expose
    private String startTimestamps;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("timestamps")
    @Expose
    private String timestamps;
    @SerializedName("dispatcher_id")
    @Expose
    private String dispatcherId;
    @SerializedName("dispatcher_name")
    @Expose
    private String dispatcherName;
    @SerializedName("dispatch_date")
    @Expose
    private String dispatchDate;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("truck")
    @Expose
    private String truck;
    @SerializedName("dispatcher")
    @Expose
    private String dispatcher;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("eta")
    @Expose
    private String eta;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
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
    @SerializedName("vehicle_make")
    @Expose
    private String vehicleMake;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;
    @SerializedName("dispatched")
    @Expose
    private String dispatched;
    @SerializedName("total_job_time")
    @Expose
    private String totalJobTime;
    @SerializedName("total_miles")
    @Expose
    private String totalMiles;
    @SerializedName("invoice_total")
    @Expose
    private String invoiceTotal;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("comments")
    @Expose
    private String comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStartTimestamps() {
        return startTimestamps;
    }

    public void setStartTimestamps(String startTimestamps) {
        this.startTimestamps = startTimestamps;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }

    public String getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(String dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getDispatched() {
        return dispatched;
    }

    public void setDispatched(String dispatched) {
        this.dispatched = dispatched;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
