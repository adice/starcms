package com.starrysky.starcms.contentallscene.service;

import com.starrysky.starcms.contentallscene.dao.ContentAllSceneDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentAllScene;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName ContentAllSceneService
 * @Description
 * @Author adi
 * @Date 2022-08-16 13:31
 */
@Service
@Transactional(readOnly = true)
public class ContentAllSceneService {
    @Resource
    private ContentAllSceneDao contentAllSceneDao;

    public ContentAllScene getByContent(Content content) {
        return this.contentAllSceneDao.findByContent(content);
    }
}
