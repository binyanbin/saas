package com.bzw.api.web;

import com.bzw.api.module.basic.param.OrderParam;
import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    CustomerQueryService customerQueryService;

    @Autowired
    CustomerEventService customerEventService;

    @RequestMapping(value = "/book", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object pay(@RequestBody List<OrderParam> orderParam) {
        Long id = customerEventService.addOrder(orderParam);
        if (id != null)
            return wrapperJsonView(customerQueryService.getOrder(id));
        else
            return wrapperJsonView(null);
    }

    @RequestMapping(value = "/{id}")
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
    public Object getOrder(@PathVariable Long id) {
        return wrapperJsonView(customerQueryService.getOrder(id));
    }

    @RequestMapping(value = "/{id}/modify", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object modify(@PathVariable Long id, @RequestBody List<OrderParam> orderParam) {
        Long orderId = customerEventService.modifyOrder(id, orderParam);
        if (orderId != null)
            return wrapperJsonView(customerQueryService.getOrder(orderId));
        else
            return wrapperJsonView(null);
    }
}
