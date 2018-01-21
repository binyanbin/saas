package com.bzw.api.web;

import com.bzw.api.module.basic.service.CustomerEventService;
import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.api.module.basic.service.RoomEventService;
import com.bzw.api.module.basic.service.WechatService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Autowired
    CustomerQueryService customerQueryService;

    @Autowired
    CustomerEventService customerEventService;

    @Autowired
    RoomEventService roomEventService;

    @Autowired
    WechatService wechatService;

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

    @RequestMapping("/{roomId}/order")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getOrder(@PathVariable Long roomId) {
        return wrapperJsonView(customerQueryService.getOrderByRoomId(roomId));
    }

    @RequestMapping(value = "/{roomId}", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object updateRoomState(@PathVariable Long roomId, @RequestParam Integer statusId) {
        Preconditions.checkArgument(customerQueryService.getRoom(roomId) != null, "房间id不存在");
        return wrapperJsonView(roomEventService.updateRoomState(roomId, statusId, WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "{roomId}/qrcode", method = {RequestMethod.GET})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public void qrCode(@PathVariable Long roomId, HttpServletResponse response) throws IOException {
        String accessToken = wechatService.getAccessToken(appid, secret);
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(wechatService.getGrCode("pages/projectlist/projectlist", accessToken, "room_" + roomId.toString()));
        stream.flush();
        stream.close();
    }


}
