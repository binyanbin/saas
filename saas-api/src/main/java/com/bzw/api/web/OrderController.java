package com.bzw.api.web;

import com.bzw.api.module.basic.constant.ErrorMessages;
import com.bzw.api.module.basic.dto.OrderDetailDTO;
import com.bzw.api.module.basic.model.Technician;
import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.api.module.basic.service.OrderEventService;
import com.bzw.api.module.basic.service.WeChatService;
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
    CustomerQueryService customerQueryService;

    @Autowired
    CustomerEventService customerEventService;

    @Autowired
    OrderEventService orderEventService;

    @Autowired
    WeChatService weChatService;

    @RequestMapping(value = "room/{roomId}/clientBook", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object clientBook(@RequestBody List<OrderParam> orderParam, @PathVariable Long roomId, @RequestParam String openId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(openId), "openId不存在");
        Long id = orderEventService.addOrderForClient(orderParam, openId, roomId);
        if (id != null) {
            return wrapperJsonView(customerQueryService.getOrder(id));
        } else {
            return wrapperJsonView(null);
        }
    }

    @RequestMapping(value = "room/{roomId}/deskBook", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object desckBook(@RequestBody List<OrderParam> orderParam, @PathVariable Long roomId) {
        Long id = orderEventService.addOrderForDesk(orderParam, WebUtils.Session.getUserId(), roomId);
        Preconditions.checkArgument(id != null, ErrorMessages.USER_NOT_EXISTS);
        return wrapperJsonView(customerQueryService.getOrder(id));
    }

    @RequestMapping(value = "/{id}")
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object getOrder(@PathVariable Long id) {
        return wrapperJsonView(customerQueryService.getOrder(id));
    }

    @RequestMapping(value = "/{id}/modify/desk", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object modifyForDesk(@PathVariable Long id, @RequestBody List<OrderParam> orderParam) {
        Long orderId = orderEventService.modifyOrderForDesk(id, orderParam, WebUtils.Session.getUserId());
        Preconditions.checkArgument(orderId != null, ErrorMessages.NOT_FOUND_ORDER);
        return wrapperJsonView(customerQueryService.getOrder(orderId));
    }

    @RequestMapping(value = "/{id}/modify/client", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object modifyForClient(@PathVariable Long id, @RequestBody List<OrderParam> orderParam, @RequestParam String openId) {
        Long orderId = orderEventService.modifyOrderForClient(id, orderParam, openId);
        Preconditions.checkArgument(orderId != null, ErrorMessages.NOT_FOUND_ORDER);
        return wrapperJsonView(customerQueryService.getOrder(orderId));
    }

    @RequestMapping(value = "detail/{detailId}/serve", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object serve(@PathVariable Long detailId) {
        return wrapperJsonView(orderEventService.serve(detailId));
    }

    @RequestMapping(value = "{id}/templateMessage", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object sendTemplateMessage(@PathVariable Long id, @RequestParam String formId) {
        List<OrderDetailDTO> orderDetails = customerQueryService.listOrderDetail(id);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(orderDetails),ErrorMessages.NOT_FOUND_ORDER);
        List<Long> technicianIds = Lists.newArrayList();
        Map<Long, OrderDetailDTO> mapOrderDetail = Maps.newHashMap();
        for (OrderDetailDTO orderDetailDTO : orderDetails) {
            technicianIds.add(orderDetailDTO.getTechnicianId());
            mapOrderDetail.put(orderDetailDTO.getTechnicianId(), orderDetailDTO);
        }
        Preconditions.checkArgument(!CollectionUtils.isEmpty(technicianIds),ErrorMessages.NOT_FOUND_ORDER);
        List<Technician> technicians = customerQueryService.listTechnicianByIds(technicianIds);
        for (Technician technician : technicians) {
            OrderDetailDTO orderDetailDTO = mapOrderDetail.get(technician.getId());
            if (StringUtils.isNotBlank(technician.getWechatId())) {
                weChatService.sendTemplateMessage(technician.getWechatId(), formId, DateUtils.formatDate(orderDetailDTO.getBookTime()),
                        orderDetailDTO.getPrice().toString(), orderDetailDTO.getProjectName(),"?id="+orderDetailDTO.getId().toString());
            }
        }
        return wrapperJsonView(true);
    }
}
