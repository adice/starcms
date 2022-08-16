package com.starrysky.starcms.contentvideo.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentVideo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ContentVideoDao
 * @Description
 * @Author adi
 * @Date 2022-08-16 09:05
 */
public interface ContentVideoDao extends JpaRepository<ContentVideo, Integer> {
    public ContentVideo findByContent(Content content);

    public void deleteByContent(Content content);
}
