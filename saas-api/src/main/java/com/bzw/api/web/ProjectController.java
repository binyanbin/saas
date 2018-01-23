package com.bzw.api.web;

import com.bzw.api.module.basic.service.CustomerQueryService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author yanbin
 */
@RestController
@RequestMapping("projects")
public class ProjectController extends BaseController {

    @Autowired
    private CustomerQueryService customerQueryService;

    @RequestMapping("/{projectId}/technicians")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listTechnicians(@PathVariable Integer projectId) {
        return wrapperJsonView(customerQueryService.listTechnicianByProjectId(projectId));
    }

    @RequestMapping("/{projectId}/rooms")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object listRooms(@PathVariable Integer projectId){
        return wrapperJsonView(customerQueryService.listRoomByProjectId(projectId));
    }

    @RequestMapping("/{projectId}")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object getProject(@PathVariable Integer projectId){
        return wrapperJsonView(customerQueryService.getProject(projectId));
    }

}
