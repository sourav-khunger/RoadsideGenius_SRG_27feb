
package com.doozycod.roadsidegenius.Model.Notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("is_read")
    @Expose
    private String isRead;
    @SerializedName("timestamps")
    @Expose
    private String timestamps;
    @SerializedName("timestamps_string")
    @Expose
    private String timestampsString;


    private boolean expanded;

    public Notification(String id, String userId, String userType, String title, Data data, String isRead, String timestamps, String timestampsString, boolean expanded) {
        this.id = id;
        this.userId = userId;
        this.userType = userType;
        this.title = title;
        this.data = data;
        this.isRead = isRead;
        this.timestamps = timestamps;
        this.timestampsString = timestampsString;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
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
