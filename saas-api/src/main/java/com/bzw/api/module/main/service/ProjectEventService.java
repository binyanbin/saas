package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Project;
import com.bzw.api.module.main.biz.ProjectEventBiz;
import com.bzw.api.module.main.biz.ProjectQueryBiz;
import com.bzw.api.module.main.dto.ProjectDTO;
import com.bzw.api.module.main.params.ProjectParam;
import com.bzw.common.enums.Status;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class ProjectEventService {

    @Autowired
    private ProjectEventBiz projectEventBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private ProjectQueryService projectQueryService;

    public ProjectDTO add(ProjectParam projectParam, Long tenantId, Long branchId, String branchName){
        Project project = new Project();
        project.setType(projectParam.getType());
        project.setBranchId(branchId);
        project.setBranchName(branchName);
        project.setTenantId(tenantId);
        project.setId(sequenceService.newKey(SeqType.project).intValue());
        project.setName(projectParam.getName());
        project.setPrice(projectParam.getPrice());
        project.setStatusId(Status.Valid.getValue());
        project.setDuration(projectParam.getDuration());
        project.setDescription(projectParam.getDescription());
        projectEventBiz.add(project);
        return projectQueryService.mapToProjectDto(project);
    }

    public boolean update(ProjectParam projectParam,Integer id){
        Project project = projectQueryBiz.getProject(id);
        if (project.getStatusId().equals(Status.Delete.getValue())){
            return false;
        }
        project.setId(id);
        project.setType(projectParam.getType());
        project.setName(projectParam.getName());
        project.setPrice(projectParam.getPrice());
        project.setDuration(projectParam.getDuration());
        project.setDescription(projectParam.getDescription());
        return projectEventBiz.update(project);
    }

    public boolean delete(Integer id){
        return projectEventBiz.delete(id);
    }
}
