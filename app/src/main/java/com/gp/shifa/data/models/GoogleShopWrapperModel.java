package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleShopWrapperModel {
    @SerializedName("next_page_token") private String nextPageToken;
    @SerializedName("status") private String status;
    @SerializedName("error_message") private String errorMsg;
    @SerializedName("results") private List<GoogleShopModel> shopsList;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GoogleShopModel> getShopsList() {
        return shopsList;
    }

    public void setShopsList(List<GoogleShopModel> shopsList) {
        this.shopsList = shopsList;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
