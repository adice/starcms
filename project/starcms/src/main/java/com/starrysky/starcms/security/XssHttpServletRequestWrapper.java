package com.starrysky.starcms.security;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.stream.Stream;

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
        return HtmlUtils.htmlEscape(value);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return HtmlUtils.htmlEscape(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        return values != null ? (String[]) Stream.of(values)
                .map(HtmlUtils::htmlEscape).toArray() :
                super.getParameterValues(name);
    }
}
