package com.starrysky.starcms.security;

import com.starrysky.starcms.entity.*;
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

    public static ContentBook unEscapeContentBook(ContentBook contentBook) {
        if(contentBook != null){
            if(contentBook.getAuthorName() != null)
                contentBook.setAuthorName(HtmlUtils.htmlUnescape(contentBook.getAuthorName()));
            if(contentBook.getSeriesName() != null)
                contentBook.setSeriesName(HtmlUtils.htmlUnescape(contentBook.getSeriesName()));
            return contentBook;
        } else
            return null;
    }

    public static ContentRubbings unEscapeContentRubbings(ContentRubbings contentRubbings) {
        if(contentRubbings != null){
            if(contentRubbings.getTime() != null)
                contentRubbings.setTime(HtmlUtils.htmlUnescape(contentRubbings.getTime()));
            if(contentRubbings.getPlace() != null)
                contentRubbings.setPlace(HtmlUtils.htmlUnescape(contentRubbings.getPlace()));
            if(contentRubbings.getPublisher() != null)
                contentRubbings.setPublisher(HtmlUtils.htmlUnescape(contentRubbings.getPublisher()));
            if(contentRubbings.getPath() != null)
                contentRubbings.setPath(HtmlUtils.htmlUnescape(contentRubbings.getPath()));
            return contentRubbings;
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

    public static ContentAudio unEscapeContentAudio(ContentAudio contentAudio) {
        if(contentAudio != null){
            if(contentAudio.getTime() != null)
                contentAudio.setTime(HtmlUtils.htmlUnescape(contentAudio.getTime()));
            if(contentAudio.getPlace() != null)
                contentAudio.setPlace(HtmlUtils.htmlUnescape(contentAudio.getPlace()));
            if(contentAudio.getPublisher() != null)
                contentAudio.setPublisher(HtmlUtils.htmlUnescape(contentAudio.getPublisher()));
            if(contentAudio.getPath() != null)
                contentAudio.setPath(HtmlUtils.htmlUnescape(contentAudio.getPath()));
            return contentAudio;
        } else
            return null;
    }

    public static ContentVideo unEscapeContentVideo(ContentVideo contentVideo) {
        if(contentVideo != null){
            if(contentVideo.getTime() != null)
                contentVideo.setTime(HtmlUtils.htmlUnescape(contentVideo.getTime()));
            if(contentVideo.getPlace() != null)
                contentVideo.setPlace(HtmlUtils.htmlUnescape(contentVideo.getPlace()));
            if(contentVideo.getPublisher() != null)
                contentVideo.setPublisher(HtmlUtils.htmlUnescape(contentVideo.getPublisher()));
            if(contentVideo.getPath() != null)
                contentVideo.setPath(HtmlUtils.htmlUnescape(contentVideo.getPath()));
            return contentVideo;
        } else
            return null;
    }

    public static Content3D unEscapeContent3D(Content3D content3D) {
        if(content3D != null){
            if(content3D.getPublisher() != null)
                content3D.setPublisher(HtmlUtils.htmlUnescape(content3D.getPublisher()));
            if(content3D.getPath() != null)
                content3D.setPath(HtmlUtils.htmlUnescape(content3D.getPath()));
            return content3D;
        } else
            return null;
    }

    public static ContentAllScene unEscapeContentAllScene(ContentAllScene contentAllScene) {
        if(contentAllScene != null){
            if(contentAllScene.getPublisher() != null)
                contentAllScene.setPublisher(HtmlUtils.htmlUnescape(contentAllScene.getPublisher()));
            if(contentAllScene.getPath() != null)
                contentAllScene.setPath(HtmlUtils.htmlUnescape(contentAllScene.getPath()));
            return contentAllScene;
        } else
            return null;
    }

    public static ContentNews unEscapeContentNews(ContentNews contentNews) {
        if(contentNews != null){
            if(contentNews.getPath() != null)
                contentNews.setPath(HtmlUtils.htmlUnescape(contentNews.getPath()));
            if(contentNews.getJournal() != null)
                contentNews.setJournal(unEscapeJournal(contentNews.getJournal()));
            return contentNews;
        } else
            return null;
    }

    public static Journal unEscapeJournal(Journal journal) {
        if(journal != null){
            if(journal.getTitle() != null)
                journal.setTitle(HtmlUtils.htmlUnescape(journal.getTitle()));
            if(journal.getAddress() != null)
                journal.setAddress(HtmlUtils.htmlUnescape(journal.getAddress()));
            if(journal.getCover() != null)
                journal.setCover(HtmlUtils.htmlUnescape(journal.getCover()));
            if(journal.getPhone() != null)
                journal.setPhone(HtmlUtils.htmlUnescape(journal.getPhone()));
            if(journal.getPublisher() != null)
                journal.setPublisher(HtmlUtils.htmlUnescape(journal.getPublisher()));
            return journal;
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
