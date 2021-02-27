
package com.doozycod.roadsidegenius.Model.ImageListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("imid")
    @Expose
    private String imid;
    @SerializedName("job_id")
    @Expose
    private String jobId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("timestamps_string")
    @Expose
    private String timestampsString;

    public String getImid() {
        return imid;
    }

    public void setImid(String imid) {
        this.imid = imid;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestampsString() {
        return timestampsString;
    }

    public void setTimestampsString(String timestampsString) {
        this.timestampsString = timestampsString;
    }

}
