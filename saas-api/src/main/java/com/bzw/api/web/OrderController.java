package com.bzw.api.web;

import com.bzw.api.module.basic.constant.WarnMessage;
import com.bzw.api.module.basic.dto.OrderDetailDTO;
import com.bzw.api.module.basic.dto.WsResult;
import com.bzw.api.module.basic.enums.TechnicianState;
import com.bzw.api.module.basic.model.Order;
import com.bzw.api.module.basic.model.OrderDetail;
import com.bzw.api.module.basic.model.Technician;
import com.bzw.api.module.basic.param.AccessParam;
import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.basic.service.*;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private CustomerQueryService customerQueryService;

    @Autowired
    private OrderEventService orderEventService;

    @Autowired
    private WcService wcService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private TechnicianEventService technicianEventService;

    @Autowired
    private TechnicianQueryService technicianQueryService;

    @RequestMapping(value = "room/{roomId}/clientBook", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object clientBook(@RequestBody List<OrderParam> orderParams, @PathVariable Long roomId, @RequestParam String openId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(openId), "openId不存在");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(orderParams),"没有选择技师");
        List<Long> technicianIds = Lists.newArrayList();
        for (OrderParam orderParam :orderParams){
            technicianIds.add(orderParam.getTechnicianId());
        }
        List<Integer> statusIds = Lists.newArrayList();
        statusIds.add(TechnicianState.free.getValue());
        statusIds.add(TechnicianState.serving.getValue());
        List<Technician> technicians = technicianQueryService.listTechnicianByIds(technicianIds);
        for (Technician technician :technicians){
            Preconditions.checkArgument(statusIds.contains(technician.getBizStatusId()),"["+technician.getName()+"]已被预约,暂时无法预约");
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

    @RequestMapping(value = "/{id}/pay",method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object pay(@PathVariable Long id) {
        orderEventService.pay(id,null);
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
        Preconditions.checkArgument(orderDetail != null, WarnMessage.NOT_FOUND_ORDER);
        Order order = orderQueryService.getOrder(orderDetail.getOrderId());
        Preconditions.checkArgument(order != null, WarnMessage.NOT_FOUND_ORDER);
        if (order.getWechatId() != null) {
            WebSocket.sendMessage(order.getWechatId(), WarnMessage.TECHNICIAN_CONFIRM_ORDER);
        }
        if (order.getUserId() != null) {
            WebSocket.sendMessage(order.getUserId().toString(), WarnMessage.TECHNICIAN_CONFIRM_ORDER);
        }
        return wrapperJsonView(orderDetail.getRoomName());
    }

    @RequestMapping(value = "detail/{detailId}/technician/{technicianId}/access", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object access(@PathVariable Long detailId, @PathVariable Long technicianId, @RequestBody AccessParam accessParam) {
        Preconditions.checkArgument(accessParam.getGrade()!=null,"评价不存在");
        Preconditions.checkArgument(accessParam.getOpenId()!=null,"openId不存在");
        technicianEventService.assess(technicianId,detailId,accessParam.getGrade(),accessParam.getTags(),accessParam.getOpenId());
        return wrapperJsonView(true);
    }

    @RequestMapping(value = "{id}/templateMessage", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object sendTemplateMessage(@PathVariable Long id, @RequestParam String formId) {
        List<OrderDetailDTO> orderDetails = orderQueryService.listOrderDetail(id);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(orderDetails), WarnMessage.NOT_FOUND_ORDER);
        List<Long> technicianIds = Lists.newArrayList();
        Map<Long, OrderDetailDTO> mapOrderDetail = Maps.newHashMap();
        for (OrderDetailDTO orderDetailDTO : orderDetails) {
            technicianIds.add(orderDetailDTO.getTechnicianId());
            mapOrderDetail.put(orderDetailDTO.getTechnicianId(), orderDetailDTO);
        }
        Preconditions.checkArgument(!CollectionUtils.isEmpty(technicianIds), WarnMessage.NOT_FOUND_ORDER);
        List<Technician> technicians = technicianQueryService.listTechnicianByIds(technicianIds);
        List<WsResult> result = Lists.newArrayList();
        for (Technician technician : technicians) {
            OrderDetailDTO orderDetailDTO = mapOrderDetail.get(technician.getId());
            if (StringUtils.isNotBlank(technician.getWechatId())) {
                result.add(wcService.sendTemplateMessage(technician.getWechatId(), formId, DateUtils.formatDate(orderDetailDTO.getBookTime()),
                        orderDetailDTO.getPrice().toString(), orderDetailDTO.getProjectName(), "?id=" + orderDetailDTO.getId().toString()));
            }
        }
        return wrapperJsonView(result);
    }
}
