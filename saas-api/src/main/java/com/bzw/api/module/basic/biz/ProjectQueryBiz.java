package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.ProjectMapper;
import com.bzw.api.module.basic.dao.RoomMapper;
import com.bzw.api.module.basic.model.Project;
import com.bzw.api.module.basic.model.ProjectExample;
import com.bzw.api.module.basic.model.Room;
import com.bzw.common.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectQueryBiz {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ProjectMapper projectMapper;


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
}
