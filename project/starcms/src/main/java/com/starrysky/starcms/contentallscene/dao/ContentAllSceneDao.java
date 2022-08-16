package com.starrysky.starcms.contentallscene.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentAllScene;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ContentAllSceneDao
 * @Description
 * @Author adi
 * @Date 2022-08-16 13:30
 */
public interface ContentAllSceneDao extends JpaRepository<ContentAllScene, Integer> {
    public ContentAllScene findByContent(Content content);

    public void deleteByContent(Content content);
}
