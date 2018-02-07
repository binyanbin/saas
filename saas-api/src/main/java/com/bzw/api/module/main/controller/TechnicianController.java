package com.bzw.api.module.main.controller;

import com.bzw.api.module.img.service.ImageService;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.api.module.main.params.PhotoParam;
import com.bzw.api.module.main.params.TechnicianParam;
import com.bzw.api.module.main.service.TechnicianEventService;
import com.bzw.api.module.main.service.TechnicianQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.utils.WebUtils;
import com.bzw.common.web.BaseController;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("technicians")
public class TechnicianController extends BaseController {

    @Autowired
    private TechnicianQueryService technicianQueryService;

    @Autowired
    private TechnicianEventService technicianEventService;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "{technicianId}", method = {RequestMethod.GET})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getBranch(@PathVariable Long technicianId) {
        return wrapperJsonView(technicianQueryService.getTechnicianDetail(technicianId));
    }

    private final String NO_PARAMETER = "技师内容不能为空";
    private final String NO_TECHNICIAN_AGE = "技师年龄必填";
    private final String NO_TECHNICIAN_JOBNUMBER = "技师工号必填";
    private final String NO_TECHNICIAN_NAME = "技师名称必填";
    private final String NO_TECHNICIAN_PHONE = "技师手机号必填";
    private final String NO_TECHNICIAN_PROJECT = "技师必选一项服务";

    private void checkTechnicianParam(TechnicianParam technicianParam) {
        Preconditions.checkArgument(technicianParam != null, NO_PARAMETER);
        Preconditions.checkArgument(technicianParam.getAge() != null, NO_TECHNICIAN_AGE);
        Preconditions.checkArgument(StringUtils.isNotBlank(technicianParam.getJobNumber()), NO_TECHNICIAN_JOBNUMBER);
        Preconditions.checkArgument(StringUtils.isNotBlank(technicianParam.getName()), NO_TECHNICIAN_NAME);
        Preconditions.checkArgument(StringUtils.isNotBlank(technicianParam.getPhone()), NO_TECHNICIAN_PHONE);
        Preconditions.checkArgument(!CollectionUtils.isEmpty(technicianParam.getProjectIds()), NO_TECHNICIAN_PROJECT);
    }

    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object add(@RequestBody TechnicianParam technicianParam) {
        checkTechnicianParam(technicianParam);
        return wrapperJsonView(technicianEventService.add(technicianParam, WebUtils.Session.getTenantId(), WebUtils.Session.getBranchId(), WebUtils.Session.getBranchName(), WebUtils.Session.getEmployeeId()));
    }

    @RequestMapping(value = "{technicianId}", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object update(@RequestBody TechnicianParam technicianParam, @PathVariable Long technicianId) {
        checkTechnicianParam(technicianParam);
        boolean result = technicianEventService.update(technicianParam, technicianId, WebUtils.Session.getEmployeeId());
        if (result) {
            return wrapperJsonView(true, WarnMessage.UPDATE_SUCCESS);
        } else {
            return wrapperJsonView(true, WarnMessage.UPDATE_FAIL);
        }
    }

    @RequestMapping(value = "{technicianId}", method = {RequestMethod.DELETE, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object delete(@PathVariable Long technicianId) {
        boolean result = technicianEventService.delete(technicianId, WebUtils.Session.getEmployeeId());
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(true, WarnMessage.DELETE_FAIL);
        }
    }

    private final String NO_PHOTO_ID = "图片id不能为空";
    private final String NO_PHOTO_NAME = "图片名称不能为空";
    private final String NO_PHOTO_URL = "图片地址不能为空";

    private void checkTechnicianPhoto(List<PhotoParam> photoParams) {
        for (PhotoParam photoParam :photoParams) {
            Preconditions.checkArgument(StringUtils.isNotBlank(photoParam.getImageId()), NO_PHOTO_ID);
            Preconditions.checkArgument(StringUtils.isNotBlank(photoParam.getName()), NO_PHOTO_NAME);
            Preconditions.checkArgument(StringUtils.isNotBlank(photoParam.getUrl()), NO_PHOTO_URL);
        }
    }

    @RequestMapping(value = "{technicianId}/photos/", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object addPhoto(@PathVariable Long technicianId, @RequestBody List<PhotoParam> photoParam) {
        checkTechnicianPhoto(photoParam);
        technicianEventService.addPhoto(photoParam, technicianId, WebUtils.Session.getBranchId(), WebUtils.Session.getTenantId());
        return wrapperJsonView(true);
    }

    @RequestMapping(value = "{technicianId}/photos/{imageId}", method = {RequestMethod.DELETE, RequestMethod.OPTIONS})
    @ApiMethodAttribute(nonSignatureValidation = true)
    public Object deletePhoto(@PathVariable Long technicianId, @PathVariable String imageId) {
        imageService.delete(imageId);
        boolean result = technicianEventService.deletePhoto(technicianId, imageId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.DELETE_FAIL);
        }
    }


}
