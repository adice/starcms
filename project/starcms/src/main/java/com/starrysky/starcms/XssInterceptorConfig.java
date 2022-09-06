package com.starrysky.starcms;

import com.starrysky.starcms.security.XssInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName XssInterceptorConfig
 * @Description 拦截器，用于转译Html
 * 问题，拦截包装的request，在控制器中没有生效
 * @Author adi
 * @Date 2022-09-04 21:18
 */
//@Configuration
public class XssInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new XssInterceptor()).addPathPatterns("/backstage/user/add", "/backstage/user/edit", "/backstage/user/list",
                "/backstage/journal/add", "/backstage/journal/edit", "/backstage/journal/list",
                "/backstage/content/add", "/backstage/content/edit", "/backstage/content/list",
                "/backstage/lucene/search");
    }
}
