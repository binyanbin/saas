package com.bzw.api.module.main.controller;

import com.bzw.api.module.base.model.OrderDetail;
import com.bzw.api.module.base.model.Technician;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.enums.TechnicianState;
import com.bzw.api.module.main.params.AccessParam;
import com.bzw.api.module.main.params.OrderParam;
import com.bzw.api.module.main.service.OrderEventService;
import com.bzw.api.module.main.service.OrderQueryService;
import com.bzw.api.module.main.service.TechnicianEventService;
import com.bzw.api.module.main.service.TechnicianQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.content.DuplicationSubmit;
import com.bzw.common.system.BusinessType;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private OrderEventService orderEventService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private TechnicianEventService technicianEventService;

    @Autowired
    private TechnicianQueryService technicianQueryService;

    private final String NO_TECHNICIAN = "没有选择技师";
    private final String TECHNICIAN_ORDER = "[%s]已被预约,暂时无法预约";

    @RequestMapping(value = "room/{roomId}/clientBook", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object clientBook(@RequestBody List<OrderParam> orderParams, @PathVariable Long roomId, @RequestParam String openId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(openId), NO_OPENID);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(orderParams), NO_TECHNICIAN);
        List<Long> technicianIds = Lists.newArrayList();
        for (OrderParam orderParam : orderParams) {
            technicianIds.add(orderParam.getTechnicianId());
        }
        List<Integer> statusIds = Lists.newArrayList();
        statusIds.add(TechnicianState.free.getValue());
        statusIds.add(TechnicianState.serving.getValue());
        List<Technician> technicians = technicianQueryService.listTechnicianByIds(technicianIds);
        for (Technician technician : technicians) {
            Preconditions.checkArgument(statusIds.contains(technician.getBizStatusId()), String.format(TECHNICIAN_ORDER, technician.getName()));
        }
        Long id = orderEventService.addOrderForClient(orderParams, openId, roomId);
        if (id != null) {
            return wrapperJsonView(orderQueryService.getOrderDto(id));
        } else {
            return wrapperJsonView(null);
        }
    }

    @RequestMapping(value = "room/{roomId}/deskBook", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    @DuplicationSubmit(businessType = BusinessType.appOrder)
    public Object desckBook(@RequestBody List<OrderParam> orderParam, @PathVariable Long roomId) {
        Long id = orderEventService.addOrderForDesk(orderParam, WebUtils.Session.getUserId(), roomId);
        Preconditions.checkArgument(id != null, WarnMessage.USER_NOT_EXISTS);
        return wrapperJsonView(orderQueryService.getOrderDto(id));
    }

    @RequestMapping(value = "/{id}")
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object getOrder(@PathVariable Long id) {
        return wrapperJsonView(orderQueryService.getOrderDto(id));
    }

    @RequestMapping(value = "/{id}/pay", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object pay(@PathVariable Long id) {
        orderEventService.pay(id, null);
        return wrapperJsonView(true);
    }

    @RequestMapping(value = "/{id}/modify/desk", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object modifyForDesk(@PathVariable Long id, @RequestBody List<OrderParam> orderParam) {
        Long orderId = orderEventService.modifyOrderForDesk(id, orderParam, WebUtils.Session.getUserId());
        Preconditions.checkArgument(orderId != null, WarnMessage.NOT_FOUND_ORDER);
        return wrapperJsonView(orderQueryService.getOrderDto(orderId));
    }

    @RequestMapping(value = "/{id}/modify/client", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object modifyForClient(@PathVariable Long id, @RequestBody List<OrderParam> orderParam, @RequestParam String openId) {
        Long orderId = orderEventService.modifyOrderForClient(id, orderParam, openId);
        Preconditions.checkArgument(orderId != null, WarnMessage.NOT_FOUND_ORDER);
        return wrapperJsonView(orderQueryService.getOrderDto(orderId));
    }

    @RequestMapping(value = "detail/{detailId}/serve", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object serve(@PathVariable Long detailId) {
        OrderDetail orderDetail = orderEventService.serve(detailId);
        return wrapperJsonView(orderDetail.getRoomName());
    }

    private final String NO_ACCESS = "评价不存在";
    private final String NO_OPENID = "openId不存在";

    @RequestMapping(value = "detail/{detailId}/technician/{technicianId}/access", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object access(@PathVariable Long detailId, @PathVariable Long technicianId, @RequestBody AccessParam accessParam) {
        Preconditions.checkArgument(accessParam.getGrade() != null, NO_ACCESS);
        Preconditions.checkArgument(!StringUtils.isNotBlank(accessParam.getOpenId()), NO_OPENID);
        technicianEventService.assess(technicianId, detailId, accessParam.getGrade(), accessParam.getTags(), accessParam.getOpenId());
        return wrapperJsonView(true);
    }

}
