package com.starrysky.starcms.content.service;

import com.starrysky.starcms.content.dao.ContentDao;
import com.starrysky.starcms.content3d.dao.Content3DDao;
import com.starrysky.starcms.contentallscene.dao.ContentAllSceneDao;
import com.starrysky.starcms.contentaudio.dao.ContentAudioDao;
import com.starrysky.starcms.contentbook.dao.ContentBookDao;
import com.starrysky.starcms.contentnews.dao.ContentNewsDao;
import com.starrysky.starcms.contentpic.dao.ContentPicDao;
import com.starrysky.starcms.contentrubbings.dao.ContentRubbingsDao;
import com.starrysky.starcms.contentvideo.dao.ContentVideoDao;
import com.starrysky.starcms.entity.*;
import com.starrysky.starcms.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@Transactional
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
    @Resource
    private ContentVideoDao contentVideoDao;
    @Resource
    private Content3DDao content3DDao;
    @Resource
    private ContentAllSceneDao contentAllSceneDao;
    @Resource
    private ContentNewsDao contentNewsDao;


    @Transactional(readOnly = true)
    public Page<Content> list(String title, Boolean recommend, Integer status, Integer[] channelIds, Integer userId, String name, String realName, int pageNum, int pageSize) throws Exception {
        return this.contentDao.findDynamic(title, recommend, status, channelIds, userId, name, realName, pageNum, pageSize);
    }

    public void addBook(Content content, Integer channelId, BackgroundUser backgroundUser, String seriesName, String authorName, String cover, String attachments) throws Exception {
        addContent(content, channelId, backgroundUser);

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
        editContent(content, channelId);

        ContentBook contentBook = this.contentBookDao.findByContent(content);
        contentBook.setSeriesName(seriesName);
        contentBook.setAuthorName(authorName);
        contentBook.setCover(cover);
        contentBook.setAttachments(attachments);
        this.contentBookDao.save(contentBook);
    }

    public void addPic(Content content, Integer channelId, BackgroundUser backgroundUser, String time, String place, String publisher, String pic) throws Exception {
        addContent(content, channelId, backgroundUser);

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
        editContent(content, channelId);

        ContentPic contentPic = this.contentPicDao.findByContent(content);
        contentPic.setTime(time);
        contentPic.setPlace(place);
        contentPic.setPublisher(publisher);
        contentPic.setPath(pic);
        this.contentPicDao.save(contentPic);
    }

    public void addRubbings(Content content, Integer channelId, BackgroundUser backgroundUser, String time, String place, String publisher, String pic, String path) throws Exception {
        addContent(content, channelId, backgroundUser);

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
        editContent(content, channelId);

        ContentRubbings contentRubbings = this.contentRubbingsDao.findByContent(content);
        contentRubbings.setTime(time);
        contentRubbings.setPlace(place);
        contentRubbings.setPublisher(publisher);
        contentRubbings.setCover(cover);
        contentRubbings.setPath(path);
        this.contentRubbingsDao.save(contentRubbings);
    }

    public void addAudio(Content content, Integer channelId, BackgroundUser backgroundUser, String time, String place, String publisher, String pic, String path) throws Exception {
        addContent(content, channelId, backgroundUser);

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
        editContent(content, channelId);

        ContentAudio contentAudio = this.contentAudioDao.findByContent(content);
        contentAudio.setTime(time);
        contentAudio.setPlace(place);
        contentAudio.setPublisher(publisher);
        contentAudio.setCover(cover);
        contentAudio.setPath(path);
        this.contentAudioDao.save(contentAudio);
    }

    public void addVideo(Content content, Integer channelId, BackgroundUser backgroundUser, String time, String place, String publisher, String pic, String path) throws Exception {
        addContent(content, channelId, backgroundUser);

        ContentVideo contentVideo = new ContentVideo();
        contentVideo.setTime(time);
        contentVideo.setPlace(place);
        contentVideo.setPublisher(publisher);
        contentVideo.setCover(pic);
        contentVideo.setPath(path);
        contentVideo.setContent(content);

        this.contentDao.save(content);
        this.contentVideoDao.save(contentVideo);
    }

    public void editVideo(Content content, Integer channelId, String time, String place, String publisher, String cover, String path) throws Exception {
        editContent(content, channelId);

        ContentVideo contentVideo = this.contentVideoDao.findByContent(content);
        contentVideo.setTime(time);
        contentVideo.setPlace(place);
        contentVideo.setPublisher(publisher);
        contentVideo.setCover(cover);
        contentVideo.setPath(path);
        this.contentVideoDao.save(contentVideo);
    }

    public void add3D(Content content, Integer channelId, BackgroundUser backgroundUser, String publisher, String cover, String path) throws Exception {
        addContent(content, channelId, backgroundUser);

        Content3D content3D = new Content3D();
        content3D.setPublisher(publisher);
        content3D.setCover(cover);
        content3D.setPath(path);
        content3D.setContent(content);

        this.contentDao.save(content);
        this.content3DDao.save(content3D);

    }

    public void edit3D(Content content, Integer channelId, String publisher, String cover, String path) throws Exception {
        editContent(content, channelId);

        Content3D content3D = this.content3DDao.findByContent(content);
        content3D.setPublisher(publisher);
        content3D.setCover(cover);
        content3D.setPath(path);
        this.content3DDao.save(content3D);
    }

    public void addAllScene(Content content, Integer channelId, BackgroundUser backgroundUser, String publisher, String cover, String path) throws Exception {
        addContent(content, channelId, backgroundUser);

        ContentAllScene contentAllScene = new ContentAllScene();
        contentAllScene.setPublisher(publisher);
        contentAllScene.setCover(cover);
        contentAllScene.setPath(path);
        contentAllScene.setContent(content);

        this.contentDao.save(content);
        this.contentAllSceneDao.save(contentAllScene);

    }

    public void editAllScene(Content content, Integer channelId, String publisher, String cover, String path) throws Exception {
        editContent(content, channelId);

        ContentAllScene contentAllScene = this.contentAllSceneDao.findByContent(content);
        contentAllScene.setPublisher(publisher);
        contentAllScene.setCover(cover);
        contentAllScene.setPath(path);
        this.contentAllSceneDao.save(contentAllScene);
    }

    public void addNews(Content content, Integer channelId, BackgroundUser backgroundUser, Integer journalId, Date newsTime, Integer section, String position, String path, HttpServletRequest request) throws Exception {
        addContent(content, channelId, backgroundUser);

        ContentNews contentNews = new ContentNews();
        Journal journal = new Journal();
        journal.setId(journalId);
        contentNews.setJournal(journal);
        contentNews.setNewsTime(newsTime);
        contentNews.setSection(section);
        contentNews.setPosition(position);
        contentNews.setPath(path);
        contentNews.setContent(content);

        this.contentDao.save(content);
        this.contentNewsDao.save(contentNews);

    }

    public void editNews(Content content, Integer channelId, String publisher, String cover, String path) throws Exception {
        editContent(content, channelId);

        ContentAllScene contentAllScene = this.contentAllSceneDao.findByContent(content);
        contentAllScene.setPublisher(publisher);
        contentAllScene.setCover(cover);
        contentAllScene.setPath(path);
        this.contentAllSceneDao.save(contentAllScene);
    }

    public void delete(int id) {
        Content content = this.contentDao.getOne(id);
        switch(content.getChannel().getId()){
            case Constant.CHANNEL_BOOK:
                this.contentBookDao.deleteByContent(content);
                break;
            case Constant.CHANNEL_PIC:
            case Constant.CHANNEL_MURAL:
            case Constant.CHANNEL_PAINTING:
                this.contentPicDao.deleteByContent(content);
                break;
            case Constant.CHANNEL_RUBBINGS:
                this.contentRubbingsDao.deleteByContent(content);
                break;
            case Constant.CHANNEL_AUDIO:
                this.contentAudioDao.deleteByContent(content);
                break;
            case Constant.CHANNEL_VIDEO:
                this.contentVideoDao.deleteByContent(content);
                break;
            case Constant.CHANNEL_3D:
                this.content3DDao.deleteByContent(content);
                break;
            case Constant.CHANNEL_ALLSCENE:
                this.contentAllSceneDao.deleteByContent(content);
                break;
        }
        this.contentDao.deleteById(id);
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

    private void addContent(Content content, Integer channelId, BackgroundUser backgroundUser) {
        Channel channel = new Channel();
        channel.setId(channelId);
        content.setChannel(channel);
        content.setUser(backgroundUser);
        content.setAddTime(new Date());
        content.setViewCount(0);
        if (content.getStatus() == 0) {
            content.setStatus(Constant.CONTENT_STATUS_AUDITING);
        }
    }

    private void editContent(Content content, Integer channelId) {
        Content contentDb = this.contentDao.getOne(content.getId());
        Channel channel = new Channel();
        channel.setId(channelId);
        contentDb.setChannel(channel);
        contentDb.setTitle(content.getTitle());
        contentDb.setShortTitle(content.getShortTitle());
        contentDb.setLastEditTime(new Date());
        contentDb.setRecommend(content.isRecommend());
        // 原为草稿状态，修改为非草稿状态则进入待审核状态，其它情况不变，否则改为草稿状态
        if (contentDb.getStatus() == 1 && content.getStatus() == 0) {
            contentDb.setStatus(Constant.CONTENT_STATUS_AUDITING);
        } else {
            contentDb.setStatus(Constant.CONTENT_STATUS_DRAFT);
        }
        contentDb.setTags(content.getTags());
        contentDb.setTxt(content.getTxt());
        this.contentDao.save(contentDb);
    }
}
