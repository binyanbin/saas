package com.bzw.api.module.main.controller;

import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.params.BranchParam;
import com.bzw.api.module.main.service.*;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("branch")
public class BranchController extends BaseController {

    @Autowired
    private ProjectQueryService projectQueryService;

    @Autowired
    private RoomQueryService roomQueryService;

    @Autowired
    private TechnicianQueryService technicianQueryService;

    @Autowired
    private BranchQueryService branchQueryService;

    @Autowired
    private BranchEventService branchEventService;

    @RequestMapping("/{branchId}/rooms")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listRooms(@PathVariable Long branchId) {
        return wrapperJsonView(roomQueryService.listRoomsByBranchId(branchId));
    }

    @RequestMapping("/{branchId}/project/{projectId}/rooms")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listRooms(@PathVariable Long branchId, @PathVariable Integer projectId) {
        return wrapperJsonView(roomQueryService.listRoomsByBranchId(branchId, projectId));
    }

    @RequestMapping("/{branchId}/technicians")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listTechnicians(@PathVariable Long branchId, @RequestParam(required = false, defaultValue = "1") Integer sort) {
        return wrapperJsonView(technicianQueryService.listTechnicianByBranchId(branchId, sort));
    }

    @RequestMapping("/{branchId}/projects")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listProjects(@PathVariable Long branchId) {
        return wrapperJsonView(projectQueryService.listProjectsByBranchId(branchId));
    }

    @RequestMapping("/")
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object listBranch() {
        return wrapperJsonView(branchQueryService.listBranchByTenantId(WebUtils.Session.getTenantId()));
    }

    @RequestMapping(value = "/", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object add(@RequestBody BranchParam branchParam) {
        return wrapperJsonView(branchEventService.add(branchParam, WebUtils.Session.getTenantId(), WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "/{branchId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object update(@RequestBody BranchParam branchParam, @PathVariable Long branchId) {
        boolean result = branchEventService.update(branchParam, branchId, WebUtils.Session.getEmployeeId());
        if (result) {
            return wrapperJsonView(true, WarnMessage.UPDATE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.UPDATE_FAIL);
        }
    }

    @RequestMapping(value = "/{branchId}", method = {RequestMethod.OPTIONS, RequestMethod.DELETE})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object delete(@PathVariable Long branchId) {
        boolean result = branchEventService.delete(branchId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.DELETE_FAIL);
        }
    }
}
