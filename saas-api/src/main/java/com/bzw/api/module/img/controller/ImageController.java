package com.bzw.api.module.img.controller;

import com.bzw.api.module.img.service.ImageService;
import com.bzw.api.module.main.constant.WarnMessage;
import com.bzw.common.content.ApiMethodAttribute;
import com.bzw.common.exception.api.NotFoundImageException;
import com.bzw.common.web.BaseController;
import com.bzw.common.web.JsonWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author yanbin
 */
@RestController
@RequestMapping("img")
public class ImageController extends BaseController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private JsonWrapperService jsonWrapperService;

    @RequestMapping(value = "upload", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object upload(@RequestParam("img") MultipartFile uploadFile) throws IOException, ExecutionException, InterruptedException {
        BufferedImage originalImage = ImageIO.read(uploadFile.getInputStream());
        return wrapperJsonView(imageService.saveImage(originalImage, 0L, uploadFile.getSize()));
    }

    @RequestMapping(value = "{imageId}", method = {RequestMethod.GET}, produces = MediaType.IMAGE_PNG_VALUE)
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public byte[] get(@PathVariable("imageId") String imageId, HttpServletResponse response) throws IOException {
        try {
            return imageService.getBytesByImageId(imageId);
        } catch (Exception e) {
            response.sendRedirect("/img/404");
            return null;
        }
    }

    @RequestMapping(value = "{imageId}", method = {RequestMethod.DELETE, RequestMethod.OPTIONS}, produces = MediaType.IMAGE_PNG_VALUE)
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object delete(@PathVariable("imageId") String imageId) {
        boolean result = imageService.delete(imageId);
        if (result) {
            return wrapperJsonView(true, WarnMessage.DELETE_SUCCESS);
        } else {
            return wrapperJsonView(false, WarnMessage.DELETE_FAIL);
        }
    }

    @RequestMapping(value = "{imageId}/standard", method = {RequestMethod.GET}, produces = MediaType.IMAGE_PNG_VALUE)
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public byte[] getStandard(@PathVariable("imageId") String imageId, HttpServletResponse response) throws IOException {
        try {
            return imageService.getStandardBytesByImageId(imageId);
        } catch (Exception e) {
            response.sendRedirect("/img/404");
            return null;
        }
    }

    @RequestMapping(value = "404")
    @ApiMethodAttribute(nonSessionValidation = true, nonSignatureValidation = true)
    public Object notFound() {
        return jsonWrapperService.getJsonExceptionWrapper(new NotFoundImageException());
    }
}
