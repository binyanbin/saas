package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.TechnicianMapper;
import com.bzw.api.module.basic.model.Technician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class TechnicianEventBiz {

    @Autowired
    private TechnicianMapper technicianMapper;

    public void updateTechnicians(List<Technician> technicianList){
        for (Technician technician :technicianList){
            technicianMapper.updateByPrimaryKey(technician);
        }
    }
}
