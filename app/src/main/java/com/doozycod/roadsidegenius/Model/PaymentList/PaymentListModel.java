
package com.doozycod.roadsidegenius.Model.PaymentList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListModel {

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
