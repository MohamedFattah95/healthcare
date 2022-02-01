package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

public class CityServiceModel {

    /**
     * country_service_id : 1
     * sum_country_services_rate : 170
     * rate : 56
     * title : الكهرباء
     * country_service : {"id":1,"title":{"ar":"الكهرباء","en":"Electric"},"sort":"4","is_active":1}
     */

    @SerializedName("country_service_id")
    private int countryServiceId;
    @SerializedName("sum_country_services_rate")
    private String sumCountryServicesRate;
    @SerializedName("rate")
    private int rate;
    @SerializedName("title")
    private String title;
    @SerializedName("country_service")
    private CountryServiceBean countryService;
    /**
     * id : 1
     * sort : 4
     * is_active : 1
     */

    @SerializedName("id")
    private int id;
    @SerializedName("sort")
    private int sort;
    @SerializedName("is_active")
    private int isActive;


    public int getCountryServiceId() {
        return countryServiceId;
    }

    public void setCountryServiceId(int countryServiceId) {
        this.countryServiceId = countryServiceId;
    }

    public String getSumCountryServicesRate() {
        return sumCountryServicesRate;
    }

    public void setSumCountryServicesRate(String sumCountryServicesRate) {
        this.sumCountryServicesRate = sumCountryServicesRate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CountryServiceBean getCountryService() {
        return countryService;
    }

    public void setCountryService(CountryServiceBean countryService) {
        this.countryService = countryService;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public static class CountryServiceBean {
        /**
         * id : 1
         * title : {"ar":"الكهرباء","en":"Electric"}
         * sort : 4
         * is_active : 1
         */

        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private TitleBean title;
        @SerializedName("sort")
        private String sort;
        @SerializedName("is_active")
        private int isActive;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public int getIsActive() {
            return isActive;
        }

        public void setIsActive(int isActive) {
            this.isActive = isActive;
        }

        public static class TitleBean {
            /**
             * ar : الكهرباء
             * en : Electric
             */

            @SerializedName("ar")
            private String ar;
            @SerializedName("en")
            private String en;

            public String getAr() {
                return ar;
            }

            public void setAr(String ar) {
                this.ar = ar;
            }

            public String getEn() {
                return en;
            }

            public void setEn(String en) {
                this.en = en;
            }
        }
    }
}
