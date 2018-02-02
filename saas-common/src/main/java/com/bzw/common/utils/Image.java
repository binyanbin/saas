package com.bzw.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Image {

    public static BufferedImage resizeImageWithHint(BufferedImage image, int maxSideLength) {
        assert (maxSideLength > 0);
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();
        double scaleFactor;
        if (originalWidth > originalHeight) {
            scaleFactor = ((double) maxSideLength / originalWidth);
        } else {
            scaleFactor = ((double) maxSideLength / originalHeight);
        }
        // create new image
        if (scaleFactor < 1 && (int) Math.round(originalWidth * scaleFactor) > 1 && (int) Math.round(originalHeight * scaleFactor) > 1) {
//            BufferedImage img = new BufferedImage((int) Math.round(originalWidth * scaleFactor), (int) Math.round(originalHeight * scaleFactor), BufferedImage.TYPE_INT_RGB);
            BufferedImage img;
            if (image.getType() != 0)
                img = new BufferedImage((int) Math.round(originalWidth * scaleFactor), (int) Math.round(originalHeight * scaleFactor), image.getType());
            else
                img = new BufferedImage((int) Math.round(originalWidth * scaleFactor), (int) Math.round(originalHeight * scaleFactor), BufferedImage.TYPE_INT_RGB);
            // fast scale (Java 1.4 & 1.5)
            Graphics g = img.getGraphics();
//        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, img.getWidth(), img.getHeight(), null);
            return img;
        } else {
            return image;
        }
    }

    public static byte[] getImageBytes(String filePath, int maxSize) throws IOException {
        byte[] buffer;
        String ext;
        String resizedFileName;
        if (filePath.indexOf(".") > 0) {
            ext = filePath.substring(filePath.lastIndexOf("."));
            resizedFileName = filePath.substring(0, filePath.lastIndexOf(".")) + "_" + maxSize + ext;
        } else {
            resizedFileName = filePath + "_" + maxSize;
        }
        File resizedImageFile = new File(resizedFileName);
        BufferedImage resizedImage;
        if (resizedImageFile.exists()) {
            resizedImage = ImageIO.read(resizedImageFile);
        } else {
            BufferedImage originalImage = ImageIO.read(new File(filePath));
            if (maxSize > 0) {
                resizedImage = resizeImageWithHint(originalImage, maxSize);
                ImageIO.write(resizedImage, "jpg", resizedImageFile);
            } else {
                resizedImage = originalImage;
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", out);
        buffer = out.toByteArray();
        return buffer;
    }
}

