package com.bzw.api.module.basic.enums;

/**
 * @author yanbin
 */

public enum OrderType {
    /**
     * 订单类型
     */
    CustomerReservation(1, "客户预定"),
    BookForCustomers(2, "帮客户预定"),
    ;

    private Integer value;

    private String desc;

    OrderType(Integer value, String desc) {
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

    public static OrderType parse(Integer value) {
        if (null == value) {
            return null;
        }
        OrderType[] coll = values();
        for (OrderType item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
