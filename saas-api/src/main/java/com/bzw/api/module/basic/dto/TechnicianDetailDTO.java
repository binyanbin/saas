package com.bzw.api.module.basic.dto;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author yanbin
 */
public class TechnicianDetailDTO extends TechnicianDTO {
    private List<String> spa;
    private List<String> massage;
    private List<String> tags;
    private List<String> photos;

    public TechnicianDetailDTO(){
        spa = Lists.newArrayList();
        massage = Lists.newArrayList();
        tags = Lists.newArrayList();
        photos = Lists.newArrayList();
    }

    public List<String> getSpa() {
        return spa;
    }

    public void setSpa(List<String> spa) {
        this.spa = spa;
    }

    public List<String> getMassage() {
        return massage;
    }

    public void setMassage(List<String> massage) {
        this.massage = massage;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "TechnicianDetailDTO{" +
                "spa=" + spa +
                ", massage=" + massage +
                ", tags=" + tags +
                ", photos=" + photos +
                '}';
    }
}
