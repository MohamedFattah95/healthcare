package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GoogleCategoriesModel implements Serializable {

    /**
     * id : 50
     * title : مخبوزات
     * parent : 2
     * slug : bakery
     */

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("parent")
    private int parent;
    @SerializedName("slug")
    private String slug;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
