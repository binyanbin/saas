package com.bzw.api.module.main.controller;

import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.params.RoomParam;
import com.bzw.api.module.main.service.*;
import com.bzw.api.module.third.service.WcService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("rooms")
public class RoomController extends BaseController {

    @Autowired
    private RoomEventService roomEventService;

    @Autowired
    private WcService wcService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private RoomQueryService roomQueryService;

    @Autowired
    private ProjectQueryService projectQueryService;

    @Autowired
    private BranchQueryService branchQueryService;

    @RequestMapping("/{roomId}/branch")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getBranch(@PathVariable Long roomId) {
        return wrapperJsonView(branchQueryService.getBranchByRoomId(roomId));
    }

    @RequestMapping("/{roomId}/projects")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listProjects(@PathVariable Long roomId) {
        return wrapperJsonView(projectQueryService.listProjectsByRoomId(roomId));
    }

    @RequestMapping("/{roomId}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getRoom(@PathVariable Long roomId) {
        return wrapperJsonView(roomQueryService.getRoom(roomId));
    }

    @RequestMapping("/{roomId}/order")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getOrder(@PathVariable Long roomId) {
        return wrapperJsonView(orderQueryService.getOrderByRoomId(roomId));
    }

    @RequestMapping(value = "/{roomId}/book", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object bookRoom(@PathVariable Long roomId) {
        return wrapperJsonView(roomEventService.book(roomId, WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "/{roomId}/finish", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object finishRoom(@PathVariable Long roomId) {
        return wrapperJsonView(roomEventService.finish(roomId, WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "/{roomId}/cancel", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object cancelRoom(@PathVariable Long roomId) {
        return wrapperJsonView(roomEventService.cancel(roomId, WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "/{roomId}/open", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object open(@PathVariable Long roomId) {
        return wrapperJsonView(roomEventService.open(roomId, WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "{roomId}/qrcode", method = {RequestMethod.GET})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public void qrCode(@PathVariable Long roomId, HttpServletResponse response) throws IOException {
        String accessToken = wcService.getAccessToken();
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(wcService.getGrCode(accessToken, "room_" + roomId.toString()));
        stream.flush();
        stream.close();
    }

    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object add(@RequestBody RoomParam roomParam) {
        checkPostRoomParams(roomParam);
        return wrapperJsonView(roomEventService.add(roomParam, WebUtils.Session.getBranchId(), WebUtils.Session.getBranchName(), WebUtils.Session.getTenantId(), WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "/{roomId}", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object update(@RequestBody RoomParam roomParam, @PathVariable Long roomId) {
        checkPostRoomParams(roomParam);
        boolean result = roomEventService.update(roomParam, roomId, WebUtils.Session.getEmployeeId());
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.DELETE_FAIL);
        }
    }

    private final String NO_PARAMETER = "房间内容不存在";
    private final String NO_ROOM_NUMBER = "房间床数必填";
    private final String NO_ROOM_NAME = "房号必填";
    private final String NO_ROOM_TYPE = "房间类型必填";

    private void checkPostRoomParams(@RequestBody RoomParam roomParam) {
        Preconditions.checkArgument(roomParam != null, NO_PARAMETER);
        Preconditions.checkArgument(roomParam.getBedNumber() != null, NO_ROOM_NUMBER);
        Preconditions.checkArgument(StringUtils.isNotBlank(roomParam.getName()), NO_ROOM_NAME);
        Preconditions.checkArgument(roomParam.getType() != null, NO_ROOM_TYPE);
    }

    @RequestMapping(value = "type", method = {RequestMethod.GET})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object type() {
        return wrapperJsonView(projectQueryService.listType());
    }

    @RequestMapping(value = "{roomId}", method = {RequestMethod.DELETE, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object delete(@PathVariable Long roomId) {
        boolean result = roomEventService.delete(roomId, WebUtils.Session.getEmployeeId());
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.DELETE_FAIL);
        }
    }

}
