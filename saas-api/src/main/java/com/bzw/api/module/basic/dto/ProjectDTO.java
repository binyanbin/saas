package com.bzw.api.module.basic.dto;

import java.math.BigDecimal;

/**
 * @author yanbin
 */
public class ProjectDTO {
    private String name;
    private Integer id;
    private String typeName;
    private Integer typeId;
    private BigDecimal price;
    private Integer duration;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", typeName='" + typeName + '\'' +
                ", typeId=" + typeId +
                ", price=" + price +
                ", duration=" + duration +
                '}';
    }
}
