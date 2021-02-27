
package com.doozycod.roadsidegenius.Model.AdminJobListModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("completed_jobs")
    @Expose
    private List<CompletedJob> completedJobs = null;
    @SerializedName("unassigned_jobs")
    @Expose
    private List<Object> unassignedJobs = null;
    @SerializedName("cancelled_jobs")
    @Expose
    private List<CancelledJob> cancelledJobs = null;
    @SerializedName("gone_all_arrival_jobs")
    @Expose
    private List<GoneAllArrivalJob> goneAllArrivalJobs = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CompletedJob> getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(List<CompletedJob> completedJobs) {
        this.completedJobs = completedJobs;
    }

    public List<Object> getUnassignedJobs() {
        return unassignedJobs;
    }

    public void setUnassignedJobs(List<Object> unassignedJobs) {
        this.unassignedJobs = unassignedJobs;
    }

    public List<CancelledJob> getCancelledJobs() {
        return cancelledJobs;
    }

    public void setCancelledJobs(List<CancelledJob> cancelledJobs) {
        this.cancelledJobs = cancelledJobs;
    }

    public List<GoneAllArrivalJob> getGoneAllArrivalJobs() {
        return goneAllArrivalJobs;
    }

    public void setGoneAllArrivalJobs(List<GoneAllArrivalJob> goneAllArrivalJobs) {
        this.goneAllArrivalJobs = goneAllArrivalJobs;
    }

}
