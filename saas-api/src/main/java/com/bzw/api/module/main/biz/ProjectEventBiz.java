package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.ProjectMapper;
import com.bzw.api.module.base.model.Project;
import com.bzw.common.system.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class ProjectEventBiz {

    @Autowired
    private ProjectMapper projectMapper;

    public void add(Project project) {
        projectMapper.insert(project);
    }

    public boolean update(Project project){
        return projectMapper.updateByPrimaryKey(project)>0;
    }

    public boolean delete(Integer id) {
        Project project = projectMapper.selectByPrimaryKey(id);
        if (project == null) {
            return false;
        }
        project.setStatusId(Status.Delete.getValue());
        return projectMapper.updateByPrimaryKey(project) > 0;
    }

}
