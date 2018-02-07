package com.bzw.api.module.img.params;

import java.awt.image.BufferedImage;

/**
 * @author yanbin
 */
public class ImageObject {
    private BufferedImage originalImage;
    private BufferedImage imageToRec;

    public ImageObject(BufferedImage originalImage, BufferedImage imageToRec) {
        this.originalImage = originalImage;
        this.imageToRec = imageToRec;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public void setImageToRec(BufferedImage imageToRec) {
        this.imageToRec = imageToRec;
    }
}
