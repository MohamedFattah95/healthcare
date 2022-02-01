package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

public class NotificationsModel {

    @SerializedName("id")
    private int id;
    @SerializedName("item_id")
    private int itemId;
    @SerializedName("user_sender_id")
    private int userSenderId;
    @SerializedName("user_sender_name")
    private String userSenderName;
    @SerializedName("user_sender_image")
    private String userSenderImage;
    @SerializedName("user_reciever_id")
    private int userRecieverId;
    @SerializedName("type")
    private int itemType;
    @SerializedName("order_status")
    private int orderStatus;
    @SerializedName("data")
    private String data;
    @SerializedName("title")
    private String title;
    @SerializedName("read_at")
    private String readAt;
    @SerializedName("created_at")
    private String createdAt;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(int userSenderId) {
        this.userSenderId = userSenderId;
    }

    public String getUserSenderName() {
        return userSenderName;
    }

    public void setUserSenderName(String userSenderName) {
        this.userSenderName = userSenderName;
    }

    public String getUserSenderImage() {
        return userSenderImage;
    }

    public void setUserSenderImage(String userSenderImage) {
        this.userSenderImage = userSenderImage;
    }

    public int getUserRecieverId() {
        return userRecieverId;
    }

    public void setUserRecieverId(int userRecieverId) {
        this.userRecieverId = userRecieverId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReadAt() {
        return readAt;
    }

    public void setReadAt(String readAt) {
        this.readAt = readAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
