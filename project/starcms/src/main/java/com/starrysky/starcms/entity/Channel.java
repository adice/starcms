package com.starrysky.starcms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Channel
 * @Description
 * @Author adi
 * @Date 2022-07-26 08:42
 */
@Entity
@Table(name = "ct_channel")
public class Channel {
    private int id;
    private String title;
    private String path;
    private int priority;
    private int state;  // 1-正常，2-禁用

    private Channel parentChannel;
    private Set<Channel> childChannels = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(length = 50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 50)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    @ManyToOne()
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Channel getParentChannel() {
        return parentChannel;
    }

    public void setParentChannel(Channel parentChannel) {
        this.parentChannel = parentChannel;
    }

    @OneToMany(mappedBy = "parentChannel", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @org.hibernate.annotations.ForeignKey(name = "none")
    @OrderBy("priority asc")
    @JsonIgnore
    public Set<Channel> getChildChannels() {
        return childChannels;
    }

    public void setChildChannels(Set<Channel> childChannels) {
        this.childChannels = childChannels;
    }
}
