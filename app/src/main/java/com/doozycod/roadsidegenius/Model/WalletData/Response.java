
package com.doozycod.roadsidegenius.Model.WalletData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("wallet_data")
    @Expose
    private WalletData walletData;

//    @SerializedName("pending_withdrawl_request")
//    @Expose
//    private PendingWithdrawlRequest pendingWithdrawlRequest;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WalletData getWalletData() {
        return walletData;
    }

    public void setWalletData(WalletData walletData) {
        this.walletData = walletData;
    }

//    public PendingWithdrawlRequest getPendingWithdrawlRequest() {
//        return pendingWithdrawlRequest;
//    }
//
//    public void setPendingWithdrawlRequest(PendingWithdrawlRequest pendingWithdrawlRequest) {
//        this.pendingWithdrawlRequest = pendingWithdrawlRequest;
//    }

}
