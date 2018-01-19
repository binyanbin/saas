package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.TechnicianMapper;
import com.bzw.api.module.basic.dao.TechnicianPhotoMapper;
import com.bzw.api.module.basic.dao.TechnicianProjectMapper;
import com.bzw.api.module.basic.model.*;
import com.bzw.common.enums.Status;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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

    public List<String> listTechnicianPhotoById(Long technicianId) {
        List<String> result = Lists.newArrayList();
        TechnicianPhotoExample example = new TechnicianPhotoExample();
        example.createCriteria().andTechnicianIdEqualTo(technicianId);
        List<TechnicianPhoto> technicianPhotos = technicianPhotoMapper.selectByExample(example);
        for (TechnicianPhoto technicianPhoto : technicianPhotos) {
            result.add(technicianPhoto.getUrl());
        }
        return result;
    }

    public Map<Long, List<String>> listTechnicianPhotoByIds(List<Long> technicianIds) {
        Map<Long, List<String>> result = Maps.newHashMap();
        TechnicianPhotoExample example = new TechnicianPhotoExample();
        example.createCriteria().andTechnicianIdIn(technicianIds);
        List<TechnicianPhoto> technicianPhotos = technicianPhotoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(technicianPhotos)) {
            return result;
        }
        for (TechnicianPhoto technicianPhoto : technicianPhotos) {
            if (StringUtils.isNotBlank(technicianPhoto.getUrl())) {
                if (result.containsKey(technicianPhoto.getTechnicianId())) {
                    List<String> photos = result.get(technicianPhoto.getTechnicianId());
                    if (!photos.contains(technicianPhoto.getUrl())) {
                        photos.add(technicianPhoto.getUrl());
                    }
                } else {
                    List<String> photos = Lists.newArrayList();
                    photos.add(technicianPhoto.getUrl());
                    result.put(technicianPhoto.getTechnicianId(), photos);
                }
            }
        }
        return result;
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
