package com.bzw.api.web;

import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rooms")
public class RoomController extends BaseController {

    @Autowired
    CustomerQueryService customerQueryService;

    @Autowired
    CustomerEventService customerEventService;

    @RequestMapping("/{roomId}/branch")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getBranch(@PathVariable Long roomId) {
        return wrapperJsonView(customerQueryService.getBranchByRoomId(roomId));
    }

    @RequestMapping("/{roomId}/projects")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listProjects(@PathVariable Long roomId) {
        return wrapperJsonView(customerQueryService.listProjectsByRoomId(roomId));
    }

    @RequestMapping("/{roomId}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getRoom(@PathVariable Long roomId) {
        return wrapperJsonView(customerQueryService.getRoom(roomId));
    }

    @RequestMapping(value = "/{roomId}", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object updateRoomState(@PathVariable Long roomId, @RequestParam Integer statusId) {
        Preconditions.checkArgument(customerQueryService.getRoom(roomId) != null, "房间id不存在");
        return wrapperJsonView(customerEventService.updateRoomState(roomId, statusId, WebUtils.Session.getEmployeeId()));
    }

}
