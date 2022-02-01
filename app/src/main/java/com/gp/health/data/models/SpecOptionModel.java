package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SpecOptionModel implements Serializable {

    /**
     * option_id : 1
     * option_is_required : null
     * option_type : select
     * option_title : اللون
     * option_image : null
     * option_is_active : 1
     * option_value :
     * option_values : [{"option_value_id":1,"option_value_title":"ابيض","option_value_is_active":1,"option_value_parent_id":0},{"option_value_id":1,"option_value_title":"رمادى","option_value_is_active":1,"option_value_parent_id":0}]
     */

    @SerializedName("option_id")
    private int optionId;
    @SerializedName("option_is_required")
    private int optionIsRequired;
    @SerializedName("option_type")
    private String optionType;
    @SerializedName("option_title")
    private String optionTitle;
    @SerializedName("option_image")
    private Object optionImage;
    @SerializedName("option_is_active")
    private int optionIsActive;
    @SerializedName("option_value")
    private String optionValue;
    @SerializedName("option_values")
    private List<SpecOptionValueModel> optionValues;
    @SerializedName("item_option_values")
    private List<SpecOptionValueModel> itemOptionValues;

    private SpecOptionValueModel selectedOptionValue = null;

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getOptionIsRequired() {
        return optionIsRequired;
    }

    public void setOptionIsRequired(int optionIsRequired) {
        this.optionIsRequired = optionIsRequired;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public Object getOptionImage() {
        return optionImage;
    }

    public void setOptionImage(Object optionImage) {
        this.optionImage = optionImage;
    }

    public int getOptionIsActive() {
        return optionIsActive;
    }

    public void setOptionIsActive(int optionIsActive) {
        this.optionIsActive = optionIsActive;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public List<SpecOptionValueModel> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<SpecOptionValueModel> optionValues) {
        this.optionValues = optionValues;
    }

    public List<SpecOptionValueModel> getItemOptionValues() {
        return itemOptionValues;
    }

    public void setItemOptionValues(List<SpecOptionValueModel> itemOptionValues) {
        this.itemOptionValues = itemOptionValues;
    }

    public SpecOptionValueModel getSelectedOptionValue() {
        return selectedOptionValue;
    }

    public void setSelectedOptionValue(SpecOptionValueModel selectedOptionValue) {
        this.selectedOptionValue = selectedOptionValue;
    }
}
