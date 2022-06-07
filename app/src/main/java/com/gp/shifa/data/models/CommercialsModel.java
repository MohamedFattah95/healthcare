package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

public class CommercialsModel {

    /**
     * id : 19
     * title : محلات الهدى
     * subscription_package_id : 1
     * mobile : 0565458215
     * lat : 23.885942
     * lng : 45.079163
     * contacts : الاتصال
     * start_date :
     * end_date :
     * logo : commercial_activities/19_.png__6047249a9de4b_.png
     * is_active : 1
     * ip : 156.215.2.43
     * access_user_id :
     * created_at : 2021-03-09T07:32:42.000000Z
     * updated_at : 2021-03-11T08:22:04.000000Z
     */

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("subscription_package_id")
    private int subscriptionPackageId;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("contacts")
    private String contacts;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("logo")
    private String logo;
    @SerializedName("is_active")
    private int isActive;
    @SerializedName("ip")
    private String ip;
    @SerializedName("access_user_id")
    private String accessUserId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSubscriptionPackageId() {
        return subscriptionPackageId;
    }

    public void setSubscriptionPackageId(int subscriptionPackageId) {
        this.subscriptionPackageId = subscriptionPackageId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAccessUserId() {
        return accessUserId;
    }

    public void setAccessUserId(String accessUserId) {
        this.accessUserId = accessUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
