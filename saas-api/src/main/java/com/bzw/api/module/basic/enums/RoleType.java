package com.bzw.api.module.basic.enums;

/**
 * @author yanbin
 */

public enum RoleType {
    /**
     * 系统角色
     */
    reception(1, "收银"),
    service(2, "接待"),
    technician(3,"技师"),
    manager(4,"经理"),
    admin(5,"管理员"),
    customer(10,"客户"),
    ;

    private Integer value;

    private String desc;

    private RoleType(Integer value, String desc) {
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

    public static RoleType parse(Integer value) {
        if (null == value) {
            return null;
        }
        RoleType[] coll = values();
        for (RoleType item : coll) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
