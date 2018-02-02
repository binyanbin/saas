package com.bzw.api.module.img.enums;

public enum  Type {
    NoType(0, "无分类"),
            ;

    private Integer value;

    private String desc;

    Type(Integer value, String desc) {
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

    public static Type parse(Integer value) {
        if (null == value) {
            return null;
        }
        Type[] coll = values();
        for (Type item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
