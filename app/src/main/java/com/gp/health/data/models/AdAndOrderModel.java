package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AdAndOrderModel implements Serializable {

    /**
     * id : 394
     * title :
     * type : 2
     * created_at : منذ 0 ساعه و 0 دقيقة
     * updated_at : 2021-03-07T14:36:57.000000Z
     * lat :
     * lng :
     * image : https://aqar.7jazi.com/storage/app
     * price : 0.00
     * price_meter : 0.00
     * space : 0.00
     * description :
     * rate : 0
     * rates_count : 0
     * likes_count : 0
     * views_count : 0
     * shares_count : 0
     * comments_count : 0
     * price_min : 1000
     * price_max : 15000
     * has_images : 0
     * is_liked_by_me : null
     * categories : [{"id":38,"title":"منزل منزل","parent":0}]
     * country : جدة
     * user : {"id":402,"user_type_id":2,"name":"سيد","image":"https://aqar.7jazi.com/storage/app/users/402_52fbe1de1aed766eb57b2f82ed5da63.png__603b48bf85d86_.png","created_at":"2021-02-22T10:10:02.000000Z","rate":"0","rate_count":0}
     * images : ["https://aqar.7jazi.com/storage/app"]
     * options : [{"option_id":7,"option_is_required":0,"option_type":"checkbox","option_title":"مصعد","option_image":null,"option_is_active":1,"option_value":null,"item_option_values":[]}]
     */

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private int type;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;
    @SerializedName("image")
    private String image;
    @SerializedName("price")
    private String price;
    @SerializedName("price_meter")
    private String priceMeter;
    @SerializedName("space")
    private String space;
    @SerializedName("description")
    private String description;
    @SerializedName("rate")
    private String rate;
    @SerializedName("rates_count")
    private int ratesCount;
    @SerializedName("likes_count")
    private int likesCount;
    @SerializedName("views_count")
    private int viewsCount;
    @SerializedName("shares_count")
    private int sharesCount;
    @SerializedName("comments_count")
    private int commentsCount;
    @SerializedName("price_min")
    private String priceMin;
    @SerializedName("price_max")
    private String priceMax;
    @SerializedName("has_images")
    private int hasImages;
    @SerializedName("is_liked_by_me")
    private String isLikedByMe;
    @SerializedName("categories")
    private List<CategoriesModel> categories;
    @SerializedName("country")
    private String country;
    @SerializedName("user")
    private UserModel user;
    @SerializedName("options")
    private List<SpecOptionModel> options;
    @SerializedName("images")
    private List<MediaModel> images;
    @SerializedName("videos")
    private List<MediaModel> videos;
    @SerializedName("country_id")
    private int countryId;
    @SerializedName("woner_relation_id")
    private int wonerRelationId;
    @SerializedName("featured")
    private int featured;
    @SerializedName("woner_perc")
    private String wonerPerc;


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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceMeter() {
        return priceMeter;
    }

    public void setPriceMeter(String priceMeter) {
        this.priceMeter = priceMeter;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getRatesCount() {
        return ratesCount;
    }

    public void setRatesCount(int ratesCount) {
        this.ratesCount = ratesCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getSharesCount() {
        return sharesCount;
    }

    public void setSharesCount(int sharesCount) {
        this.sharesCount = sharesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public int getHasImages() {
        return hasImages;
    }

    public void setHasImages(int hasImages) {
        this.hasImages = hasImages;
    }

    public String getIsLikedByMe() {
        return isLikedByMe;
    }

    public void setIsLikedByMe(String isLikedByMe) {
        this.isLikedByMe = isLikedByMe;
    }

    public List<CategoriesModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesModel> categories) {
        this.categories = categories;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<SpecOptionModel> getOptions() {
        return options;
    }

    public void setOptions(List<SpecOptionModel> options) {
        this.options = options;
    }

    public List<MediaModel> getImages() {
        return images;
    }

    public void setImages(List<MediaModel> images) {
        this.images = images;
    }

    public List<MediaModel> getVideos() {
        return videos;
    }

    public void setVideos(List<MediaModel> videos) {
        this.videos = videos;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getWonerRelationId() {
        return wonerRelationId;
    }

    public void setWonerRelationId(int wonerRelationId) {
        this.wonerRelationId = wonerRelationId;
    }

    public String getWonerPerc() {
        return wonerPerc;
    }

    public void setWonerPerc(String wonerPerc) {
        this.wonerPerc = wonerPerc;
    }

    public int getFeatured() {
        return featured;
    }

    public void setFeatured(int featured) {
        this.featured = featured;
    }
}