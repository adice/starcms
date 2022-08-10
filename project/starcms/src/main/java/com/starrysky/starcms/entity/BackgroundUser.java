package com.starrysky.starcms.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName BackgroundUser
 * @Description
 * @Author adi
 * @Date 2022-07-26 08:12
 */
@Entity
@Table(name = "bg_user")
public class BackgroundUser {
    private int id;
    private String name;
    private String password;
    private String realName;
    private String email;
    private Date registTime;
    private String registIp;
    private Date lastLoginTime;
    private String lastLoginIp;
    private int loginCount;
    private int dataRange;  // 1-所有数据，2-自己的数据
    private int state;      // 1-正常，2-禁用

    private Set<BackgroundRole> roles = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Column(unique = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(length = 50)
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Column(length = 100)
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

    @Column(length = 50)
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

    @Column(length = 50)
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
    @ManyToMany()
    @JoinTable(name = "rel_role_backgrounduser",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))})
    public Set<BackgroundRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<BackgroundRole> roles) {
        this.roles = roles;
    }
}
