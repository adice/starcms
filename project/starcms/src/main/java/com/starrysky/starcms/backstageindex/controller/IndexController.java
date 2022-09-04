package com.starrysky.starcms.backstageindex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName IndexController1
 * @Description
 * @Author adi
 * @Date 2022-09-04 17:04
 */
@Controller
@RequestMapping("/backstage")
public class IndexController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        request.setAttribute("activemenu", "homemenu");
        return "/backstage/index";
    }

    @GetMapping("/loginpage")
    public String login() {
        return "/backstage/login";
    }

    @RequestMapping("/unauthen")
    public String unauthen() {
        return "/backstage/unauthen";
    }

    //    @Resource
//    private Producer captchaProducer;

//    @RequestMapping("/createvcode")
//    public void createCode(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession();
//        response.setDateHeader("Expires", 0);
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        response.setHeader("Pragma", "no-cache");
//        response.setContentType("image/jpg");
//        //生成验证码
//        String capText = captchaProducer.createText();
//        session.setAttribute(Constant.KAPTCHA_SESSION_KEY, capText);
//        //向客户端写出
//        ServletOutputStream out = null;
//        try {
//            BufferedImage bi = captchaProducer.createImage(capText);
//            out = response.getOutputStream();
//            ImageIO.write(bi, "jpg", out);
//            out.flush();
//        } catch(Exception e){
//            e.printStackTrace();
//        } finally {
//            try {
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
