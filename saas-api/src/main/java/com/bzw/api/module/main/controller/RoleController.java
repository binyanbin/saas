package com.bzw.api.module.main.controller;

import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.params.RoleParam;
import com.bzw.api.module.main.service.RoleEventService;
import com.bzw.api.module.main.service.RoleQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roles")
public class RoleController extends BaseController {

    @Autowired
    private RoleEventService roleEventService;

    @Autowired
    private RoleQueryService roleQueryService;

    @RequestMapping(value = "/", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object add(@RequestBody RoleParam roleParam) {
        return wrapperJsonView(roleEventService.add(roleParam));
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object list() {
        return wrapperJsonView(roleQueryService.listRoles());
    }

    @RequestMapping(value = "/{roleId}", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object update(@RequestBody RoleParam roleParam, @PathVariable Integer roleId) {
        boolean result = roleEventService.update(roleParam, roleId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.UPDATE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.UPDATE_FAIL);
        }
    }

    @RequestMapping(value = "/{roleId}", method = {RequestMethod.DELETE})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object delete(@PathVariable Integer roleId) {
        boolean result = roleEventService.delete(roleId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.DELETE_FAIL);
        }
    }


}
