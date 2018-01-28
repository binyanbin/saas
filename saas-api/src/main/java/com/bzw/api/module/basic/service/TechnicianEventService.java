package com.bzw.api.module.basic.service;

import com.bzw.api.module.basic.biz.TechnicianEventBiz;
import com.bzw.api.module.basic.biz.TechnicianQueryBiz;
import com.bzw.api.module.basic.constant.WarnMessage;
import com.bzw.api.module.basic.model.Technician;
import com.bzw.api.module.basic.model.TechnicianAssess;
import com.bzw.api.module.basic.model.TechnicianTag;
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
    private SequenceService sequenceService;

    public void assess(Long technicianId,Long orderDetailId,Integer grade,String tag,String openId){
        Technician technician = technicianQueryBiz.getTechnicianById(technicianId);
        Preconditions.checkArgument(technician!=null, WarnMessage.NOT_FOUND_TECHNICIAN);
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
}
