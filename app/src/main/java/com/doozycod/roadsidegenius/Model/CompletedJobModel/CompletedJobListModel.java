
package com.doozycod.roadsidegenius.Model.CompletedJobModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompletedJobListModel {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
