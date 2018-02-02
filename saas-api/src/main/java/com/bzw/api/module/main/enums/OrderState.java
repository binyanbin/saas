package com.bzw.api.module.main.enums;

/**
 * @author yanbin
 */

public enum OrderState {
    /**
     * 订单状态
     */
    non_payment(1, "未支付"),
    paid(2, "已支付"),
    cancel(3,"已取消"),
    finish(4,"已完成")
    ;

    private Integer value;

    private String desc;

    OrderState(Integer value, String desc) {
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

    public static OrderState parse(Integer value) {
        if (null == value) {
            return null;
        }
        OrderState[] coll = values();
        for (OrderState item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
