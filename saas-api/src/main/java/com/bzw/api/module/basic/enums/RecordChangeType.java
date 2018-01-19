package com.bzw.api.module.basic.enums;

/**
 * @author yanbin
 */

public enum  RecordChangeType {
    /**
     * 业务记录类型
     */
    room(1, "biz_room"),
    order(2, "biz_order"),
    technician(3,"biz_technician"),
            ;

    private Integer value;

    private String desc;

    private RecordChangeType(Integer value, String desc) {
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

    public static RecordChangeType parse(Integer value) {
        if (null == value) {
            return null;
        }
        RecordChangeType[] coll = values();
        for (RecordChangeType item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
