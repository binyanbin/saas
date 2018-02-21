package com.bzw.api.module.base.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetail {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.biz_status_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Integer bizStatusId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.member_ID
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Long memberId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.tenant_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Long tenantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.branch_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Long branchId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.room_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Long roomId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.begin_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Date beginTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.end_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Date endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.type_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Integer typeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.price
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private BigDecimal price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.book_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Date bookTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.order_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Long orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.project_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private String projectName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.technician_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private String technicianName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.branch_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private String branchName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.project_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Integer projectId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.technician_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Long technicianId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.room_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private String roomName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column biz_order_detail.duration
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    private Integer duration;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.id
     *
     * @return the value of biz_order_detail.id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.id
     *
     * @param id the value for biz_order_detail.id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.biz_status_id
     *
     * @return the value of biz_order_detail.biz_status_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Integer getBizStatusId() {
        return bizStatusId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.biz_status_id
     *
     * @param bizStatusId the value for biz_order_detail.biz_status_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setBizStatusId(Integer bizStatusId) {
        this.bizStatusId = bizStatusId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.member_ID
     *
     * @return the value of biz_order_detail.member_ID
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.member_ID
     *
     * @param memberId the value for biz_order_detail.member_ID
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.tenant_id
     *
     * @return the value of biz_order_detail.tenant_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Long getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.tenant_id
     *
     * @param tenantId the value for biz_order_detail.tenant_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.branch_id
     *
     * @return the value of biz_order_detail.branch_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Long getBranchId() {
        return branchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.branch_id
     *
     * @param branchId the value for biz_order_detail.branch_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.room_id
     *
     * @return the value of biz_order_detail.room_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.room_id
     *
     * @param roomId the value for biz_order_detail.room_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.begin_time
     *
     * @return the value of biz_order_detail.begin_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.begin_time
     *
     * @param beginTime the value for biz_order_detail.begin_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.end_time
     *
     * @return the value of biz_order_detail.end_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.end_time
     *
     * @param endTime the value for biz_order_detail.end_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.type_id
     *
     * @return the value of biz_order_detail.type_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.type_id
     *
     * @param typeId the value for biz_order_detail.type_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.price
     *
     * @return the value of biz_order_detail.price
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.price
     *
     * @param price the value for biz_order_detail.price
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.book_time
     *
     * @return the value of biz_order_detail.book_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Date getBookTime() {
        return bookTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.book_time
     *
     * @param bookTime the value for biz_order_detail.book_time
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.order_id
     *
     * @return the value of biz_order_detail.order_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.order_id
     *
     * @param orderId the value for biz_order_detail.order_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.project_name
     *
     * @return the value of biz_order_detail.project_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.project_name
     *
     * @param projectName the value for biz_order_detail.project_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.technician_name
     *
     * @return the value of biz_order_detail.technician_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public String getTechnicianName() {
        return technicianName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.technician_name
     *
     * @param technicianName the value for biz_order_detail.technician_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.branch_name
     *
     * @return the value of biz_order_detail.branch_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.branch_name
     *
     * @param branchName the value for biz_order_detail.branch_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.project_id
     *
     * @return the value of biz_order_detail.project_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.project_id
     *
     * @param projectId the value for biz_order_detail.project_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.technician_id
     *
     * @return the value of biz_order_detail.technician_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Long getTechnicianId() {
        return technicianId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.technician_id
     *
     * @param technicianId the value for biz_order_detail.technician_id
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.room_name
     *
     * @return the value of biz_order_detail.room_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.room_name
     *
     * @param roomName the value for biz_order_detail.room_name
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column biz_order_detail.duration
     *
     * @return the value of biz_order_detail.duration
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column biz_order_detail.duration
     *
     * @param duration the value for biz_order_detail.duration
     *
     * @mbggenerated Sat Jan 20 15:37:01 CST 2018
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}