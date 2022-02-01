package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GoogleShopModel implements Serializable {

    /**
     * business_status : OPERATIONAL
     * formatted_address : 19212, Cairo Egypt
     * geometry : {"location":{"lat":23.885942,"lng":45.079162},"viewport":{"northeast":{"lat":23.88729182989272,"lng":45.08051182989272},"southwest":{"lat":23.88459217010728,"lng":45.07781217010728}}}
     * icon : https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png
     * id : dbded1fc487c4aa0dc18d49108b45c3bde839f66
     * name : Burger King
     * place_id : ChIJ-_te3LQjKD4RUFIkB7hrZPM
     * plus_code : {"compound_code":"V3PH+9M Quai","global_code":"7HM7V3PH+9M"}
     * rating : 5
     * reference : ChIJ-_te3LQjKD4RUFIkB7hrZPM
     * types : ["restaurant","food","point_of_interest","establishment"]
     * user_ratings_total : 5
     */

    private String business_status;
    private String formatted_address;
    private GeometryBean geometry;
    private String icon;
    private String id;
    private String name;
    @SerializedName("photos")
    private List<GoogleShopImgModel> shopImgList;
    private List<ShopsCategoriesModel> shopCategories;
    private String place_id;
    private PlusCodeBean plus_code;
    private float rating;
    private String reference;
    private int user_ratings_total;
    private List<String> types;

    private String userLat;
    private String userLng;

    private float distance;
    private String shopImg;

    public List<ShopsCategoriesModel> getShopCategories() {
        return shopCategories;
    }

    public void setShopCategories(List<ShopsCategoriesModel> shopCategories) {
        this.shopCategories = shopCategories;

        for (int i = 0; i < shopCategories.size(); i++) {
            for (int j = 0; j < types.size(); j++) {

                if (shopCategories.get(i).getType().equalsIgnoreCase(types.get(j))) {
                    shopImg = shopCategories.get(i).getImage();
                    break;
                }

            }
        }
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public List<GoogleShopImgModel> getShopImgList() {
        return shopImgList;
    }

    public String getShopImgRef() {
        String imgRef = "";
        if (shopImgList.size() > 0) {
            imgRef = shopImgList.get(0).getPhotoReference();
        }
        return imgRef;
    }

    public void setShopImgList(List<GoogleShopImgModel> shopImgList) {
        this.shopImgList = shopImgList;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLng() {
        return userLng;
    }

    public void setUserLng(String userLng) {
        this.userLng = userLng;
    }

    public String getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public PlusCodeBean getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(PlusCodeBean plus_code) {
        this.plus_code = plus_code;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getUser_ratings_total() {
        return user_ratings_total;
    }

    public void setUser_ratings_total(int user_ratings_total) {
        this.user_ratings_total = user_ratings_total;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public static class GeometryBean implements Serializable {
        /**
         * location : {"lat":23.885942,"lng":45.079162}
         * viewport : {"northeast":{"lat":23.88729182989272,"lng":45.08051182989272},"southwest":{"lat":23.88459217010728,"lng":45.07781217010728}}
         */

        private LocationBean location;
        private ViewportBean viewport;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public ViewportBean getViewport() {
            return viewport;
        }

        public void setViewport(ViewportBean viewport) {
            this.viewport = viewport;
        }

        public static class LocationBean implements Serializable {
            /**
             * lat : 23.885942
             * lng : 45.079162
             */

            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        public static class ViewportBean implements Serializable {
            /**
             * northeast : {"lat":23.88729182989272,"lng":45.08051182989272}
             * southwest : {"lat":23.88459217010728,"lng":45.07781217010728}
             */

            private NortheastBean northeast;
            private SouthwestBean southwest;

            public NortheastBean getNortheast() {
                return northeast;
            }

            public void setNortheast(NortheastBean northeast) {
                this.northeast = northeast;
            }

            public SouthwestBean getSouthwest() {
                return southwest;
            }

            public void setSouthwest(SouthwestBean southwest) {
                this.southwest = southwest;
            }

            public static class NortheastBean implements Serializable {
                /**
                 * lat : 23.88729182989272
                 * lng : 45.08051182989272
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class SouthwestBean implements Serializable {
                /**
                 * lat : 23.88459217010728
                 * lng : 45.07781217010728
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }
    }

    public static class PlusCodeBean implements Serializable {
        /**
         * compound_code : V3PH+9M Quai
         * global_code : 7HM7V3PH+9M
         */

        private String compound_code;
        private String global_code;

        public String getCompound_code() {
            return compound_code;
        }

        public void setCompound_code(String compound_code) {
            this.compound_code = compound_code;
        }

        public String getGlobal_code() {
            return global_code;
        }

        public void setGlobal_code(String global_code) {
            this.global_code = global_code;
        }
    }
}
