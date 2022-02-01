package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoriesModel implements Serializable {

    boolean isSelected = false;
    @SerializedName("title")
    private String title;
    /**
     * id : 1
     * title : عروض الأسبوع
     * parent : 0
     */

    @SerializedName("id")
    private int id;
    @SerializedName("parent")
    private int parent;

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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
