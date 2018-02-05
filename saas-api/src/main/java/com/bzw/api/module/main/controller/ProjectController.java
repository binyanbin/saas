package com.bzw.api.module.main.controller;

import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.params.ProjectParam;
import com.bzw.api.module.main.service.ProjectEventService;
import com.bzw.api.module.main.service.ProjectQueryService;
import com.bzw.api.module.main.service.RoomQueryService;
import com.bzw.api.module.main.service.TechnicianQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("projects")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectQueryService projectQueryService;

    @Autowired
    private TechnicianQueryService technicianQueryService;

    @Autowired
    private RoomQueryService roomQueryService;

    @Autowired
    private ProjectEventService projectEventService;

    @RequestMapping("{projectId}/technicians")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listTechnicians(@PathVariable Integer projectId) {
        return wrapperJsonView(technicianQueryService.listTechnicianByProjectId(projectId));
    }

    @RequestMapping("{projectId}/rooms")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listRooms(@PathVariable Integer projectId) {
        return wrapperJsonView(roomQueryService.listRoomByProjectId(projectId));
    }

    @RequestMapping("{projectId}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getProject(@PathVariable Integer projectId) {
        return wrapperJsonView(projectQueryService.getProject(projectId));
    }

    @RequestMapping(value = "/", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object add(@RequestBody ProjectParam projectParam) {
        return wrapperJsonView(projectEventService.add(projectParam, WebUtils.Session.getTenantId(), WebUtils.Session.getBranchId(), WebUtils.Session.getBranchName()));
    }

    @RequestMapping(value = "{projectId}/update", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object add(@RequestBody ProjectParam projectParam,@PathVariable Integer projectId) {
        boolean result = projectEventService.update(projectParam,projectId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.UPDATE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.UPDATE_FAIL);
        }
    }

    @RequestMapping(value = "type",method = {RequestMethod.GET})
    @ApiMethodAttribute(nonSignatureValidation = true,nonSessionValidation = true)
    public Object listTypes() {
        return wrapperJsonView(projectQueryService.listType());
    }

    @RequestMapping(value = "{projectId}", method = {RequestMethod.OPTIONS, RequestMethod.DELETE})
    public Object delete(@PathVariable Integer projectId) {
        boolean result = projectEventService.delete(projectId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.DELETE_FAIL);
        }
    }

}
