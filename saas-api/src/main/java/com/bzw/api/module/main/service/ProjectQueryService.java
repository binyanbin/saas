package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Project;
import com.bzw.api.module.main.biz.ProjectQueryBiz;
import com.bzw.api.module.main.dto.IdName;
import com.bzw.api.module.main.dto.ProjectDTO;
import com.bzw.api.module.main.enums.ProjectType;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class ProjectQueryService {

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    public List<ProjectDTO> listProjectsByBranchId(Long branchId) {
        List<Project> projectList = projectQueryBiz.listProjectByBranchId(branchId);
        return mapToProjectDtoList(projectList);
    }

    public ProjectDTO getProject(Integer projectId) {
        return mapToProjectDto(projectQueryBiz.getProject(projectId));
    }

    public List<IdName> listType(){
        List<IdName> result = Lists.newArrayList();
        ProjectType[] types = ProjectType.values();
        for(ProjectType projectType : types){
            IdName idName = new IdName();
            idName.setId(projectType.getValue());
            idName.setName(projectType.getDesc());
            result.add(idName);
        }
        return result;
    }

    public List<ProjectDTO> listProjectsByRoomId(Long roomId) {
        List<Project> projectList = projectQueryBiz.listProjectsByRoomId(roomId);
        return mapToProjectDtoList(projectList);
    }

    private List<ProjectDTO> mapToProjectDtoList(List<Project> projectList) {
        List<ProjectDTO> result = Lists.newArrayList();
        for (Project project : projectList) {
            result.add(mapToProjectDto(project));
        }
        return result;
    }

    public ProjectDTO mapToProjectDto(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setTypeName(ProjectType.parse(project.getType()).getDesc());
        projectDTO.setTypeId(project.getType());
        projectDTO.setPrice(project.getPrice());
        projectDTO.setDuration(project.getDuration());
        projectDTO.setDescription(project.getDescription());
        return projectDTO;
    }

}
