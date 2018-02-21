package com.bzw.api.module.main.dto;

import java.util.List;

public class UserDTO {

    private Long id;
    private String name;
    private String phone;
    private String sex;
    private List<IdName> functions;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<IdName> getFunctions() {
        return functions;
    }

    public void setFunctions(List<IdName> functions) {
        this.functions = functions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
