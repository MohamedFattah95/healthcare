package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("user_type_id")
    private int userTypeId;
    @SerializedName("grade")
    private int grade;
    @SerializedName("code")
    private String code;
    @SerializedName("verification_code")
    private int verificationCode;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("ip")
    private String ip;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("image")
    private String image;
    @SerializedName("rate")
    private String rate;
    @SerializedName("rate_count")
    private int rateCount;
    @SerializedName("description")
    private String description;
    @SerializedName("followers_count")
    private int followersCount;
    @SerializedName("followings_count")
    private int followingsCount;
    @SerializedName("unseen_notifications_count")
    private int unseenNotificationsCount;
    @SerializedName("comment")
    private CommentModel comment;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getRate() {
        if (rate != null && !rate.isEmpty())
            return Float.parseFloat(rate);
        else
            return 0f;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    public void setFollowingsCount(int followingsCount) {
        this.followingsCount = followingsCount;
    }

    public int getUnseenNotificationsCount() {
        return unseenNotificationsCount;
    }

    public void setUnseenNotificationsCount(int unseenNotificationsCount) {
        this.unseenNotificationsCount = unseenNotificationsCount;
    }

    public CommentModel getComment() {
        return comment;
    }

    public void setComment(CommentModel comment) {
        this.comment = comment;
    }

}
