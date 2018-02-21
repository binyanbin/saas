package com.bzw.api.module.img.service;

import com.bzw.api.module.base.model.Picture;
import com.bzw.api.module.img.biz.PictureEventBiz;
import com.bzw.api.module.img.biz.PictureQueryBiz;
import com.bzw.api.module.img.constants.Constants;
import com.bzw.api.module.img.dto.ImageDTO;
import com.bzw.common.system.Status;
import com.bzw.common.exception.api.NotFoundImageException;
import com.bzw.common.utils.DtUtils;
import com.bzw.common.utils.Image;
import com.bzw.common.utils.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author yanbin
 */
@Service
public class ImageService {

    @Autowired
    private PictureEventBiz pictureEventBiz;

    @Autowired
    private PictureQueryBiz pictureQueryBiz;

    @Autowired
    private Constants constants;

    public ImageDTO saveImage(BufferedImage originalImage, Long tenantId, Long size) throws IOException, ExecutionException, InterruptedException {
        String fileName = UuidUtil.newUuidString();
        String date = DtUtils.toDayNumber(new Date());
        String todayFolder = constants.getLibPath() + "/" + date;
        File todayFolderFile = new File(todayFolder);
        if (!todayFolderFile.exists()) {
            todayFolderFile.mkdirs();
        }
        String originalImageFilePath = todayFolder + "/" + fileName + "-o.jpg";
        String imageFilePath = todayFolder + "/" + fileName + "_" + constants.getMaxWidth().toString() + ".jpg";
        CompletableFuture<Boolean> isSave = saveImage(originalImage, originalImageFilePath, imageFilePath);

        String imgId = pictureEventBiz.add(imageFilePath, originalImageFilePath, tenantId, size);
        if (isSave.get()) {
            return new ImageDTO(imgId,constants.getUrl() + imgId);
        } else {
            return null;
        }
    }

    @Async
    CompletableFuture<Boolean> saveImage(BufferedImage originalImage, String originalImageFilePath, String imageFilePath) throws IOException {
        BufferedImage resizedImage = null;
        if (originalImage.getWidth() > constants.getMaxWidth()) {
            resizedImage = Image.resizeImageWithHint(originalImage, constants.getMaxWidth());
        }
        File originalImageFile = new File(originalImageFilePath);
        ImageIO.write(originalImage, "jpg", originalImageFile);
        if (resizedImage != null) {
            File imageFile = new File(imageFilePath);
            ImageIO.write(resizedImage, "jpg", imageFile);
        }
        return CompletableFuture.completedFuture(true);
    }

    public boolean delete(String imageId) {
        Picture picture = pictureQueryBiz.getPicture(imageId);
        if (picture != null) {
            return false;
        } else {
            picture.setStatusId(Status.Delete.getValue());
            return pictureEventBiz.update(picture);
        }
    }

    public byte[] getBytesByImageId(String imageId, Integer width) throws IOException {
        Picture picture = pictureQueryBiz.getPicture(imageId);
        if (picture == null) {
            throw new NotFoundImageException();
        }
        if (width > constants.getMaxWidth()) {
            width = constants.getMaxWidth();
        }
        return Image.getImageBytes(picture.getOriginalPath(), width);
    }

    public byte[] getBytesByImageId(String imageId) throws Exception {
        Picture picture = pictureQueryBiz.getPicture(imageId);
        if (picture == null) {
            throw new NotFoundImageException();
        }
        return Image.getImageBytes(picture.getOriginalPath(), 0);
    }

    public byte[] getStandardBytesByImageId(String imageId) throws Exception {
        Picture picture = pictureQueryBiz.getPicture(imageId);
        if (picture == null) {
            throw new NotFoundImageException();
        }
        if (StringUtils.isNotBlank(picture.getFilePath())) {
            return Image.getImageBytes(picture.getFilePath(), 0);
        } else {
            return Image.getImageBytes(picture.getOriginalPath(), 0);
        }
    }
}
