package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpecOptionValueModel implements Serializable {

    /**
     * option_value_id : 1
     * option_value_title : ابيض
     * option_value_is_active : 1
     * option_value_parent_id : 0
     */

    @SerializedName("option_id")
    private int parentId;
    @SerializedName("option_value_id")
    private int optionValueId;
    @SerializedName("option_value_title")
    private String optionValueTitle;
    @SerializedName("option_value_is_active")
    private int optionValueIsActive;
    @SerializedName("option_value_parent_id")
    private int optionValueParentId;
    @SerializedName("option_value_image")
    private String optionValueImage;

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getOptionValueId() {
        return optionValueId;
    }

    public void setOptionValueId(int optionValueId) {
        this.optionValueId = optionValueId;
    }

    public String getOptionValueTitle() {
        return optionValueTitle;
    }

    public void setOptionValueTitle(String optionValueTitle) {
        this.optionValueTitle = optionValueTitle;
    }

    public int getOptionValueIsActive() {
        return optionValueIsActive;
    }

    public void setOptionValueIsActive(int optionValueIsActive) {
        this.optionValueIsActive = optionValueIsActive;
    }

    public int getOptionValueParentId() {
        return optionValueParentId;
    }

    public void setOptionValueParentId(int optionValueParentId) {
        this.optionValueParentId = optionValueParentId;
    }

    public String getOptionValueImage() {
        return optionValueImage;
    }

    public void setOptionValueImage(String optionValueImage) {
        this.optionValueImage = optionValueImage;
    }
}
