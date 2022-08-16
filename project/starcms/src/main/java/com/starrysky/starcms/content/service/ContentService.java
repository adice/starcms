package com.starrysky.starcms.content.service;

import com.starrysky.starcms.content.dao.ContentDao;
import com.starrysky.starcms.contentaudio.dao.ContentAudioDao;
import com.starrysky.starcms.contentbook.dao.ContentBookDao;
import com.starrysky.starcms.contentpic.dao.ContentPicDao;
import com.starrysky.starcms.contentrubbings.dao.ContentRubbingsDao;
import com.starrysky.starcms.entity.*;
import com.starrysky.starcms.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ContentService
 * @Description
 * @Author adi
 * @Date 2022-08-02 08:58
 */
@Service
@Transactional()
public class ContentService {
    @Resource
    private ContentDao contentDao;
    @Resource
    private ContentBookDao contentBookDao;
    @Resource
    private ContentPicDao contentPicDao;
    @Resource
    private ContentRubbingsDao contentRubbingsDao;
    @Resource
    private ContentAudioDao contentAudioDao;

    @Transactional(readOnly = true)
    public Page<Content> list(String title, Boolean recommend, Integer status, Integer[] channelIds, Integer userId, String name, String realName, int pageNum, int pageSize) throws Exception {
        return this.contentDao.findDynamic(title, recommend, status, channelIds, userId, name, realName, pageNum, pageSize);
    }

    public void addBook(Content content, Integer channelId, BackgroundUser backgroundUser, String seriesName, String authorName, String cover, String attachments) throws Exception {
        Channel channel = new Channel();
        channel.setId(channelId);
        content.setChannel(channel);
        content.setUser(backgroundUser);
        content.setAddTime(new Date());
        content.setViewCount(0);
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }

        ContentBook contentBook = new ContentBook();
        contentBook.setSeriesName(seriesName);
        contentBook.setAuthorName(authorName);
        contentBook.setCover(cover);
        contentBook.setAttachments(attachments);
        contentBook.setContent(content);

        this.contentDao.save(content);
        this.contentBookDao.save(contentBook);

    }

    public void editBook(Content content, Integer channelId, String seriesName, String authorName, String cover, String attachments) throws Exception {
        Content contentDb = this.contentDao.getOne(content.getId());
        Channel channel = new Channel();
        channel.setId(channelId);
        contentDb.setChannel(channel);
        contentDb.setTitle(content.getTitle());
        contentDb.setShortTitle(content.getShortTitle());
        contentDb.setLastEditTime(new Date());
        contentDb.setRecommend(content.isRecommend());
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }
        contentDb.setTags(content.getTags());
        contentDb.setTxt(content.getTxt());
        this.contentDao.save(contentDb);

        ContentBook contentBook = this.contentBookDao.findByContent(content);
        contentBook.setSeriesName(seriesName);
        contentBook.setAuthorName(authorName);
        contentBook.setCover(cover);
        contentBook.setAttachments(attachments);
        this.contentBookDao.save(contentBook);
    }

    public void addPic(Content content, Integer channelId, BackgroundUser backgroundUser, String time, String place, String publisher, String pic) throws Exception {
        Channel channel = new Channel();
        channel.setId(channelId);
        content.setChannel(channel);
        content.setUser(backgroundUser);
        content.setAddTime(new Date());
        content.setViewCount(0);
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }

        ContentPic contentPic = new ContentPic();
        contentPic.setTime(time);
        contentPic.setPlace(place);
        contentPic.setPublisher(publisher);
        contentPic.setPath(pic);
        contentPic.setContent(content);

        this.contentDao.save(content);
        this.contentPicDao.save(contentPic);
    }

    public void editPic(Content content, Integer channelId, String time, String place, String publisher, String pic) throws Exception {
        Content contentDb = this.contentDao.getOne(content.getId());
        Channel channel = new Channel();
        channel.setId(channelId);
        contentDb.setChannel(channel);
        contentDb.setTitle(content.getTitle());
        contentDb.setShortTitle(content.getShortTitle());
        contentDb.setLastEditTime(new Date());
        contentDb.setRecommend(content.isRecommend());
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }
        contentDb.setTags(content.getTags());
        contentDb.setTxt(content.getTxt());
        this.contentDao.save(contentDb);

        ContentPic contentPic = this.contentPicDao.findByContent(content);
        contentPic.setTime(time);
        contentPic.setPlace(place);
        contentPic.setPublisher(publisher);
        contentPic.setPath(pic);
        this.contentPicDao.save(contentPic);
    }

    public void addRubbings(Content content, Integer channelId, BackgroundUser backgroundUser, String time, String place, String publisher, String pic, String path) throws Exception {
        Channel channel = new Channel();
        channel.setId(channelId);
        content.setChannel(channel);
        content.setUser(backgroundUser);
        content.setAddTime(new Date());
        content.setViewCount(0);
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }

        ContentRubbings contentRubbings = new ContentRubbings();
        contentRubbings.setTime(time);
        contentRubbings.setPlace(place);
        contentRubbings.setPublisher(publisher);
        contentRubbings.setCover(pic);
        contentRubbings.setPath(path);
        contentRubbings.setContent(content);

        this.contentDao.save(content);
        this.contentRubbingsDao.save(contentRubbings);
    }

    public void editRubbings(Content content, Integer channelId, String time, String place, String publisher, String cover, String path) throws Exception {
        Content contentDb = this.contentDao.getOne(content.getId());
        Channel channel = new Channel();
        channel.setId(channelId);
        contentDb.setChannel(channel);
        contentDb.setTitle(content.getTitle());
        contentDb.setShortTitle(content.getShortTitle());
        contentDb.setLastEditTime(new Date());
        contentDb.setRecommend(content.isRecommend());
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }
        contentDb.setTags(content.getTags());
        contentDb.setTxt(content.getTxt());
        this.contentDao.save(contentDb);

        ContentRubbings contentRubbings = this.contentRubbingsDao.findByContent(content);
        contentRubbings.setTime(time);
        contentRubbings.setPlace(place);
        contentRubbings.setPublisher(publisher);
        contentRubbings.setCover(cover);
        contentRubbings.setPath(path);
        this.contentRubbingsDao.save(contentRubbings);
    }

    public void addAudio(Content content, Integer channelId, BackgroundUser backgroundUser, String time, String place, String publisher, String pic, String path) throws Exception {
        Channel channel = new Channel();
        channel.setId(channelId);
        content.setChannel(channel);
        content.setUser(backgroundUser);
        content.setAddTime(new Date());
        content.setViewCount(0);
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }

        ContentAudio contentAudio = new ContentAudio();
        contentAudio.setTime(time);
        contentAudio.setPlace(place);
        contentAudio.setPublisher(publisher);
        contentAudio.setCover(pic);
        contentAudio.setPath(path);
        contentAudio.setContent(content);

        this.contentDao.save(content);
        this.contentAudioDao.save(contentAudio);
    }

    public void editAudio(Content content, Integer channelId, String time, String place, String publisher, String cover, String path) throws Exception {
        Content contentDb = this.contentDao.getOne(content.getId());
        Channel channel = new Channel();
        channel.setId(channelId);
        contentDb.setChannel(channel);
        contentDb.setTitle(content.getTitle());
        contentDb.setShortTitle(content.getShortTitle());
        contentDb.setLastEditTime(new Date());
        contentDb.setRecommend(content.isRecommend());
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }
        contentDb.setTags(content.getTags());
        contentDb.setTxt(content.getTxt());
        this.contentDao.save(contentDb);

        ContentAudio contentAudio = this.contentAudioDao.findByContent(content);
        contentAudio.setTime(time);
        contentAudio.setPlace(place);
        contentAudio.setPublisher(publisher);
        contentAudio.setCover(cover);
        contentAudio.setPath(path);
        this.contentAudioDao.save(contentAudio);
    }

    public void delete(int id) {
        Content content = this.contentDao.getOne(id);
        switch(content.getChannel().getId()){
            case 1:
                this.contentBookDao.deleteByContent(content);
                this.contentDao.deleteById(id);
                break;
            case 2:
            case 8:
            case 9:
                this.contentPicDao.deleteByContent(content);
                this.contentDao.deleteById(id);
                break;
            case 3:
                this.contentRubbingsDao.deleteByContent(content);
                this.contentDao.deleteById(id);
                break;
            case 4:
                this.contentAudioDao.deleteByContent(content);
                this.contentDao.deleteById(id);
                break;
                // TODO 其它栏目的删除

        }
    }

    public void deletes(String ids){
        String[] allId = ids.split(",");
        List<BackgroundUser> list = new ArrayList<>();
        for(String id : allId){
            if(id!=null && !id.equals("")) {
                this.delete(Integer.parseInt(id));
            }
        }
    }

    public void check(int id) {
        Content content = this.contentDao.getOne(id);
        content.setStatus(Constant.CONTENT_STATUS_AUDITSUCCESS);
        this.contentDao.save(content);
    }

    public void checks(String ids) {
        String[] allId = ids.split(",");
        for(String id : allId){
            Content content = this.contentDao.getOne(Integer.parseInt(id));
            content.setStatus(Constant.CONTENT_STATUS_AUDITSUCCESS);
            this.contentDao.save(content);
        }
    }

    public void deny(int id) {
        Content content = this.contentDao.getOne(id);
        content.setStatus(Constant.CONTENT_STATUS_AUDITFAILURE);
        this.contentDao.save(content);
    }

    public void denys(String ids) {
        String[] allId = ids.split(",");
        for(String id : allId){
            Content content = this.contentDao.getOne(Integer.parseInt(id));
            content.setStatus(Constant.CONTENT_STATUS_AUDITFAILURE);
            this.contentDao.save(content);
        }
    }

    public Content getById(int id) throws Exception {
        return this.contentDao.getOne(id);
    }
}
