package com.starrysky.starcms;

import com.starrysky.starcms.channel.service.ChannelService;
import com.starrysky.starcms.entity.Channel;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * @ClassName InitChannelsApplicationRunner
 * @Description
 * @Author adi
 * @Date 2022-08-04 08:13
 */
@Component
@Order(1)
public class InitChannelsApplicationRunner implements ApplicationRunner, ServletContextAware {

    private ServletContext servletContext;
    @Resource
    private ChannelService channelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Channel> list = this.channelService.listNormal();
        this.servletContext.setAttribute("channels", list);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
