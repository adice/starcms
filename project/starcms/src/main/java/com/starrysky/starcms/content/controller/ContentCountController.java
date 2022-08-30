package com.starrysky.starcms.content.controller;

import com.starrysky.starcms.content.service.ContentCountService;
import com.starrysky.starcms.entity.BackgroundUser;
import com.starrysky.starcms.entity.Channel;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.security.SecurityUser;
import com.starrysky.starcms.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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

    @PostMapping("/countstackedline")
    @ResponseBody
    public LinkedHashMap<String, LinkedHashMap<String, Integer>> countStackedLine() {
        return this.contentCountService.countByMonth();
    }

    @RequestMapping("/list")
    public String list(String beginTime, String endTime, HttpServletRequest request) {
        try {
            if(StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)){
                request.setAttribute("activemenu", "countmenu");
                return "/backstage/contentcount/list";
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(beginTime);
                date = simpleDateFormat.parse(endTime);

                List<Object[]> list = this.contentCountService.countByDateGroupByUser(beginTime, endTime);
                request.setAttribute("beginTime", beginTime);
                request.setAttribute("endTime", endTime);
                request.setAttribute("list", list);
                request.setAttribute("activemenu", "countmenu");
                return "/backstage/contentcount/list";
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("activemenu", "homemenu");
            return "/backstage/contentcount/list";
        }

    }
}
