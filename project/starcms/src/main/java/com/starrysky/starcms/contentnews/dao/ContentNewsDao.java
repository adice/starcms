package com.starrysky.starcms.contentnews.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentNews;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ContentBookDao
 * @Description
 * @Author adi
 * @Date 2022-08-05 08:18
 */
public interface ContentNewsDao extends JpaRepository<ContentNews, Integer> {
    public ContentNews findByContent(Content content);

    public void deleteByContent(Content content);
}
