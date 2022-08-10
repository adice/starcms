package com.starrysky.starcms.contentbook.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentBook;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ContentBookDao
 * @Description
 * @Author adi
 * @Date 2022-08-05 08:18
 */
public interface ContentBookDao extends JpaRepository<ContentBook, Integer> {
    public ContentBook findByContent(Content content);

    public void deleteByContent(Content content);
}
