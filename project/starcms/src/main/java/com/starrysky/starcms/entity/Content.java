package com.starrysky.starcms.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName Content
 * @Description
 * @Author adi
 * @Date 2022-07-26 11:14
 */
@Entity
@Table(name = "ct_content")
public class Content {
    private int id;
    private String title;
    private String shortTitle;
    private Date addTime;
    private Date lastEditTime;
    private Date lastPublishTime;
    private int viewCount;
    private boolean recommend;  //是否推荐
    private int status;     // 1-草稿，2-审核中，3-审核通过，4-审核退回，5-回收站
    private String tags;
    private String txt;

    private Channel channel;
    private BackgroundUser user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Date getLastPublishTime() {
        return lastPublishTime;
    }

    public void setLastPublishTime(Date lastPublishTime) {
        this.lastPublishTime = lastPublishTime;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Column(columnDefinition = "text")
    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    @ManyToOne()
    @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @ManyToOne()
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public BackgroundUser getUser() {
        return user;
    }

    public void setUser(BackgroundUser user) {
        this.user = user;
    }
}
