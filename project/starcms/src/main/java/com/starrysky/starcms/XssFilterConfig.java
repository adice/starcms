package com.starrysky.starcms;

import com.starrysky.starcms.security.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @ClassName XssFilterConfig
 * @Description 配置XssFilter生效，因为WebFilter和Component注解冲突，导致WebFilter的urlpattern无效，
 * 因此使用Config的方式来配置过滤器，此过滤器用于将传入内容进行转译.
 * @Author adi
 * @Date 2022-09-04 19:30
 */
@Configuration
public class XssFilterConfig {
    @Bean
    public FilterRegistrationBean webAuthFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(xssFilter());
        registration.setName("XssFilter");
        registration.addUrlPatterns("/backstage/user/add", "/backstage/user/edit", "/backstage/user/list",
                "/backstage/journal/add", "/backstage/journal/edit", "/backstage/journal/list",
                "/backstage/content/add", "/backstage/content/edit", "/backstage/content/list",
                "/backstage/lucene/search");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public Filter xssFilter() {
        return new XssFilter();
    }
}
