package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.RoomMapper;
import com.bzw.api.module.base.model.Room;
import com.bzw.api.module.base.model.RoomExample;
import com.bzw.common.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class RoomEventBiz {

    @Autowired
    private RoomMapper roomMapper;

    public int update(Room room, Long id, Integer version) {
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andIdEqualTo(id).andVersionIdEqualTo(version);
        return roomMapper.updateByExampleSelective(room, roomExample);
    }

    public boolean update(Room room) {
        return roomMapper.updateByPrimaryKey(room) > 0;
    }

    public void add(Room room) {
        roomMapper.insert(room);
    }

    public boolean delete(Long id) {
        Room room = roomMapper.selectByPrimaryKey(id);
        if (room == null) {
            return false;
        }
        room.setStatusId(Status.Delete.getValue());
        return roomMapper.updateByPrimaryKey(room) > 0;
    }
}
