package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

public class ChatsModel {

    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("medical_id")
    private int medicalId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    private String roomId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
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

    public String getRoomId() {
        if (userId < medicalId) {
            roomId = userId + "," + medicalId;
        } else {
            roomId = medicalId + "," + userId;
        }
        return roomId;
    }
}
