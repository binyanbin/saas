package com.bzw.api.module.basic.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yanbin
 */
public class OrderDetailDTO {
    private Long roomId;
    private String roomName;
    private Date bookTime;
    private Long technicianId;
    private String technicianName;
    private BigDecimal price;
    private Integer projectId;
    private String projectName;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", bookTime=" + bookTime +
                ", technicianId=" + technicianId +
                ", technicianName='" + technicianName + '\'' +
                ", price=" + price +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
