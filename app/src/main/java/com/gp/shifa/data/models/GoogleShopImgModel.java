package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GoogleShopImgModel implements Serializable {

    /**
     * height : 960
     * html_attributions : ["<a href=\"https://maps.google.com/maps/contrib/116251732643350001098\">تجهيز صيدليات<\/a>"]
     * photo_reference : CkQ0AAAAA5yzW87q1DBp3Xd9KoME6hJw4MJaAA6qagpUGIhUzNXuDZMTIRDmE7KZbtOe29tBMgSh0FRySTs89l2fdP5emhIQY91jqWNlc4jI-ODQxgF2zxoUYWbKZ2T1sz6EA6FdPcwE-O2wZzs
     * width : 1280
     */

    private int height;
    @SerializedName("photo_reference")
    private String photoReference;
    private int width;
    private List<String> html_attributions;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }
}
