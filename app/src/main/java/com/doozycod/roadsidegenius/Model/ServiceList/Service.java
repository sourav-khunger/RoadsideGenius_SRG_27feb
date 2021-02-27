
package com.doozycod.roadsidegenius.Model.ServiceList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Service implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("service_description")
    @Expose
    private String serviceDescription;
    @SerializedName("service_cost")
    @Expose
    private String serviceCost;
    @SerializedName("admin_id")
    @Expose
    private String adminId;

    @SerializedName("service_image")
    @Expose
    private String serviceImage;

    private boolean expanded;

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public Service(String id, String serviceId, String serviceType, String serviceDescription, String serviceCost, String adminId, boolean expanded) {
        this.id = id;
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.serviceDescription = serviceDescription;
        this.serviceCost = serviceCost;
        this.adminId = adminId;
        expanded = false;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

}
