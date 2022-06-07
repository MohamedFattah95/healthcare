package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountriesAndAreasModel {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private String code;
    @SerializedName("deleted_at")
    private Object deletedAt;
    @SerializedName("areas")
    private List<AreasBean> areas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<AreasBean> getAreas() {
        return areas;
    }

    public void setAreas(List<AreasBean> areas) {
        this.areas = areas;
    }

    public static class AreasBean {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("sort")
        private int sort;
        @SerializedName("governorate_id")
        private int governorateId;
        @SerializedName("deleted_at")
        private Object deletedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getGovernorateId() {
            return governorateId;
        }

        public void setGovernorateId(int governorateId) {
            this.governorateId = governorateId;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }
    }
}
