package com.starrysky.starcms.test;

import com.starrysky.starcms.content.service.ContentService;
import com.starrysky.starcms.entity.Content;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName TestController
 * @Description
 * @Author adi
 * @Date 2022-08-18 08:44
 */
@Controller
@RequestMapping("/backstage")
public class TestController {
    @Resource
    private ContentService contentService;
    @RequestMapping("/testxss")
    public String testxss(HttpServletRequest request){
        Content content = null;
        try {
            content = contentService.getById(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("content", content);
        return "/test/testxss";
    }

}
