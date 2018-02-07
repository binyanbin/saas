package com.bzw.api.module.img.dto;

/**
 * @author yanbin
 */
public class ImageDTO {

    private String imageId;
    private String url;

    public ImageDTO(String imageId,String url){
        this.imageId = imageId;
        this.url = url;
    }

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

    @Override
    public String toString() {
        return "ImageDTO{" +
                "imageId='" + imageId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
