
package com.doozycod.roadsidegenius.Model.WalletData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletData implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("owner_id")
    @Expose
    private String ownerId;
    @SerializedName("opening_balance")
    @Expose
    private String openingBalance;
    @SerializedName("cr_amount")
    @Expose
    private String crAmount;
    @SerializedName("db_amount")
    @Expose
    private String dbAmount;
    @SerializedName("closing_balance")
    @Expose
    private String closingBalance;
    @SerializedName("timestamps")
    @Expose
    private String timestamps;
    @SerializedName("timestamps_string")
    @Expose
    private String timestampsString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getCrAmount() {
        return crAmount;
    }

    public void setCrAmount(String crAmount) {
        this.crAmount = crAmount;
    }

    public String getDbAmount() {
        return dbAmount;
    }

    public void setDbAmount(String dbAmount) {
        this.dbAmount = dbAmount;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
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

}
