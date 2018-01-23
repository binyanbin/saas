package com.bzw.api.web;

import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.api.module.basic.service.OrderEventService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Preconditions.checkArgument(id != null, "用户不存在或已被禁用");
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
        Preconditions.checkArgument(orderId != null, "未找到订单");
        return wrapperJsonView(customerQueryService.getOrder(orderId));
    }

    @RequestMapping(value = "/{id}/modify/client", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object modifyForClient(@PathVariable Long id, @RequestBody List<OrderParam> orderParam, @RequestParam String openId) {
        Long orderId = orderEventService.modifyOrderForClient(id, orderParam, openId);
        Preconditions.checkArgument(orderId != null, "未找到订单");
        return wrapperJsonView(customerQueryService.getOrder(orderId));
    }

    @RequestMapping(value = "detail/{detailId}/serve", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true,nonSessionValidation = true)
    public Object serve(@PathVariable Long detailId) {
        return wrapperJsonView(orderEventService.serve(detailId));
    }


}
