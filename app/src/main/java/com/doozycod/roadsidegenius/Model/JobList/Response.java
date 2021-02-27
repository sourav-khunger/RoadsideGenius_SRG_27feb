
package com.doozycod.roadsidegenius.Model.JobList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {


    @SerializedName("active")
    @Expose
    private List<Active> active = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("jobs")
    @Expose
    private List<Job> jobs = null;

    public List<Active> getActive() {
        return active;
    }

    public void setActive(List<Active> active) {
        this.active = active;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

}
