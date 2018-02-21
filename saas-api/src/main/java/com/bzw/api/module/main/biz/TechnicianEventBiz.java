package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.*;
import com.bzw.api.module.base.model.*;
import com.bzw.common.system.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private TechnicianProjectMapper technicianProjectMapper;

    @Autowired
    private TechnicianPhotoMapper technicianPhotoMapper;

    public void updateList(List<Technician> technicianList) {
        for (Technician technician : technicianList) {
            technicianMapper.updateByPrimaryKey(technician);
        }
    }

    public boolean updateTechnician(Technician technician) {
        return technicianMapper.updateByPrimaryKey(technician)>0;
    }

    public void addTechnician(Technician technician){
        technicianMapper.insert(technician);
    }

    public boolean deleteTechnician(Long id,Long employeeId){
        Technician technician = technicianMapper.selectByPrimaryKey(id);
        if (technician==null) {
            return  false;
        }
        technician.setStatusId(Status.Delete.getValue());
        technician.setModifiedId(employeeId);
        technician.setModifiedDate(new Date());
        return technicianMapper.updateByPrimaryKey(technician)>0;
    }

    public void addTechnicianAccess(TechnicianAssess technicianAssess) {
        technicianAssessMapper.insert(technicianAssess);
    }

    public void addTechnicianTag(List<TechnicianTag> technicianTags) {
        for (TechnicianTag technicianTag : technicianTags) {
            technicianTagMapper.insert(technicianTag);
        }
    }

    public void addTechnicianProject(List<TechnicianProject> technicianProjects){
        for (TechnicianProject technicianProject : technicianProjects) {
            technicianProjectMapper.insert(technicianProject);
        }
    }

    public void addTechnicianPhoto(List<TechnicianPhoto> technicianPhotos){
        for (TechnicianPhoto technicianPhoto :technicianPhotos){
            technicianPhotoMapper.insert(technicianPhoto);
        }
    }

    public void deleteProjectByTechnicianId(Long technicianId){
        TechnicianProjectExample technicianProjectExample = new TechnicianProjectExample();
        technicianProjectExample.createCriteria().andTechnicianIdEqualTo(technicianId);
        technicianProjectMapper.deleteByExample(technicianProjectExample);
    }

    public boolean deletePhoto(Long id){
        return technicianPhotoMapper.deleteByPrimaryKey(id)>0;
    }


}
