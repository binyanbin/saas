package com.bzw.api.module.main.controller;

import com.bzw.api.module.main.service.CustomerQueryService;
import com.bzw.api.module.main.service.RoomQueryService;
import com.bzw.api.module.main.service.TechnicianQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("branch")
public class BranchController extends BaseController {

    @Autowired
    private CustomerQueryService customerQueryService;

    @Autowired
    private RoomQueryService roomQueryService;

    @Autowired
    private TechnicianQueryService technicianQueryService;

    @RequestMapping("/{branchId}/rooms")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listRooms(@PathVariable Long branchId) {
        return wrapperJsonView(roomQueryService.listRoomsByBranchId(branchId));
    }

    @RequestMapping("/{branchId}/project/{projectId}/rooms")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listRooms(@PathVariable Long branchId,@PathVariable Integer projectId) {
        return wrapperJsonView(roomQueryService.listRoomsByBranchId(branchId,projectId));
    }

    @RequestMapping("/{branchId}/technicians")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listTechnicians(@PathVariable Long branchId, @RequestParam(required = false,defaultValue = "1") Integer sort){
        return wrapperJsonView(technicianQueryService.listTechnicianByBranchId(branchId,sort));
    }

    @RequestMapping("/{branchId}/projects")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listProjects(@PathVariable Long branchId){
        return wrapperJsonView(customerQueryService.listProjectsByBranchId(branchId));
    }
}
