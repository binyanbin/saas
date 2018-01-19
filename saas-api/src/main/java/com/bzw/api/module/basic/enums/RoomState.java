package com.bzw.api.module.basic.enums;

/**
 * @author yanbin
 */

public enum RoomState {
    /**
     * 房间状态
     */
    free(1, "空闲"),
    booked(2, "预定"),
    using(3, "使用中"),
    serving(4,"服务中"),
    resting(5,"休息中"),
    cleaning(6,"清理中"),
    pause(7,"暂停使用");

    private Integer value;

    private String desc;

    private RoomState(Integer value, String desc) {
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

    public static RoomState parse(Integer value) {
        if (null == value) {
            return null;
        }
        RoomState[] coll = values();
        for (RoomState item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
