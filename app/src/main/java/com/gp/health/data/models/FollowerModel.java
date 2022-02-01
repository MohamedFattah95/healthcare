package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

public class FollowerModel {

    /**
     * id : 1
     * created_at : null
     * following_id : 382
     * following_name : شركة المروة
     * following_image : https://aqar.7jazi.com/storage/app/clients/382_ad04b19a76f59ee781c5fd65bc7b345-anonymous-avatar-b__6028ddcd70d66_.png
     * following_rate : 4.00
     * followed_by_me : true
     */

    @SerializedName("id")
    private int id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("following_id")
    private int followingId;
    @SerializedName("following_name")
    private String followingName;
    @SerializedName("following_image")
    private String followingImage;
    @SerializedName("following_rate")
    private String followingRate;
    @SerializedName("followed_by_me")
    private boolean followedByMe;

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

    public int getFollowingId() {
        return followingId;
    }

    public void setFollowingId(int followingId) {
        this.followingId = followingId;
    }

    public String getFollowingName() {
        return followingName;
    }

    public void setFollowingName(String followingName) {
        this.followingName = followingName;
    }

    public String getFollowingImage() {
        return followingImage;
    }

    public void setFollowingImage(String followingImage) {
        this.followingImage = followingImage;
    }

    public float getFollowingRate() {
        if (followingRate != null && !followingRate.isEmpty())
            return Float.parseFloat(followingRate);
        else
            return 0f;
    }

    public void setFollowingRate(String followingRate) {
        this.followingRate = followingRate;
    }

    public boolean isFollowedByMe() {
        return followedByMe;
    }

    public void setFollowedByMe(boolean followedByMe) {
        this.followedByMe = followedByMe;
    }
}
