package com.starrysky.starcms.security;

import com.starrysky.starcms.entity.BackgroundUser;
import com.starrysky.starcms.entity.Channel;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentPic;
import org.springframework.web.util.HtmlUtils;

/**
 * @ClassName ObjectHtmlUnEscapeUtil
 * @Description 该工具类用于将Content、Channel、User的属性进行Html格式转换
 * @Author adi
 * @Date 2022-09-03 20:35
 */
public class HtmlUnEscapeUtil {

    public static Content unEscapeContent(Content content) {
        if(content != null){
            content.setTitle(HtmlUtils.htmlUnescape(content.getTitle()));
            content.setTags(HtmlUtils.htmlUnescape(content.getTags()));
            content.setShortTitle(HtmlUtils.htmlUnescape(content.getShortTitle()));
            content.setTxt(HtmlUtils.htmlUnescape(content.getTxt()));
            if(content.getUser() != null)
                content.setUser(unEscapeUser(content.getUser()));
            if(content.getChannel() != null)
                content.setChannel(unEscapeChannel(content.getChannel()));
            return content;
        } else
            return null;
    }

    public static ContentPic unEscapeContentPic(ContentPic contentPic) {
        if(contentPic != null){
            if(contentPic.getTime() != null)
                contentPic.setTime(HtmlUtils.htmlUnescape(contentPic.getTime()));
            if(contentPic.getPlace() != null)
                contentPic.setPlace(HtmlUtils.htmlUnescape(contentPic.getPlace()));
            if(contentPic.getPublisher() != null)
                contentPic.setPublisher(HtmlUtils.htmlUnescape(contentPic.getPublisher()));
            if(contentPic.getPath() != null)
                contentPic.setPath(HtmlUtils.htmlUnescape(contentPic.getPath()));
            return contentPic;
        } else
            return null;
    }

    public static Channel unEscapeChannel(Channel channel) {
        if(channel != null){
            if(channel.getTitle() != null)
                channel.setTitle(HtmlUtils.htmlUnescape(channel.getTitle()));
            return channel;
        } else
            return null;
    }

    public static BackgroundUser unEscapeUser(BackgroundUser user) {
        if(user != null){
            if(user.getName() != null)
                user.setName(HtmlUtils.htmlUnescape(user.getName()));
            if(user.getRealName() != null)
                user.setRealName(HtmlUtils.htmlUnescape(user.getRealName()));
            if(user.getEmail() != null)
                user.setEmail(HtmlUtils.htmlUnescape(user.getEmail()));
            return user;
        } else
            return null;
    }
}
