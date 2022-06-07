package com.gp.shifa.data.models;

public class ChatRoomModel {
    private int userId;
    private String username;
    private String userImage;
    private String roomId;
    private String messageDate;
    private String message;
    private boolean isLastMsgByYou;
    private boolean seen;

    public ChatRoomModel() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLastMsgByYou() {
        return isLastMsgByYou;
    }

    public void setLastMsgByYou(boolean lastMsgByYou) {
        isLastMsgByYou = lastMsgByYou;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setIsSeen(boolean seen) {
        this.seen = seen;
    }
}
