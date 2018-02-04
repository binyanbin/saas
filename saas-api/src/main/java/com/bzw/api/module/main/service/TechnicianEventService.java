package com.bzw.api.module.main.service;

import com.bzw.api.module.main.biz.OrderEventBiz;
import com.bzw.api.module.main.biz.OrderQueryBiz;
import com.bzw.api.module.main.biz.TechnicianEventBiz;
import com.bzw.api.module.main.biz.TechnicianQueryBiz;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.enums.OrderDetailState;
import com.bzw.api.module.main.enums.TechnicianState;
import com.bzw.api.module.base.model.OrderDetail;
import com.bzw.api.module.base.model.Technician;
import com.bzw.api.module.base.model.TechnicianAssess;
import com.bzw.api.module.base.model.TechnicianTag;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    /**
     * 评价技师
     */
    public void assess(Long technicianId,Long orderDetailId,Integer grade,String tag,String openId){
        Technician technician = technicianQueryBiz.getTechnicianById(technicianId);
        Preconditions.checkArgument(technician!=null, WarnMessage.NOT_FOUND_TECHNICIAN);
        OrderDetail orderDetail = orderQueryBiz.getOrderDetail(orderDetailId);
        Preconditions.checkArgument(orderDetail!=null,WarnMessage.NOT_FOUND_ORDER);
        Preconditions.checkArgument(orderDetail.getBizStatusId().equals(OrderDetailState.finished.getValue()),WarnMessage.SERVICE_NOT_FINISH);
        orderDetail.setBizStatusId(OrderDetailState.access.getValue());
        orderEventBiz.updateOrderDetail(orderDetail);

        Long accessId = sequenceService.newKey(SeqType.technicianAssess);
        TechnicianAssess technicianAssess =new TechnicianAssess();
        technicianAssess.setId(accessId);
        technicianAssess.setGrade(grade);
        technicianAssess.setOrderDetailId(orderDetailId);
        technicianAssess.setWechatId(openId);
        technicianAssess.setTechnicianId(technicianId);
        technicianAssess.setTenantId(technician.getTenantId());
        technicianAssess.setBranchId(technician.getBranchId());
        technicianEventBiz.addTechnicianAccess(technicianAssess);
        List<TechnicianTag> technicianTagList = Lists.newArrayList();
        if (StringUtils.isNotBlank(tag)){
            String[] tags = tag.split(",");
            List<Long> tagIds = sequenceService.newKeys(SeqType.technicianTag,tags.length);
            int i=0;
            for (String tagName :tags){
                if (StringUtils.isNotBlank(tagName)){
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
            if (!CollectionUtils.isEmpty(technicianTagList)){
                technicianEventBiz.addTechnicianTag(technicianTagList);
            }
        }
    }

    /**
     * 释放技师
     */
    public void freeTechnician(Long id){
        modifyTechnicianByType(id,1);
    }

    /**
     * 技师工作完成
     */
    public void finishTechnician(Long id){
        modifyTechnicianByType(id,2);
    }

    private void modifyTechnicianByType(Long id,int type){
        Technician technician = technicianQueryBiz.getTechnicianById(id);
        if (technician!=null){
            technician.setStartTime(null);
            technician.setOverTime(null);
            technician.setBizStatusId(TechnicianState.free.getValue());
            technician.setRoomId(null);
            technician.setRoomName(null);
            technician.setOrderDetailId(null);
            if (type==2) {
                technician.setOrderCount(technician.getOrderCount()+1);
            }
        }
        technicianEventBiz.updateTechnician(technician);
    }
}
