
package com.doozycod.roadsidegenius.Model.WalletData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendingWithdrawlRequest implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("timestamps")
    @Expose
    private String timestamps;
    @SerializedName("timestamps_string")
    @Expose
    private String timestampsString;
    @SerializedName("status_timestamps_string")
    @Expose
    private String statusTimestampsString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getTimestampsString() {
        return timestampsString;
    }

    public void setTimestampsString(String timestampsString) {
        this.timestampsString = timestampsString;
    }

    public String getStatusTimestampsString() {
        return statusTimestampsString;
    }

    public void setStatusTimestampsString(String statusTimestampsString) {
        this.statusTimestampsString = statusTimestampsString;
    }

}
