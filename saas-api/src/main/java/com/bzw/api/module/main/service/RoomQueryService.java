package com.bzw.api.module.main.service;

import com.bzw.api.module.main.biz.ProjectQueryBiz;
import com.bzw.api.module.main.biz.RoomQueryBiz;
import com.bzw.api.module.main.dto.RoomDTO;
import com.bzw.api.module.main.enums.ProjectType;
import com.bzw.api.module.main.enums.RoomState;
import com.bzw.api.module.base.model.Project;
import com.bzw.api.module.base.model.Room;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomQueryService {

    @Autowired
    private RoomQueryBiz roomQueryBiz;

    @Autowired
    private ProjectQueryBiz projectQueryBiz;

    public RoomDTO getRoom(Long roomId) {
        Room room = roomQueryBiz.getRoom(roomId);
        return mapToRoomDto(room);
    }

    public List<RoomDTO> listRoomsByBranchId(Long branchId) {
        List<Room> rooms = roomQueryBiz.listRoomByBranchId(branchId);
        return mapToRoomDtoList(rooms);
    }

    public List<RoomDTO> listRoomsByBranchId(Long branchId, Integer projectId) {
        Project project = projectQueryBiz.getProject(projectId);
        List<Room> rooms = roomQueryBiz.listRoomByBranchId(branchId, project.getType());
        return mapToRoomDtoList(rooms);
    }

    public List<RoomDTO> listRoomByProjectId(Integer projectId) {
        List<Room> rooms = roomQueryBiz.listRoomByProjectId(projectId);
        return mapToRoomDtoList(rooms);
    }

    private List<RoomDTO> mapToRoomDtoList(List<Room> rooms) {
        List<RoomDTO> result = Lists.newArrayList();
        for (Room room : rooms) {
            result.add(mapToRoomDto(room));
        }
        return result;
    }

    public RoomDTO mapToRoomDto(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setBizStatusName(RoomState.parse(room.getBizStatusId()).getDesc());
        roomDTO.setBizStatusId(room.getBizStatusId());
        roomDTO.setHaveRestRoom(room.getHaveRestroom() == 1);
        roomDTO.setTypeId(room.getType());
        roomDTO.setTypeName(ProjectType.parse(room.getType()).getDesc());
        roomDTO.setBedNumber(room.getBedNumber());
        roomDTO.setStartTime(room.getStartTime());
        roomDTO.setOverTime(room.getOverTime());
        return roomDTO;
    }

}
