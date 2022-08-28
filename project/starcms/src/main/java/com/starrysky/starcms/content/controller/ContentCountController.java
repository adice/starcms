package com.starrysky.starcms.content.controller;

import com.starrysky.starcms.content.service.ContentCountService;
import com.starrysky.starcms.security.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @ClassName ContentCountController
 * @Description 用于对内容进行统计的控制器
 * @Author adi
 * @Date 2022-08-27 20:21
 */
@Controller
@RequestMapping("/backstage/contentcount")
public class ContentCountController {
    @Resource
    private ContentCountService contentCountService;

    @PostMapping("/heatmap")
    @ResponseBody
    public LinkedHashMap<String, Integer> countHeat() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.contentCountService.countByUserAndYear(securityUser.getId());
    }
}
