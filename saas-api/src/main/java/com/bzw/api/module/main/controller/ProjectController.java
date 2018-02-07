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
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
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
        checkProjectParam(projectParam);
        return wrapperJsonView(projectEventService.add(projectParam, WebUtils.Session.getTenantId(), WebUtils.Session.getBranchId(), WebUtils.Session.getBranchName()));
    }

    @RequestMapping(value = "{projectId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object update(@RequestBody ProjectParam projectParam, @PathVariable Integer projectId) {
        checkProjectParam(projectParam);
        boolean result = projectEventService.update(projectParam, projectId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.UPDATE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.UPDATE_FAIL);
        }
    }

    private final String NO_PARAMETER = "项目内容不存在";
    private final String NO_PROJECT_NAME = "项目名称必填";
    private final String NO_PROJECT_DURATION = "项目时长必填";
    private final String NO_PROJECT_PRICE = "项目价格必填";
    private final String NO_PROJECT_TYPE = "项目类型必填";

    private void checkProjectParam(ProjectParam projectParam) {
        Preconditions.checkArgument(projectParam != null, NO_PARAMETER);
        Preconditions.checkArgument(StringUtils.isNotBlank(projectParam.getName()), NO_PROJECT_NAME);
        Preconditions.checkArgument(projectParam.getDuration() != null, NO_PROJECT_DURATION);
        Preconditions.checkArgument(projectParam.getPrice() != null, NO_PROJECT_PRICE);
        Preconditions.checkArgument(projectParam.getType() != null, NO_PROJECT_TYPE);
    }

    @RequestMapping(value = "type", method = {RequestMethod.GET})
    @ApiMethodAttribute(nonSignatureValidation = true, nonSessionValidation = true)
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
