package com.gp.shifa.data.models;

import java.util.List;

public class SliderModel {

    /**
     * id : 1
     * images : [{"image":"sliders/1_5f86eac6bbb19_.jpg","link":"445"},{"image":"sliders/1_5f86ead3770a5_.jpg","link":"677"}]
     */

    private int id;
    private List<ImagesBean> images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean {
        /**
         * image : sliders/1_5f86eac6bbb19_.jpg
         * link : 445
         */

        private String image;
        private String link;

        public ImagesBean(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
