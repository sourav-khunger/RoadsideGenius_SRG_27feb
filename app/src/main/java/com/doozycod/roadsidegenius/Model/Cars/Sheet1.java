
package com.doozycod.roadsidegenius.Model.Cars;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sheet1 {

    @SerializedName("Vehicle_Make")
    @Expose
    private String vehicleMake;

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

}
