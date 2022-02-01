package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

public class BanksModel {

    /**
     * id : 7
     * woner_name : احمد حسن
     * bank_title : cib
     * account_no : 123456
     * ibn : 123456789
     * is_active : 1
     * ip : 41.44.165.145
     * access_user_id : 1
     * created_at : null
     * updated_at : null
     */

    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String image;
    @SerializedName("woner_name")
    private String wonerName;
    @SerializedName("bank_title")
    private String bankTitle;
    @SerializedName("account_no")
    private String accountNo;
    @SerializedName("ibn")
    private String ibn;
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


    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWonerName() {
        return wonerName;
    }

    public void setWonerName(String wonerName) {
        this.wonerName = wonerName;
    }

    public String getBankTitle() {
        return bankTitle;
    }

    public void setBankTitle(String bankTitle) {
        this.bankTitle = bankTitle;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIbn() {
        return ibn;
    }

    public void setIbn(String ibn) {
        this.ibn = ibn;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
