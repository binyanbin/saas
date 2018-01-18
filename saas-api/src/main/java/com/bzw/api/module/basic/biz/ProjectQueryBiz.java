package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.ProjectMapper;
import com.bzw.api.module.basic.dao.RoomMapper;
import com.bzw.api.module.basic.dao.TechnicianProjectMapper;
import com.bzw.api.module.basic.model.*;
import com.bzw.common.enums.Status;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class ProjectQueryBiz {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private TechnicianProjectMapper technicianProjectMapper;


    public List<Project> listProjectsByRoomId(Long roomId) {
        Room room = roomMapper.selectByPrimaryKey(roomId);
        ProjectExample example  = new ProjectExample();
        example.createCriteria().andBranchIdEqualTo(room.getBranchId()).andTypeEqualTo(room.getType());
        return projectMapper.selectByExample(example);
    }

    public List<Project> listProjectByBranchId(Long branchId){
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andBranchIdEqualTo(branchId).andStatusIdEqualTo(Status.Valid.getValue());
        return projectMapper.selectByExample(projectExample);
    }

    public List<Project> listProjectByIds(List<Integer> projectIds){
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andIdIn(projectIds);
        return projectMapper.selectByExample(projectExample);
    }

    public List<Project> listProjectByTechnicianId(Long technicianId) {
        TechnicianProjectExample technicianProjectExample = new TechnicianProjectExample();
        technicianProjectExample.createCriteria().andTechnicianIdEqualTo(technicianId);
        List<TechnicianProject> technicianProjects = technicianProjectMapper.selectByExample(technicianProjectExample);
        List<Integer> projectIds = Lists.newArrayList();
        for (TechnicianProject project : technicianProjects) {
            projectIds.add(project.getProjectId());
        }
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andIdIn(projectIds);
        List<Project> result = projectMapper.selectByExample(projectExample);
        if (CollectionUtils.isEmpty(result)) {
            return Lists.newArrayList();
        } else {
            return result;
        }
    }

    public Map<Long, List<Project>> listTechnicianProject(List<Long> technicianIds) {
        Map<Long, List<Project>> result = Maps.newHashMap();
        TechnicianProjectExample technicianProjectExample = new TechnicianProjectExample();
        technicianProjectExample.createCriteria().andTechnicianIdIn(technicianIds);
        List<TechnicianProject> technicianProjects = technicianProjectMapper.selectByExample(technicianProjectExample);
        List<Integer> projectIds = Lists.newArrayList();
        for (TechnicianProject project : technicianProjects) {
            projectIds.add(project.getProjectId());
        }
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andIdIn(projectIds);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        if (CollectionUtils.isEmpty(projects)) {
            return result;
        }
        Map<Integer, Project> mapProject = Maps.newHashMap();
        projects.forEach(t -> mapProject.put(t.getId(), t));
        for (TechnicianProject technicianProject : technicianProjects) {
            Project project = mapProject.get(technicianProject.getProjectId());
            if (project != null) {
                if (result.containsKey(technicianProject.getTechnicianId())) {
                    List<Project> projectList = result.get(technicianProject.getTechnicianId());
                    projectList.add(project);
                } else {
                    List<Project> projectList = Lists.newArrayList();
                    projectList.add(project);
                    result.put(technicianProject.getTechnicianId(), projectList);
                }
            }
        }
        return result;
    }
}
