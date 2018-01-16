package com.bzw.api.web;

import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("branch")
public class BranchController extends BaseController {

    @Autowired
    private CustomerQueryService customerQueryService;

    @RequestMapping("/{branchId}/rooms")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listRooms(@PathVariable Long branchId) {
        return wrapperJsonView(customerQueryService.listRoomsByBranchId(branchId));
    }

    @RequestMapping("/{branchId}/technicians")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listTechnicians(@PathVariable Long branchId, @RequestParam(required = false,defaultValue = "1") Integer sort){
        return wrapperJsonView(customerQueryService.listTechnicianByBranchId(branchId,sort));
    }

    @RequestMapping("/{branchId}/projects")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listProjects(@PathVariable Long branchId){
        return wrapperJsonView(customerQueryService.listProjectsByBranchId(branchId));
    }
}
