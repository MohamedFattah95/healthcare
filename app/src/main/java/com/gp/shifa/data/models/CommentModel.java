package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

public class CommentModel {

    /**
     * id : 373
     * user_id : 402
     * commentable_id : 382
     * commentable_type : App\User
     * comment : تعليق 1 402
     * rate : 4
     * parent_id : 0
     * main_parent_id : 0
     * childs_count : 0
     * is_active : 1
     * ip :
     * access_user_id : 0
     * created_at : null
     * updated_at : null
     */

    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("commentable_id")
    private int commentableId;
    @SerializedName("commentable_type")
    private String commentableType;
    @SerializedName("comment")
    private String comment;
    @SerializedName("rate")
    private String rate;
    @SerializedName("parent_id")
    private int parentId;
    @SerializedName("main_parent_id")
    private int mainParentId;
    @SerializedName("childs_count")
    private int childsCount;
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
    /**
     * user_title : احمد نبيل
     * user_image : https://aqar.7jazi.com/storage/app/users/407_mg_20210224_104110342.jpg__60361129d4cb6_.jpg
     * mention_user_id :
     * mention_user_title :
     * mention_user_image :
     */

    @SerializedName("user_title")
    private String userTitle;
    @SerializedName("user_image")
    private String userImage;
    @SerializedName("mention_user_id")
    private String mentionUserId;
    @SerializedName("mention_user_title")
    private String mentionUserTitle;
    @SerializedName("mention_user_image")
    private String mentionUserImage;


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

    public int getCommentableId() {
        return commentableId;
    }

    public void setCommentableId(int commentableId) {
        this.commentableId = commentableId;
    }

    public String getCommentableType() {
        return commentableType;
    }

    public void setCommentableType(String commentableType) {
        this.commentableType = commentableType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getMainParentId() {
        return mainParentId;
    }

    public void setMainParentId(int mainParentId) {
        this.mainParentId = mainParentId;
    }

    public int getChildsCount() {
        return childsCount;
    }

    public void setChildsCount(int childsCount) {
        this.childsCount = childsCount;
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

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getMentionUserId() {
        return mentionUserId;
    }

    public void setMentionUserId(String mentionUserId) {
        this.mentionUserId = mentionUserId;
    }

    public String getMentionUserTitle() {
        return mentionUserTitle;
    }

    public void setMentionUserTitle(String mentionUserTitle) {
        this.mentionUserTitle = mentionUserTitle;
    }

    public String getMentionUserImage() {
        return mentionUserImage;
    }

    public void setMentionUserImage(String mentionUserImage) {
        this.mentionUserImage = mentionUserImage;
    }
}
