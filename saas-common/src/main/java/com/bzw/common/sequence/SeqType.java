package com.bzw.common.sequence;

/**
 *
 * @author yanbin
 * @date 2017/7/8
 */
public enum SeqType {
    /**
     * 表id序列
     */
    User("pl_user", "userMapper"),
    branch("biz_branch","branchMapper"),
    employee("biz_employee","employeeMapper"),
    order("biz_order","orderMapper"),
    orderDetail("biz_order_detail","orderDetailMapper"),
    parameter("biz_parameter","parameterMapper"),
    project("biz_project","projectMapper"),
    memberRecord("biz_member_record","memberRecordMapper"),
    recordChange("biz_record_change","recordChangeMapper"),
    room("biz_room","roomMapper"),
    roomProject("biz_room_project","roomProjectMapper"),
    tag("biz_tag","tagMapper"),
    technician("biz_technician","technicianMapper"),
    technicianAssess("biz_technician_assess","technicianAssessMapper"),
    technicianPhoto("biz_technician_photo","technicianPhotoMapper"),
    technicianProject("biz_technician_project","technicianProjectMapper"),
    technicianTag("biz_technician_tag","technicianTagMapper"),
    tenant("biz_tenant","tenantMapper"),
    function("pl_function","functionMapper"),
    role("pl_role","roleMapper"),
    roleFunction("pl_role_function","roleFunction"),
    ;

    private String key;
    private String mapper;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    SeqType(String key, String mapper) {
        this.key = key;
        this.mapper = mapper;
    }
}

