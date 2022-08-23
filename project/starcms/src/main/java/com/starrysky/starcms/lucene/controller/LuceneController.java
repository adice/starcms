package com.starrysky.starcms.lucene.controller;

import com.starrysky.starcms.lucene.service.LuceneService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName LuceneController
 * @Description
 * @Author adi
 * @Date 2022-08-23 15:46
 */
@Controller
@RequestMapping("/backstage")
public class LuceneController {
    @Resource
    private LuceneService luceneService;

    @RequestMapping("/lucene/list")
    public String list(HttpServletRequest request) {
        request.setAttribute("activemenu", "sysmenu");
        return "/backstage/lucene/list";
    }

    @GetMapping("/lucene/init")
    public String init(HttpServletRequest request) {
        try {
            luceneService.initIndex();
            request.setAttribute("luceneinfo", "初始化索引成功");
        } catch (Exception e) {
            request.setAttribute("luceneinfo", "初始化索引失败，请稍后重试");
            e.printStackTrace();
        }
        return "forward:/backstage/lucene/list";
    }
}
