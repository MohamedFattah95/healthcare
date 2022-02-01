package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityModel {

    /**
     * id : 65
     * title : الحسكه
     * parent : 57
     * services_rates : null
     */

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("parent")
    private int parent;
    @SerializedName("services_rates")
    private List<CityServiceModel> servicesRates;
    @SerializedName("comments")
    private List<CommentModel> comments;
    /**
     * title_general :
     * parent_id : 0
     * rate :
     * rate_count : 0
     * is_active : 1
     */

    @SerializedName("title_general")
    private String titleGeneral;
    @SerializedName("parent_id")
    private int parentId;
    @SerializedName("rate")
    private String rate;
    @SerializedName("rate_count")
    private int rateCount;
    @SerializedName("is_active")
    private int isActive;


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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public List<CityServiceModel> getServicesRates() {
        return servicesRates;
    }

    public void setServicesRates(List<CityServiceModel> servicesRates) {
        this.servicesRates = servicesRates;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public String getTitleGeneral() {
        return titleGeneral;
    }

    public void setTitleGeneral(String titleGeneral) {
        this.titleGeneral = titleGeneral;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getRate() {
        return rate;
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

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
