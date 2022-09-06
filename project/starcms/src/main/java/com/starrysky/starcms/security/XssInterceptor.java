package com.starrysky.starcms.security;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName XssInterceptor
 * @Description 该拦截器用于包装请求，转译和反转译HTML
 * @Author adi
 * @Date 2022-09-04 21:13
 */
public class XssInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("xss prehandle");
        XssHttpServletRequestWrapper xssRequestWrapper = new XssHttpServletRequestWrapper(request);
        request = xssRequestWrapper;

        System.out.println(xssRequestWrapper.getParameter("title"));
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("xss posthandle");
        XssUnEscapeHttpServletRequestWrapper xssUnEscapeRequestWrapper = new XssUnEscapeHttpServletRequestWrapper(request);
        super.postHandle(xssUnEscapeRequestWrapper, response, handler, modelAndView);
    }
}
