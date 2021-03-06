package com.bzw.api.module.main.dto;

/**
 * @author yanbin
 */
public class TechnicianPhotoDTO {

    private String imageId;
    private String url;
    private String name;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TechnicianPhotoDTO{" +
                "imageId='" + imageId + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
