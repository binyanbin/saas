package com.bzw.api.module.main.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yanbin
 */
public class OrderDTO {
    private String branchName;
    private BigDecimal price;
    private Integer stateId;
    private String stateName;
    private Long id;
    private List<OrderDetailDTO> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetailDTO> details) {
        this.details = details;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "branchName='" + branchName + '\'' +
                ", price=" + price +
                ", stateId=" + stateId +
                ", stateName='" + stateName + '\'' +
                ", id=" + id +
                ", details=" + details +
                '}';
    }
}
