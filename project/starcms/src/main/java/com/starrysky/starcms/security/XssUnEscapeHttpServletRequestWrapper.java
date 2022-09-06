package com.starrysky.starcms.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName XssHttpServletRequestWrapper
 * @Description
 * @Author adi
 * @Date 2022-08-17 14:52
 */
public class XssUnEscapeHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssUnEscapeHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(value != null)
            return HtmlUtils.htmlUnescape(value);
        else
            return null;
    }

    @Override
    public String getQueryString() {
        String target = super.getQueryString();
        return target == null ? null : HtmlUtils.htmlUnescape(target);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(value != null)
            return HtmlUtils.htmlUnescape(value);
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
            parameterValues[i] = HtmlUtils.htmlUnescape(value);
        }
        return parameterValues;
    }

//    private static ObjectMapper objectMapper = new ObjectMapper();
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//        String str=getRequestBody(super.getInputStream());
//        Map<String,Object> map= objectMapper.readValue(str, Map.class);
//        Map<String,Object> resultMap=new HashMap<>(map.size());
//        for(String key:map.keySet()){
//            Object val=map.get(key);
//            if(map.get(key) instanceof String){
//                resultMap.put(key,HtmlUtils.htmlUnescape(val.toString()));
//            }else{
//                resultMap.put(key,val);
//            }
//        }
//
//        str=objectMapper.writeValueAsString(resultMap);
//        final ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
//        return new ServletInputStream() {
//            @Override
//            public int read() throws IOException {
//                return bais.read();
//            }
//            @Override
//            public boolean isFinished() {
//                return false;
//            }
//            @Override
//            public boolean isReady() {
//                return false;
//            }
//            @Override
//            public void setReadListener(ReadListener listener) {
//            }
//        };
//    }
//
//    private String getRequestBody(InputStream stream) {
//        String line = "";
//        StringBuilder body = new StringBuilder();
//        int counter = 0;
//
//        // 读取POST提交的数据内容
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
//        try {
//            while ((line = reader.readLine()) != null) {
//                body.append(line);
//                counter++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return body.toString();
//    }
}
