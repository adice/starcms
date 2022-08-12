package com.starrysky.starcms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @ClassName ReloadMessageConfig
 * @Description 用于SpringSecurity的提示信息显示中文
 * @Author adi
 * @Date 2022-08-11 17:56
 */
@Configuration
public class ReloadMessageConfig {
    @Bean //加载中文的认证提示信息
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        //.properties 不要加到后面
        messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
        return  messageSource;
    }
}
