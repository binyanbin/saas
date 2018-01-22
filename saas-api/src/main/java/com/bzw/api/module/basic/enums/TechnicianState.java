package com.bzw.api.module.basic.enums;

/**
 * @author yanbin
 */

public enum TechnicianState {
    /**
     * 技师状态
     */
    free(1, "空闲"),
    booked(2, "预定"),
    serving(3,"服务中"),
    servingAndBooked(4,"服务中并预定"),
    vcation(5,"休假"),;

    private Integer value;

    private String desc;

    private TechnicianState(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static TechnicianState parse(Integer value) {
        if (null == value) {
            return null;
        }
        TechnicianState[] coll = values();
        for (TechnicianState item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
