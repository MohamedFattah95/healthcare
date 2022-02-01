package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

public class MemberTypeModel {

    /**
     * id : 2
     * title : باحث عن عقار
     * alias : search
     * for_admin : 0
     * for_client : 1
     * for_customer : 0
     * sort : 1
     * is_active : 1
     */

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("alias")
    private String alias;
    @SerializedName("for_admin")
    private int forAdmin;
    @SerializedName("for_client")
    private int forClient;
    @SerializedName("for_customer")
    private int forCustomer;
    @SerializedName("sort")
    private int sort;
    @SerializedName("is_active")
    private int isActive;
    @SerializedName("is_default")
    private int isDefault;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getForAdmin() {
        return forAdmin;
    }

    public void setForAdmin(int forAdmin) {
        this.forAdmin = forAdmin;
    }

    public int getForClient() {
        return forClient;
    }

    public void setForClient(int forClient) {
        this.forClient = forClient;
    }

    public int getForCustomer() {
        return forCustomer;
    }

    public void setForCustomer(int forCustomer) {
        this.forCustomer = forCustomer;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
