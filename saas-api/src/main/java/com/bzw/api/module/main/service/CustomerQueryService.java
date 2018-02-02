package com.bzw.api.module.main.service;

import com.bzw.api.module.main.biz.ProjectQueryBiz;
import com.bzw.api.module.main.biz.RoomQueryBiz;
import com.bzw.api.module.main.biz.UserQueryBiz;
import com.bzw.api.module.main.dto.BranchDTO;
import com.bzw.api.module.main.dto.ProjectDTO;
import com.bzw.api.module.main.enums.ProjectType;
import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.base.model.Project;
import com.bzw.api.module.base.model.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class CustomerQueryService {

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    @Autowired
    private UserQueryBiz userQueryBiz;

    public BranchDTO getBranchByRoomId(Long roomId) {
        Branch branch = roomQueryBiz.getBranchByRoomId(roomId);
        if (branch == null) {
            return null;
        } else {
            BranchDTO result = new BranchDTO();
            result.setId(branch.getId());
            result.setAddress(branch.getAddress());
            result.setName(branch.getName());
            result.setTelephone(branch.getTelephone());
            return result;
        }
    }

    public Boolean containPhone(String phone) {
        List<User> users = userQueryBiz.listUserByPhone(phone);
        return !CollectionUtils.isEmpty(users);
    }

    public List<ProjectDTO> listProjectsByBranchId(Long branchId) {
        List<Project> projectList = projectQueryBiz.listProjectByBranchId(branchId);
        return mapToProjectDtoList(projectList);
    }

    public ProjectDTO getProject(Integer projectId) {
        return mapToProjectDto(projectQueryBiz.getProject(projectId));
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

    private ProjectDTO mapToProjectDto(Project project) {
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
