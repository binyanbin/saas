package com.bzw.api.module.main.params;

public class RoomParam {
    private Integer bedNumber;
    private String name;
    private Integer type;
    private Boolean haveRestroom;
    private String description;

    public Integer getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getHaveRestroom() {
        return haveRestroom;
    }

    public void setHaveRestroom(Boolean haveRestroom) {
        this.haveRestroom = haveRestroom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
