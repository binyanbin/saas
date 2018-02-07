package com.bzw.api.module.main.service;

import com.bzw.api.module.base.model.*;
import com.bzw.api.module.main.biz.OrderEventBiz;
import com.bzw.api.module.main.biz.OrderQueryBiz;
import com.bzw.api.module.main.biz.TechnicianEventBiz;
import com.bzw.api.module.main.biz.TechnicianQueryBiz;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.dto.TechnicianDTO;
import com.bzw.api.module.main.enums.OrderDetailState;
import com.bzw.api.module.main.enums.TechnicianState;
import com.bzw.api.module.main.params.PhotoParam;
import com.bzw.api.module.main.params.TechnicianParam;
import com.bzw.common.enums.Status;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author yanbin
 */
@Service
public class TechnicianEventService {

    @Autowired
    private TechnicianEventBiz technicianEventBiz;

    @Autowired
    private TechnicianQueryBiz technicianQueryBiz;

    @Autowired
    private OrderQueryBiz orderQueryBiz;

    @Autowired
    private OrderEventBiz orderEventBiz;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private TechnicianQueryService technicianQueryService;

    /**
     * 评价技师
     */
    public void assess(Long technicianId, Long orderDetailId, Integer grade, String tag, String openId) {
        Technician technician = technicianQueryBiz.getTechnicianById(technicianId);
        Preconditions.checkNotNull(technician, WarnMessage.NOT_FOUND_TECHNICIAN);
        OrderDetail orderDetail = orderQueryBiz.getOrderDetail(orderDetailId);
        Preconditions.checkNotNull(orderDetail, WarnMessage.NOT_FOUND_ORDER);
        Preconditions.checkState(orderDetail.getBizStatusId().equals(OrderDetailState.finished.getValue()), WarnMessage.SERVICE_NOT_FINISH);
        orderDetail.setBizStatusId(OrderDetailState.access.getValue());
        orderEventBiz.updateOrderDetail(orderDetail);

        Long accessId = sequenceService.newKey(SeqType.technicianAssess);
        TechnicianAssess technicianAssess = new TechnicianAssess();
        technicianAssess.setId(accessId);
        technicianAssess.setGrade(grade);
        technicianAssess.setOrderDetailId(orderDetailId);
        technicianAssess.setWechatId(openId);
        technicianAssess.setTechnicianId(technicianId);
        technicianAssess.setTenantId(technician.getTenantId());
        technicianAssess.setBranchId(technician.getBranchId());
        technicianEventBiz.addTechnicianAccess(technicianAssess);
        List<TechnicianTag> technicianTagList = Lists.newArrayList();
        if (StringUtils.isNotBlank(tag)) {
            String[] tags = tag.split(",");
            List<Long> tagIds = sequenceService.newKeys(SeqType.technicianTag, tags.length);
            int i = 0;
            for (String tagName : tags) {
                if (StringUtils.isNotBlank(tagName)) {
                    TechnicianTag technicianTag = new TechnicianTag();
                    technicianTag.setAssessId(accessId);
                    technicianTag.setId(tagIds.get(i++).intValue());
                    technicianTag.setBranchId(technician.getBranchId());
                    technicianTag.setTechnicianId(technicianId);
                    technicianTag.setTenantId(technician.getTenantId());
                    technicianTag.setTag(tagName);
                    technicianTagList.add(technicianTag);
                }
            }
            if (!CollectionUtils.isEmpty(technicianTagList)) {
                technicianEventBiz.addTechnicianTag(technicianTagList);
            }
        }
    }

    /**
     * 释放技师
     */
    public void freeTechnician(Long id) {
        modifyTechnicianByType(id, 1);
    }

    /**
     * 技师工作完成
     */
    public void finishTechnician(Long id) {
        modifyTechnicianByType(id, 2);
    }

    private void modifyTechnicianByType(Long id, int type) {
        Technician technician = technicianQueryBiz.getTechnicianById(id);
        if (technician != null) {
            technician.setStartTime(null);
            technician.setOverTime(null);
            technician.setBizStatusId(TechnicianState.free.getValue());
            technician.setRoomId(null);
            technician.setRoomName(null);
            technician.setOrderDetailId(null);
            if (type == 2) {
                technician.setOrderCount(technician.getOrderCount() + 1);
            }
        }
        technicianEventBiz.updateTechnician(technician);
    }

    public TechnicianDTO add(TechnicianParam technicianParam, Long tenantId, Long branchId, String branchName, Long employeeId) {
        Date now = new Date();
        Technician technician = new Technician();
        technician.setId(sequenceService.newKey(SeqType.technician));
        technician.setStatusId(Status.Valid.getValue());
        technician.setBizStatusId(TechnicianState.free.getValue());
        technician.setBranchId(branchId);
        technician.setBranchName(branchName);
        technician.setCreatedDate(now);
        technician.setCreatedId(employeeId);
        technician.setTenantId(tenantId);
        technician.setModifiedDate(now);
        technician.setModifiedId(employeeId);
        technician.setDescription(technicianParam.getDescription());
        technician.setPhone(technicianParam.getPhone());
        technician.setName(technicianParam.getName());
        technician.setAge(technicianParam.getAge());
        technician.setJobNumber(technicianParam.getJobNumber());
        technicianEventBiz.addTechnician(technician);
        List<TechnicianProject> technicianProjects = Lists.newArrayList();
        for (Integer projectId : technicianParam.getProjectIds()) {
            TechnicianProject technicianProject = new TechnicianProject();
            technicianProject.setId(sequenceService.newKey(SeqType.technicianProject));
            technicianProject.setBranchId(branchId);
            technicianProject.setTechnicianId(technician.getId());
            technicianProject.setTenantId(tenantId);
            technicianProject.setProjectId(projectId);
            technicianProjects.add(technicianProject);
        }
        technicianEventBiz.addTechnicianProject(technicianProjects);

        return technicianQueryService.mapToTechnicianDTO(technician);
    }

    public boolean update(TechnicianParam technicianParam, Long id, Long employeeId) {
        Date now = new Date();
        Technician technician = technicianQueryBiz.getTechnicianById(id);
        if (technician == null) {
            return false;
        }
        technician.setModifiedDate(now);
        technician.setModifiedId(employeeId);
        technician.setDescription(technicianParam.getDescription());
        technician.setPhone(technicianParam.getPhone());
        technician.setName(technicianParam.getName());
        technician.setAge(technicianParam.getAge());
        technician.setJobNumber(technicianParam.getJobNumber());
        technicianEventBiz.deleteProjectByTechnicianId(id);
        List<TechnicianProject> technicianProjects = Lists.newArrayList();
        for (Integer projectId : technicianParam.getProjectIds()) {
            TechnicianProject technicianProject = new TechnicianProject();
            technicianProject.setId(sequenceService.newKey(SeqType.technicianProject));
            technicianProject.setBranchId(technician.getBranchId());
            technicianProject.setTechnicianId(technician.getId());
            technicianProject.setTenantId(technician.getTenantId());
            technicianProject.setProjectId(projectId);
            technicianProjects.add(technicianProject);
        }
        technicianEventBiz.addTechnicianProject(technicianProjects);
        return technicianEventBiz.updateTechnician(technician);
    }

    public boolean delete(Long id, Long employeeId) {
        return technicianEventBiz.deleteTechnician(id, employeeId);
    }

    public void addPhoto(List<PhotoParam> photoParams, Long technicianId, Long branchId, Long tenantId) {
        List<Long> ids = sequenceService.newKeys(SeqType.technicianPhoto, photoParams.size());
        List<TechnicianPhoto> technicianPhotos = Lists.newArrayList();
        for (int i = 0; i < photoParams.size(); i++) {
            TechnicianPhoto technicianPhoto = new TechnicianPhoto();
            technicianPhoto.setId(ids.get(i));
            technicianPhoto.setBranchId(branchId);
            technicianPhoto.setTenantId(tenantId);
            technicianPhoto.setTechnicianId(technicianId);
            technicianPhoto.setName(photoParams.get(i).getName());
            technicianPhoto.setUrl(photoParams.get(i).getUrl());
            technicianPhoto.setImageId(photoParams.get(i).getImageId());
            technicianPhotos.add(technicianPhoto);
        }
        technicianEventBiz.addTechnicianPhoto(technicianPhotos);
    }

    public boolean deletePhoto(Long technicianId, String imageId) {
        boolean result = false;
        List<TechnicianPhoto> technicianPhotos = technicianQueryBiz.listPhotoByTechnicianId(technicianId);
        for (TechnicianPhoto technicianPhoto : technicianPhotos) {
            if (technicianPhoto.getImageId().equals(imageId)) {
                if (technicianEventBiz.deletePhoto(technicianPhoto.getId())) {
                    result = true;
                }
            }
        }
        return result;
    }
}
