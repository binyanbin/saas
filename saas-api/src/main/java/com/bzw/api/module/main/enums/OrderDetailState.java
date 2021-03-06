package com.bzw.api.module.main.enums;

/**
 * @author yanbin
 */
public enum OrderDetailState {
    /**
     * 订单明细状态
     */
    booked(1, "已预定"),
    Serving(2, "服务中"),
    finished(3, "服务完成"),
    access(4, "服务已评价"),
    cancel(9, "服务取消"),;

    private Integer value;

    private String desc;

    OrderDetailState(Integer value, String desc) {
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

    public static OrderDetailState parse(Integer value) {
        if (null == value) {
            return null;
        }
        OrderDetailState[] coll = values();
        for (OrderDetailState item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
