package com.bzw.api.web;

import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("technicians")
public class TechnicianController extends BaseController {

    @Autowired
    private CustomerQueryService customerQueryService;

    @RequestMapping("/{technicianId}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getBranch(@PathVariable Long technicianId) {
        return wrapperJsonView(customerQueryService.getTechnicianDetail(technicianId));
    }

}