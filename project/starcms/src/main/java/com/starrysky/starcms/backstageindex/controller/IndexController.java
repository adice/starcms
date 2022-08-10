package com.starrysky.starcms.backstageindex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName IndexController
 * @Description
 * @Author adi
 * @Date 2022-07-26 16:13
 */
@Controller
@RequestMapping("/backstage")
public class IndexController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        request.setAttribute("activemenu", "homemenu");
        return "/backstage/index";
    }
}
