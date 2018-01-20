package com.bzw.api.module.basic.dto;

import java.util.Date;

/**
 * @author yanbin
 */
public class RoomDTO {

    private Long id;
    private String name;
    private String bizStatusName;
    private Integer bizStatusId;
    private Boolean haveRestRoom;
    private Integer typeId;
    private String typeName;
    private Integer bedNumber;
    private Date startTime;
    private Date overTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public Integer getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBizStatusName() {
        return bizStatusName;
    }

    public void setBizStatusName(String bizStatusName) {
        this.bizStatusName = bizStatusName;
    }

    public Integer getBizStatusId() {
        return bizStatusId;
    }

    public void setBizStatusId(Integer bizStatusId) {
        this.bizStatusId = bizStatusId;
    }

    public Boolean isHaveRestRoom() {
        return haveRestRoom;
    }

    public void setHaveRestRoom(Boolean haveRestRoom) {
        this.haveRestRoom = haveRestRoom;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bizStatusName='" + bizStatusName + '\'' +
                ", bizStatusId=" + bizStatusId +
                ", haveRestRoom=" + haveRestRoom +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", bedNumber=" + bedNumber +
                '}';
    }
}
