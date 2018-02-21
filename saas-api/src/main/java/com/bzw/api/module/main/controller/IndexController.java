package com.bzw.api.module.main.controller;

import com.bzw.api.module.base.dao.FunctionMapper;
import com.bzw.api.module.base.model.Function;
import com.bzw.api.module.third.service.BaiduService;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.system.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author yanbin
 */
@RestController
public class IndexController {

    @Value("${application.title}")
    private String title;

    @Value("${application.version}")
    private String version;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private BaiduService baiduService;

    @RequestMapping("/")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object index() {
        return title + " verison:" + version;
    }

    @RequestMapping(value = "/voice")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public void getVoice(HttpServletResponse response, @RequestParam String voice) throws IOException {
        response.setContentType("audio/mp3");
        OutputStream stream = response.getOutputStream();
        stream.write(baiduService.getVoice(voice));
        stream.flush();
        stream.close();
    }

    @RequestMapping("/test")
    @ApiMethodAttribute(nonSignatureValidation = true,nonSessionValidation = true)
    public Object test(){
        Page<Function> page = PageHelper.startPage(2,2);
        page.setOrderBy("id desc");
        List<Function> functionList = functionMapper.selectByExample(null);
        PageInfo<Function> result = new PageInfo<>();
        result.setItems(functionList);
        result.setPageIndex(2);
        result.setPageSize(2);
        result.setTotal(page.getTotal());
        return result;
    }
}
