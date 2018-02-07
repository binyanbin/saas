package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.Project;
import com.bzw.api.module.base.model.Technician;
import com.bzw.api.module.base.model.TechnicianPhoto;
import com.bzw.api.module.main.biz.ProjectQueryBiz;
import com.bzw.api.module.main.biz.TechnicianQueryBiz;
import com.bzw.api.module.main.dto.ProjectDTO;
import com.bzw.api.module.main.dto.TechnicianDTO;
import com.bzw.api.module.main.dto.TechnicianDetailDTO;
import com.bzw.api.module.main.dto.TechnicianPhotoDTO;
import com.bzw.api.module.main.enums.ProjectType;
import com.bzw.api.module.main.enums.TechnicianState;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class TechnicianQueryService {

    @Autowired
    private TechnicianQueryBiz technicianQueryBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    @Autowired
    private ProjectQueryService projectQueryService;

    public List<TechnicianDTO> listTechnicianByProjectId(Integer projectId) {
        List<Technician> technicians = technicianQueryBiz.listTechnicianByProjectId(projectId);
        return mapToTechnicianDtoList(technicians);
    }

    public List<TechnicianDTO> listTechnicianByBranchId(Long branchId, int sort) {
        List<Technician> technicians = technicianQueryBiz.listTechnicianByBranchId(branchId, sort);
        return mapToTechnicianDtoList(technicians);
    }

    public List<Technician> listTechnicianByIds(List<Long> ids) {
        return technicianQueryBiz.listTechnicianByIds(ids);
    }

    public List<TechnicianDTO> mapToTechnicianDtoList(List<Technician> technicians) {
        List<TechnicianDTO> result = Lists.newArrayList();
        for (Technician technician : technicians) {
            TechnicianDTO technicianDTO = mapToTechnicianDTO(technician);
            result.add(technicianDTO);
        }
        return result;
    }

    public TechnicianDTO mapToTechnicianDTO(Technician technician) {
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
        return technicianDTO;
    }


    public TechnicianDetailDTO getTechnicianDetail(Long technicianId) {
        Technician technician = technicianQueryBiz.getTechnicianById(technicianId);
        if (technician == null) {
            return null;
        }
        List<TechnicianPhoto> photos = technicianQueryBiz.listPhotoByTechnicianId(technicianId);
        List<Project> projects = projectQueryBiz.listProjectByTechnicianId(technicianId);
        return mapToTechnicianDetailDTO(technician, photos, projects);
    }

    public TechnicianDetailDTO mapToTechnicianDetailDTO(Technician technician, List<TechnicianPhoto> photos, List<Project> projects) {
        TechnicianDetailDTO result = new TechnicianDetailDTO();
        result.setAge(technician.getAge());
        result.setDescription(technician.getDescription());
        result.setName(technician.getName());
        result.setPhone(technician.getPhone());
        result.setStateName(TechnicianState.parse(technician.getBizStatusId()).getDesc());
        result.setJobNumber(technician.getJobNumber());
        result.setOrderCount(technician.getOrderCount());
        result.setStateId(technician.getBizStatusId());
        result.setDescription(technician.getDescription());
        result.setId(technician.getId());
        List<TechnicianPhotoDTO> technicianPhotoDTOList = Lists.newArrayList();
        for(TechnicianPhoto technicianPhoto : photos) {
            TechnicianPhotoDTO technicianPhotoDTO = new TechnicianPhotoDTO();
            technicianPhotoDTO.setImageId(technicianPhoto.getImageId());
            technicianPhotoDTO.setName(technicianPhoto.getName());
            technicianPhotoDTO.setUrl(technicianPhoto.getUrl());
            technicianPhotoDTOList.add(technicianPhotoDTO);
        }
        result.setPhotos(technicianPhotoDTOList);
        List<String> spa = Lists.newArrayList();
        List<String> massage = Lists.newArrayList();
        List<ProjectDTO> projectDTOS = Lists.newArrayList();
        for (Project project : projects) {
            if (project.getType().equals(ProjectType.spa.getValue())) {
                spa.add(project.getName());
            } else {
                massage.add(project.getName());
            }
            projectDTOS.add(projectQueryService.mapToProjectDto(project));
        }
        result.setProjects(projectDTOS);
        result.setSpa(spa);
        result.setMassage(massage);
        return result;
    }
}
