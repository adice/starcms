package com.starrysky.starcms.security;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @ClassName XssHttpServletRequestWrapper
 * @Description
 * @Author adi
 * @Date 2022-08-17 14:52
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(value != null)
            return HtmlUtils.htmlEscape(value);
        else
            return null;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(value != null)
            return HtmlUtils.htmlEscape(value);
        else
            return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String value = parameterValues[i];
            parameterValues[i] = HtmlUtils.htmlEscape(value);
        }
        return parameterValues;
    }
}
