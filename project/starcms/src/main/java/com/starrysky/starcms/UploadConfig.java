package com.starrysky.starcms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName UploadWebAppConfigurer
 * @Description
 * @Author adi
 * @Date 2020-03-28 15:49
 */
@Configuration
public class UploadConfig implements WebMvcConfigurer {
    /*上传地址*/
    @Value("${file.upload.path}")
    private String uploadPath;
    /*显示相对地址*/
    @Value("${file.upload.relative.path}")
    private String relativeUploadPath;

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(relativeUploadPath).addResourceLocations("file:" + uploadPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
