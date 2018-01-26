package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.ProjectQueryBiz;
import com.bzw.api.module.basic.biz.RoomQueryBiz;
import com.bzw.api.module.basic.biz.TechnicianQueryBiz;
import com.bzw.api.module.basic.biz.UserQueryBiz;
import com.bzw.api.module.basic.dto.BranchDTO;
import com.bzw.api.module.basic.dto.ProjectDTO;
import com.bzw.api.module.basic.dto.TechnicianDTO;
import com.bzw.api.module.basic.dto.TechnicianDetailDTO;
import com.bzw.api.module.basic.enums.ProjectType;
import com.bzw.api.module.basic.enums.TechnicianState;
import com.bzw.api.module.basic.model.Branch;
import com.bzw.api.module.basic.model.Project;
import com.bzw.api.module.basic.model.Technician;
import com.bzw.api.module.basic.model.User;
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
    private TechnicianQueryBiz technicianQueryBiz;

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

    public List<TechnicianDTO> listTechnicianByProjectId(Integer projectId) {
        List<Technician> technicians = technicianQueryBiz.listTechnicianByProjectId(projectId);
        return mapToTechnicianDto(technicians);
    }

    public List<TechnicianDTO> listTechnicianByBranchId(Long branchId, int sort) {
        List<Technician> technicians = technicianQueryBiz.listTechnicianByBranchId(branchId, sort);
        return mapToTechnicianDto(technicians);
    }

    public List<Technician> listTechnicianByIds(List<Long> ids){
        return technicianQueryBiz.listTechnicianByIds(ids);
    }

    private List<TechnicianDTO> mapToTechnicianDto(List<Technician> technicians) {
        List<TechnicianDTO> result = Lists.newArrayList();
        for (Technician technician : technicians) {
            TechnicianDTO technicianDTO = new TechnicianDTO();
            technicianDTO.setAge(technician.getAge());
            technicianDTO.setDescription(technician.getDescription());
            technicianDTO.setName(technician.getName());
            technicianDTO.setPhone(technician.getPhone());
            technicianDTO.setStateName(TechnicianState.parse(technician.getBizStatusId()).getDesc());
            technicianDTO.setStateId(technician.getBizStatusId());
            technicianDTO.setId(technician.getId());
            technicianDTO.setJobNumber(technician.getJobNumber());
            technicianDTO.setPraise(technician.getPraise());
            technicianDTO.setOrderCount(technician.getOrderCount());
            technicianDTO.setOverTime(technician.getOverTime());
            technicianDTO.setRoomId(technician.getRoomId());
            technicianDTO.setRoomName(technician.getRoomName());
            result.add(technicianDTO);
        }
        return result;
    }

    public TechnicianDetailDTO getTechnicianDetail(Long technicianId) {
        TechnicianDetailDTO result = new TechnicianDetailDTO();
        Technician technician = technicianQueryBiz.getTechnicianById(technicianId);
        if (technician != null) {
            result.setAge(technician.getAge());
            result.setDescription(technician.getDescription());
            result.setName(technician.getName());
            result.setPhone(technician.getPhone());
            result.setStateName(TechnicianState.parse(technician.getBizStatusId()).getDesc());
            result.setStateId(technician.getBizStatusId());
            List<String> photos = technicianQueryBiz.listTechnicianPhotoById(technicianId);
            result.setPhotos(photos);
            List<Project> projects = projectQueryBiz.listProjectByTechnicianId(technicianId);
            List<String> spa = Lists.newArrayList();
            List<String> massage = Lists.newArrayList();

            for (Project project : projects) {
                if (project.getType().equals(ProjectType.spa.getValue())) {
                    spa.add(project.getName());
                } else {
                    massage.add(project.getName());
                }
            }
            result.setSpa(spa);
            result.setMassage(massage);
            return result;
        } else {
            return null;
        }
    }
}
