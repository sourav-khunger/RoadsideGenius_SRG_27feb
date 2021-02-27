
package com.doozycod.roadsidegenius.Model.WithdrawList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pending")
    @Expose
    private List<Pending> pending = null;
    @SerializedName("cancelled")
    @Expose
    private List<Approved> cancelled = null;
    @SerializedName("approved")
    @Expose
    private List<Approved> approved = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Pending> getPending() {
        return pending;
    }

    public void setPending(List<Pending> pending) {
        this.pending = pending;
    }

    public List<Approved> getCancelled() {
        return cancelled;
    }

    public void setCancelled(List<Approved> cancelled) {
        this.cancelled = cancelled;
    }

    public List<Approved> getApproved() {
        return approved;
    }

    public void setApproved(List<Approved> approved) {
        this.approved = approved;
    }
}
