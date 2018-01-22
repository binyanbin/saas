package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.RoomMapper;
import com.bzw.api.module.basic.model.Room;
import com.bzw.api.module.basic.model.RoomExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class RoomEventBiz {

    @Autowired
    private RoomMapper roomMapper;

    public int update(Room room, Long id, Integer version){
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andIdEqualTo(id).andVersionIdEqualTo(version);
        return roomMapper.updateByExampleSelective(room, roomExample);
    }

    public void update(Room room){
        roomMapper.updateByPrimaryKey(room);
    }
}
