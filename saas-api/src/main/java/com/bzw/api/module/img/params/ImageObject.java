package com.bzw.api.module.img.params;

import java.awt.image.BufferedImage;

public class ImageObject {
    private BufferedImage originalImage;
    private BufferedImage imageToRec;

    public ImageObject(BufferedImage _originalImage, BufferedImage _imageToRec) {
        this.originalImage = _originalImage;
        this.imageToRec = _imageToRec;
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public void setImageToRec(BufferedImage imageToRec) {
        this.imageToRec = imageToRec;
    }
}
