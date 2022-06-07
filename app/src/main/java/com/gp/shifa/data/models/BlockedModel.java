package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

public class BlockedModel {

    /**
     * id : 1
     * created_at : null
     * banned_id : 382
     * banned_name : شركة المروة
     * banned_image : https://aqar.7jazi.com/storage/app/clients/382_ad04b19a76f59ee781c5fd65bc7b345-anonymous-avatar-b__6028ddcd70d66_.png
     * banned_rate : 4.00
     */

    @SerializedName("id")
    private int id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("banned_id")
    private int bannedId;
    @SerializedName("banned_name")
    private String bannedName;
    @SerializedName("banned_image")
    private String bannedImage;
    @SerializedName("banned_rate")
    private String bannedRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getBannedId() {
        return bannedId;
    }

    public void setBannedId(int bannedId) {
        this.bannedId = bannedId;
    }

    public String getBannedName() {
        return bannedName;
    }

    public void setBannedName(String bannedName) {
        this.bannedName = bannedName;
    }

    public String getBannedImage() {
        return bannedImage;
    }

    public void setBannedImage(String bannedImage) {
        this.bannedImage = bannedImage;
    }

    public String getBannedRate() {
        return bannedRate;
    }

    public void setBannedRate(String bannedRate) {
        this.bannedRate = bannedRate;
    }
}
