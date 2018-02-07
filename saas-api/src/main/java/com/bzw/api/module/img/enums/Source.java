package com.bzw.api.module.img.enums;

/**
 * @author yanbin
 */
public enum Source {
    /**
     * 来源
     */
    api(1, "api"),
    ;

    private Integer value;

    private String desc;

    Source(Integer value, String desc) {
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

    public static Source parse(Integer value) {
        if (null == value) {
            return null;
        }
        Source[] coll = values();
        for (Source item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
