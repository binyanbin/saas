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
    open(3, "开房"),
    waiting(4,"等待"),
    unfinished(5,"未结账"),
    ;

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
