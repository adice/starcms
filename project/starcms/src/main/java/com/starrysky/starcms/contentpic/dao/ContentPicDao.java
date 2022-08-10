package com.starrysky.starcms.contentpic.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentPic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ContentBookDao
 * @Description
 * @Author adi
 * @Date 2022-08-05 08:18
 */
public interface ContentPicDao extends JpaRepository<ContentPic, Integer> {
    public ContentPic findByContent(Content content);

    public void deleteByContent(Content content);
}
