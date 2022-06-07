package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("user")
    private UserBean user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Serializable {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("phone")
        private String phone;
        @SerializedName("phone2")
        private String phone2;
        @SerializedName("date")
        private String date;
        @SerializedName("gander")
        private String gander;
        @SerializedName("img")
        private String img;
        @SerializedName("is_availability_diagnostics")
        private int isAvailabilityDiagnostics;
        @SerializedName("governorate_id")
        private int governorateId;
        @SerializedName("area_id")
        private int areaId;
        @SerializedName("email_verified_at")
        private Object emailVerifiedAt;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("img_src")
        private String imgSrc;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone2() {
            return phone2;
        }

        public void setPhone2(String phone2) {
            this.phone2 = phone2;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getGander() {
            return gander;
        }

        public void setGander(String gander) {
            this.gander = gander;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getIsAvailabilityDiagnostics() {
            return isAvailabilityDiagnostics;
        }

        public void setIsAvailabilityDiagnostics(int isAvailabilityDiagnostics) {
            this.isAvailabilityDiagnostics = isAvailabilityDiagnostics;
        }

        public int getGovernorateId() {
            return governorateId;
        }

        public void setGovernorateId(int governorateId) {
            this.governorateId = governorateId;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
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

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }
    }
}
