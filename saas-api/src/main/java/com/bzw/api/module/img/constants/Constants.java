package com.bzw.api.module.img.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${imageService.path}")
    private String libPath;

    @Value("${imageService.with}")
    private Integer maxWidth;

    @Value("${imageService.url}")
    private String url;

    public String getLibPath() {
        return libPath;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public String getUrl() {
        return url;
    }
}
