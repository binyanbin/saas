package com.bzw.common.enums;

public enum  Status {
    Valid(1, "启用"),
    Invalid(0, "禁用"),
    Delete(2, "删除"),;

    private Integer value;

    private String desc;

    private Status(Integer value, String desc) {
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

    public static Status parse(Integer value) {
        if (null == value) {
            return null;
        }
        Status[] coll = values();
        for (Status item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
