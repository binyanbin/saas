package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.BranchMapper;
import com.bzw.api.module.base.dao.ProjectMapper;
import com.bzw.api.module.base.dao.RoomMapper;
import com.bzw.api.module.base.model.Branch;
import com.bzw.api.module.base.model.Project;
import com.bzw.api.module.base.model.Room;
import com.bzw.api.module.base.model.RoomExample;
import com.bzw.api.module.main.enums.RoomState;
import com.bzw.common.system.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class RoomQueryBiz {

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ProjectMapper projectMapper;

    public Branch getBranchByRoomId(Long roomId) {
        Room room = roomMapper.selectByPrimaryKey(roomId);
        if (room == null) {
            return null;
        }
        return branchMapper.selectByPrimaryKey(room.getBranchId());
    }

    public Room getRoom(Long roomId) {
        return roomMapper.selectByPrimaryKey(roomId);
    }

    public List<Room> listRoomByBranchId(Long branchId) {
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andBranchIdEqualTo(branchId).andStatusIdEqualTo(Status.Valid.getValue());
        return roomMapper.selectByExample(roomExample);
    }

    public List<Room> listRoomByBranchId(Long branchId,Integer type){
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andBranchIdEqualTo(branchId).andStatusIdEqualTo(Status.Valid.getValue()).andTypeEqualTo(type);
        return roomMapper.selectByExample(roomExample);
    }

    public List<Room> listRoomByProjectId(Integer projectId) {
        Project project = projectMapper.selectByPrimaryKey(projectId);
        Integer type = project.getType();
        Long branchId = project.getBranchId();
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andBranchIdEqualTo(branchId).andTypeEqualTo(type).andStatusIdEqualTo(Status.Valid.getValue());
        return roomMapper.selectByExample(roomExample);
    }

    public List<Room> listRoomByIds(List<Long> roomIds) {
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andIdIn(roomIds);
        return roomMapper.selectByExample(roomExample);
    }

    public List<Room> listRoomByRoomSate(RoomState roomState){
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andStatusIdEqualTo(Status.Valid.getValue())
        .andBizStatusIdEqualTo(roomState.getValue());
        return roomMapper.selectByExample(roomExample);
    }


}
