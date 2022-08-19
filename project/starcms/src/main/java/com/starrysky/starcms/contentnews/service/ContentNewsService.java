package com.starrysky.starcms.contentnews.service;

import com.starrysky.starcms.contentnews.dao.ContentNewsDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentNews;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName ContentBookService
 * @Description
 * @Author adi
 * @Date 2022-08-08 10:48
 */
@Service
@Transactional(readOnly = true)
public class ContentNewsService {

    @Resource
    private ContentNewsDao contentNewsDao;

    public ContentNews getByContent(Content content) {
        return this.contentNewsDao.findByContent(content);
    }
}
