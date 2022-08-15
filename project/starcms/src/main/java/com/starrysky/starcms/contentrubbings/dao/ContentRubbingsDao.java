package com.starrysky.starcms.contentrubbings.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentRubbings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ContentRubbingsDao
 * @Description
 * @Author adi
 * @Date 2022-08-15 11:07
 */
public interface ContentRubbingsDao extends JpaRepository<ContentRubbings, Integer> {
    public ContentRubbings findByContent(Content content);

    public void deleteByContent(Content content);
}
