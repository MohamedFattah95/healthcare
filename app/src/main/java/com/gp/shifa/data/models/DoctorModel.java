package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorModel {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("name")
    private String name;
    @SerializedName("img")
    private String img;
    @SerializedName("specialty")
    private String specialty;
    @SerializedName("rating")
    private Integer rating;
    @SerializedName("is_known")
    private Integer isKnown;
    @SerializedName("is_recommended")
    private Integer isRecommended;
    @SerializedName("is_active")
    private Integer isActive;
    @SerializedName("areas_text")
    private String areasText;
    @SerializedName("count_medicals")
    private Integer countMedicals;
    @SerializedName("img_src")
    private String imgSrc;
    @SerializedName("detection_min")
    private String detectionMin;
    @SerializedName("medicals_areas")
    private List<MedicalsAreasBean> medicalsAreas;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getIsKnown() {
        return isKnown;
    }

    public void setIsKnown(int isKnown) {
        this.isKnown = isKnown;
    }

    public int getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(int isRecommended) {
        this.isRecommended = isRecommended;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getAreasText() {
        return areasText;
    }

    public void setAreasText(String areasText) {
        this.areasText = areasText;
    }

    public int getCountMedicals() {
        return countMedicals;
    }

    public void setCountMedicals(int countMedicals) {
        this.countMedicals = countMedicals;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getDetectionMin() {
        return detectionMin;
    }

    public void setDetectionMin(String detectionMin) {
        this.detectionMin = detectionMin;
    }

    public List<MedicalsAreasBean> getMedicalsAreas() {
        return medicalsAreas;
    }

    public void setMedicalsAreas(List<MedicalsAreasBean> medicalsAreas) {
        this.medicalsAreas = medicalsAreas;
    }

    public static class MedicalsAreasBean {
        @SerializedName("id")
        private int id;
        @SerializedName("doctor_id")
        private int doctorId;
        @SerializedName("detection_price")
        private String detectionPrice;
        @SerializedName("area")
        private AreaBean area;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public String getDetectionPrice() {
            return detectionPrice;
        }

        public void setDetectionPrice(String detectionPrice) {
            this.detectionPrice = detectionPrice;
        }

        public AreaBean getArea() {
            return area;
        }

        public void setArea(AreaBean area) {
            this.area = area;
        }

        public static class AreaBean {
            @SerializedName("id")
            private int id;
            @SerializedName("name")
            private String name;

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
        }
    }
}
