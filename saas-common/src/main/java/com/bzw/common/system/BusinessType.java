package com.bzw.common.system;

/**
 * @author yanbin
 */
public enum BusinessType {
    /**
     * 业务
     */
    NULL(-1,"无"),
    weChatOrder(1, "微信预定"),
    appOrder(2,"app预定"),
    ;
    private Integer value;
    private String desc;

    BusinessType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static BusinessType parse(Integer value) {
        if (null == value) {
            return null;
        }
        BusinessType[] coll = BusinessType.values();
        for (BusinessType item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }
}
