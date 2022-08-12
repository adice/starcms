package com.starrysky.starcms.entity;

import javax.persistence.*;

/**
 * @ClassName BackgroundRole
 * @Description
 * @Author adi
 * @Date 2022-07-26 08:27
 */
@Entity
@Table(name = "bg_role")
public class BackgroundRole {
    private int id;
    private String title;
    private String name;
    private int priority;
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

    @Column(length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
