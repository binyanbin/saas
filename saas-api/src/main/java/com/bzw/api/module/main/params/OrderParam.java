package com.bzw.api.module.main.params;

/**
 * @author yanbin
 */
public class OrderParam {
    private Long technicianId;
    private Integer projectId;

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
