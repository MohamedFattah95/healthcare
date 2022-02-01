package com.gp.health.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MediaModel implements Serializable {
    /**
     * id : 760
     * file_name : items/403_g.jpg__6045f99aec92d_.jpg
     */

    @SerializedName("id")
    private int id;
    @SerializedName("file_name")
    private String fileName;

    public MediaModel(int id, String path) {
        this.id = id;
        this.fileName = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
