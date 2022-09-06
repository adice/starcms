package com.starrysky.starcms.lucene.controller;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.lucene.service.LuceneService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

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

    @RequestMapping("/lucene/search")
    public String search(@RequestParam("keywords") String keywords, @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                         @RequestParam(name = "pageNum", required = false, defaultValue = "10") int pageSize, HttpServletRequest request) {
        if (keywords != null && !keywords.equals("")) {
            try {
                Page<Content> page = this.luceneService.search(keywords, pageNum, pageSize);
                request.setAttribute("page", page);
                // 反转译HTML内容
                if(keywords != null)
                    request.setAttribute("keywords", HtmlUtils.htmlUnescape(keywords));
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("luceneinfo", "检索出错，请稍后再试");
            }
        } else {
            request.setAttribute("luceneinfo", "请输入检索词再检索");
        }
        return "/backstage/lucene/result";
    }
}
