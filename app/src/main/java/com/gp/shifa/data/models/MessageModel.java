package com.gp.shifa.data.models;

public class MessageModel {

    private String type;
    private int receiverId;
    private int senderId;
    private String message;
    private String time;
    private boolean seen;

    public MessageModel() {
    }

    public MessageModel(String type, int receiverId, int senderId, String message, String time, boolean seen) {
        this.type = type;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.message = message;
        this.time = time;
        this.seen = seen;
    }

    public MessageModel(String type, int senderId, String message, String time) {
        this.type = type;
        this.senderId = senderId;
        this.message = message;
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
