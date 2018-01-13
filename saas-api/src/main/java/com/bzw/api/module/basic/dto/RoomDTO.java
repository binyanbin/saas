package com.bzw.api.module.basic.dto;

public class RoomDTO {

    private Long id;
    private String name;
    private String stateName;
    private Integer stateId;
    private boolean haveRestRoom;
    private Integer typeId;
    private String typeName;
    private Integer bedNumber;

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public boolean isHaveRestRoom() {
        return haveRestRoom;
    }

    public void setHaveRestRoom(boolean haveRestRoom) {
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
}