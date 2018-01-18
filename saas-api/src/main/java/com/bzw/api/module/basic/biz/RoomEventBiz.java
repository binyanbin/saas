package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.EmployeeMapper;
import com.bzw.api.module.basic.dao.RecordChangeMapper;
import com.bzw.api.module.basic.dao.RoomMapper;
import com.bzw.api.module.basic.enums.RecordChangeType;
import com.bzw.api.module.basic.enums.RoomState;
import com.bzw.api.module.basic.model.Employee;
import com.bzw.api.module.basic.model.RecordChange;
import com.bzw.api.module.basic.model.Room;
import com.bzw.api.module.basic.model.RoomExample;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomEventBiz {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RecordChangeMapper recordChangeMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SequenceService sequenceService;

    public boolean updateRoomState(Long roomId, Integer statusId, Long employeeId) {
        Room room = roomMapper.selectByPrimaryKey(roomId);
        if (room.getBizStatusId().equals(statusId)) {
            return false;
        }
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        Date now = new Date();
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andIdEqualTo(roomId).andVersionIdEqualTo(room.getVersionId());
        Room updateRoom = new Room();
        updateRoom.setStatusId(statusId);
        updateRoom.setModifiedTime(now);
        updateRoom.setModifiedId(employee.getId());
        updateRoom.setVersionId(room.getVersionId() + 1);
        int result = roomMapper.updateByExampleSelective(updateRoom, roomExample);
        if (result > 0) {
            RecordChange recordChange = new RecordChange();
            recordChange.setId(sequenceService.newKey(SeqType.recordChange));
            recordChange.setChnageDate(now);
            recordChange.setContent(employee.getName() + "把房态[" + RoomState.parse(room.getBizStatusId()).getDesc() + "]修改为:[" + RoomState.parse(statusId).getDesc() + "]");
            recordChange.setDocumentId(room.getId());
            recordChange.setTenantId(room.getTenantId());
            recordChange.setType(RecordChangeType.room.getValue());
            recordChangeMapper.insert(recordChange);
            return true;
        } else {
            return false;
        }
    }

    public void updateRoom(Room room){
        roomMapper.updateByPrimaryKeySelective(room);
    }
}
