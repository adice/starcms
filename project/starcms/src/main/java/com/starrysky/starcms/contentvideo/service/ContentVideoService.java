package com.starrysky.starcms.contentvideo.service;

import com.starrysky.starcms.contentvideo.dao.ContentVideoDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentVideo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName ContentVideoService
 * @Description
 * @Author adi
 * @Date 2022-08-16 09:06
 */
@Service
@Transactional(readOnly = true)
public class ContentVideoService {
    @Resource
    private ContentVideoDao contentVideoDao;

    public ContentVideo getByContent(Content content) {
        return this.contentVideoDao.findByContent(content);
    }
}
