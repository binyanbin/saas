package com.bzw.api.module.main.dto;

import java.util.List;

public class RoleDTO {
    private String name;
    private String description;
    private Integer id;
    private List<IdName> functions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<IdName> getFunctions() {
        return functions;
    }

    public void setFunctions(List<IdName> functions) {
        this.functions = functions;
    }
}
