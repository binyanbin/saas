package com.bzw.api.module.main.enums;

public enum FunctionId {

    /**
     * 权限
     */
    roomManage(1, "房间管理"),
    ;
    private Integer value;

    private String desc;

    FunctionId(Integer value, String desc) {
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

    public static FunctionId parse(Integer value) {
        if (null == value) {
            return null;
        }
        FunctionId[] coll = values();
        for (FunctionId item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
