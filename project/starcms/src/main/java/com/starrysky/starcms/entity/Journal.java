package com.starrysky.starcms.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName ContentJounal
 * @Description
 * @Author adi
 * @Date 2022-08-18 12:13
 */
@Entity
@Table(name = "ct_journal")
public class Journal {
    private int id;
    private String title;
    private String cover;
    private Date beginTime;
    private Date endTime;
    private String publisher;
    private String address;
    private String phone;
    private int state;      // 1-正常，2-禁用

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Temporal(TemporalType.DATE)
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Temporal(TemporalType.DATE)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
