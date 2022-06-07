package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShopsCategoriesModel implements Serializable {


    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private String type;
    @SerializedName("image")
    private String image;


    private boolean isSelected = false;

    @SerializedName("title_general")
    private String titleGeneral;
    @SerializedName("parent_id")
    private int parentId;
    @SerializedName("title")
    private String title;
    @SerializedName("alias")
    private String alias;
    @SerializedName("is_active")
    private int isActive;


    public ShopsCategoriesModel(int id, String title, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
