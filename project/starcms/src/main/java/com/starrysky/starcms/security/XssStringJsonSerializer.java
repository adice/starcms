package com.starrysky.starcms.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * @ClassName XssStringJsonSerializer
 * @Description
 * @Author adi
 * @Date 2022-08-17 14:55
 */
public class XssStringJsonSerializer extends JsonSerializer {
    @Override
    public void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (obj != null) {
            jsonGenerator.writeString(HtmlUtils.htmlEscape(obj.toString()));
        }
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }

}
