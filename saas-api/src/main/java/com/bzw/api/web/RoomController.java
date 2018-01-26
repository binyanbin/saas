package com.bzw.api.web;

import com.bzw.api.module.basic.service.*;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private CustomerQueryService customerQueryService;

    @Autowired
    private CustomerEventService customerEventService;

    @Autowired
    private RoomEventService roomEventService;

    @Autowired
    private WcService wcService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private RoomQueryService roomQueryService;

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
        return wrapperJsonView(roomEventService.book(roomId,WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value="/{roomId}/cancel", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object cancelRoom(@PathVariable Long roomId){
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
        stream.write(wcService.getGrCode( accessToken, "room_" + roomId.toString()));
        stream.flush();
        stream.close();
    }
}
