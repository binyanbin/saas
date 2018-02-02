package com.bzw.api.module.main.enums;

/**
 * @author yanbin
 */

public enum ProjectType {
    /**
     * 项目类型
     */
    spa(1, "spa"),
    footMassage(2, "足浴"),
    ;

    private Integer value;

    private String desc;

    private ProjectType(Integer value, String desc) {
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

    public static ProjectType parse(Integer value) {
        if (null == value) {
            return null;
        }
        ProjectType[] coll = values();
        for (ProjectType item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
