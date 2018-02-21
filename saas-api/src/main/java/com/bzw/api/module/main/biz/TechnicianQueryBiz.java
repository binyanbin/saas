package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.TechnicianMapper;
import com.bzw.api.module.base.dao.TechnicianPhotoMapper;
import com.bzw.api.module.base.dao.TechnicianProjectMapper;
import com.bzw.api.module.base.model.*;
import com.bzw.common.system.Status;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class TechnicianQueryBiz {

    @Autowired
    TechnicianMapper technicianMapper;

    @Autowired
    private TechnicianProjectMapper technicianProjectMapper;

    @Autowired
    private TechnicianPhotoMapper technicianPhotoMapper;

    public List<TechnicianPhoto> listPhotoByTechnicianId(Long technicianId) {
        TechnicianPhotoExample example = new TechnicianPhotoExample();
        example.createCriteria().andTechnicianIdEqualTo(technicianId);
        return technicianPhotoMapper.selectByExample(example);
    }


    public Technician getTechnicianById(Long technicianId) {
        return technicianMapper.selectByPrimaryKey(technicianId);
    }

    public List<Technician> listTechnicianByBranchId(Long branchId, int sort) {
        String orderByPraise = " praise desc";
        String orderByCount = " order_count desc";
        int praise = 1, order = 2;
        TechnicianExample technicianExample = new TechnicianExample();
        technicianExample.createCriteria().andBranchIdEqualTo(branchId).andStatusIdEqualTo(Status.Valid.getValue());
        if (sort == praise) {
            technicianExample.setOrderByClause(orderByPraise);
        } else if (sort == order) {
            technicianExample.setOrderByClause(orderByCount);
        }
        return technicianMapper.selectByExample(technicianExample);
    }

    public List<Technician> listTechnicianByProjectId(Integer projectId) {
        TechnicianProjectExample example = new TechnicianProjectExample();
        example.createCriteria().andProjectIdEqualTo(projectId);
        List<TechnicianProject> technicianProjects = technicianProjectMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(technicianProjects)) {
            return Lists.newArrayList();
        }
        List<Long> technicianIds = Lists.newArrayList();
        Long branchId = 0L;
        for (TechnicianProject technicianProject : technicianProjects) {
            if (!technicianIds.contains(technicianProject.getTechnicianId())) {
                technicianIds.add(technicianProject.getTechnicianId());
            }
            branchId = technicianProject.getBranchId();
        }
        TechnicianExample technicianExample = new TechnicianExample();
        technicianExample.createCriteria().andIdIn(technicianIds).andStatusIdEqualTo(Status.Valid.getValue()).andBranchIdEqualTo(branchId);
        List<Technician> result = technicianMapper.selectByExample(technicianExample);
        if (CollectionUtils.isEmpty(result)) {
            return Lists.newArrayList();
        } else {
            return result;
        }
    }

    public List<Technician> listTechnicianByIds(List<Long> technicianIds) {
        TechnicianExample technicianExample = new TechnicianExample();
        technicianExample.createCriteria().andIdIn(technicianIds);
        return technicianMapper.selectByExample(technicianExample);
    }

}
