package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.TechnicianAssessMapper;
import com.bzw.api.module.basic.dao.TechnicianMapper;
import com.bzw.api.module.basic.dao.TechnicianTagMapper;
import com.bzw.api.module.basic.model.Technician;
import com.bzw.api.module.basic.model.TechnicianAssess;
import com.bzw.api.module.basic.model.TechnicianTag;
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

    @Autowired
    private TechnicianAssessMapper technicianAssessMapper;

    @Autowired
    private TechnicianTagMapper technicianTagMapper;

    public void updateList(List<Technician> technicianList) {
        for (Technician technician : technicianList) {
            technicianMapper.updateByPrimaryKey(technician);
        }
    }

    public void updateTechnician(Technician technician) {
        technicianMapper.updateByPrimaryKey(technician);
    }

    public void addTechnicianAccess(TechnicianAssess technicianAssess) {
        technicianAssessMapper.insert(technicianAssess);
    }

    public void addTechnicianTag(List<TechnicianTag> technicianTags) {
        for (TechnicianTag technicianTag : technicianTags) {
            technicianTagMapper.insert(technicianTag);
        }
    }


}
