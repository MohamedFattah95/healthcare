package com.gp.shifa.data.models;

import com.google.gson.annotations.SerializedName;
import com.gp.shifa.BuildConfig;

import java.io.Serializable;
import java.util.List;

public class DoctorDetailsModel implements Serializable {

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
    private int rating;
    @SerializedName("is_known")
    private int isKnown;
    @SerializedName("is_recommended")
    private int isRecommended;
    @SerializedName("is_active")
    private int isActive;
    @SerializedName("areas_text")
    private String areasText;
    @SerializedName("count_medicals")
    private int countMedicals;
    @SerializedName("img_src")
    private String imgSrc;
    @SerializedName("detection_min")
    private int detectionMin;
    @SerializedName("medicals")
    private List<MedicalsBean> medicals;
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

    public int getDetectionMin() {
        return detectionMin;
    }

    public void setDetectionMin(int detectionMin) {
        this.detectionMin = detectionMin;
    }

    public List<MedicalsBean> getMedicals() {
        return medicals;
    }

    public void setMedicals(List<MedicalsBean> medicals) {
        this.medicals = medicals;
    }

    public List<MedicalsAreasBean> getMedicalsAreas() {
        return medicalsAreas;
    }

    public void setMedicalsAreas(List<MedicalsAreasBean> medicalsAreas) {
        this.medicalsAreas = medicalsAreas;
    }

    public static class MedicalsBean implements Serializable {
        @SerializedName("id")
        private int id;
        @SerializedName("address")
        private String address;
        @SerializedName("detection_price")
        private String detectionPrice;
        @SerializedName("location")
        private String location;
        @SerializedName("phone")
        private String phone;
        @SerializedName("phone2")
        private String phone2;
        @SerializedName("is_active")
        private String isActive;
        @SerializedName("doctor_id")
        private int doctorId;
        @SerializedName("governorate")
        private GovernorateBean governorate;
        @SerializedName("area")
        private AreaBean area;
        @SerializedName("category")
        private CategoryBean category;
        @SerializedName("services")
        private List<ServicesBean> services;
        @SerializedName("images")
        private List<ImagesBean> images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDetectionPrice() {
            return detectionPrice;
        }

        public void setDetectionPrice(String detectionPrice) {
            this.detectionPrice = detectionPrice;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone2() {
            return phone2;
        }

        public void setPhone2(String phone2) {
            this.phone2 = phone2;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public GovernorateBean getGovernorate() {
            return governorate;
        }

        public void setGovernorate(GovernorateBean governorate) {
            this.governorate = governorate;
        }

        public AreaBean getArea() {
            return area;
        }

        public void setArea(AreaBean area) {
            this.area = area;
        }

        public CategoryBean getCategory() {
            return category;
        }

        public void setCategory(CategoryBean category) {
            this.category = category;
        }

        public List<ServicesBean> getServices() {
            return services;
        }

        public void setServices(List<ServicesBean> services) {
            this.services = services;
        }

        public List<ImagesBean> getImages() {
            return images;
        }

        public void setImages(List<ImagesBean> images) {
            this.images = images;
        }

        public static class GovernorateBean implements Serializable {
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class AreaBean implements Serializable {

            @SerializedName("name")
            private String name;


            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class CategoryBean implements Serializable {
            @SerializedName("id")
            private int id;
            @SerializedName("name")
            private String name;
            @SerializedName("img")
            private String img;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }

        public static class ServicesBean implements Serializable {
            @SerializedName("id")
            private int id;
            @SerializedName("name_ar")
            private String nameAr;
            @SerializedName("name_en")
            private String nameEn;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNameAr() {
                return nameAr;
            }

            public void setNameAr(String nameAr) {
                this.nameAr = nameAr;
            }

            public String getNameEn() {
                return nameEn;
            }

            public void setNameEn(String nameEn) {
                this.nameEn = nameEn;
            }
        }

        public static class ImagesBean implements Serializable {
            @SerializedName("src")
            private String src;

            public ImagesBean(String s) {
                src = s;
            }

            public String getSrc() {
                if (src.contains("http"))
                    return src;

                return BuildConfig.BASE_URL + "assets/web/img/medical/" + src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

        }
    }

    public static class MedicalsAreasBean implements Serializable {
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

        public static class AreaBean implements Serializable {
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
