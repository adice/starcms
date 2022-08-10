package com.starrysky.starcms.entity;

import javax.persistence.*;

/**
 * @ClassName ContentBook
 * @Description
 * @Author adi
 * @Date 2022-07-26 14:02
 */
@Entity
@Table(name = "ct_book")
public class ContentBook {
    private int id;
    private String seriesName;  //丛书名
    private String authorName;  //作者
    private String cover;
    private String attachments;

    private Content content;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(length = 100)
    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    @Column(length = 100)
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    @ManyToOne()
    @JoinColumn(name = "content_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
