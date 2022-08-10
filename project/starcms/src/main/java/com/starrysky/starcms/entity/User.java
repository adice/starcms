package com.starrysky.starcms.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName BackgroundUser
 * @Description
 * @Author adi
 * @Date 2022-07-26 08:12
 */
@Entity
@Table(name = "front_user")
public class User {
    private int id;
    private String name;
    private String realName;
    private String email;
    private Date registTime;
    private String registIp;
    private Date lastLoginTime;
    private String lastLoginIp;
    private int loginCount;
    private int dataRange;  // 1-所有数据，2-自己的数据
    private int state;      // 1-正常，2-禁用

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public String getRegistIp() {
        return registIp;
    }

    public void setRegistIp(String registIp) {
        this.registIp = registIp;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public int getDataRange() {
        return dataRange;
    }

    public void setDataRange(int dataRange) {
        this.dataRange = dataRange;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
