package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

public class SubscriptionPackagesModel {

    /**
     * id : 1
     * title : اشتراك سنه
     * duration : 365
     * is_active : 1
     * ip :
     * access_user_id : 0
     * created_at :
     * updated_at :
     */

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("duration")
    private int duration;
    @SerializedName("is_active")
    private int isActive;
    @SerializedName("ip")
    private String ip;
    @SerializedName("access_user_id")
    private int accessUserId;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public int getAccessUserId() {
        return accessUserId;
    }

    public void setAccessUserId(int accessUserId) {
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
