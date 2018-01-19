package com.bzw.api.module.basic.enums;

/**
 * @author yanbin
 */
public enum OrderDetailState {
    /**
     * 订单明细状态
     */
    booked(1, "已预定"),
    paid(2, "已支付"),
    finished(3,"服务完成"),
    canceled(4,"订单取消"),
    ;

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
