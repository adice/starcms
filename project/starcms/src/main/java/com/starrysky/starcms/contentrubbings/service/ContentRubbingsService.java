package com.starrysky.starcms.contentrubbings.service;

import com.starrysky.starcms.contentrubbings.dao.ContentRubbingsDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentRubbings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName ContentRubbingsService
 * @Description
 * @Author adi
 * @Date 2022-08-15 11:05
 */
@Service
@Transactional(readOnly = true)
public class ContentRubbingsService {
    @Resource
    private ContentRubbingsDao contentRubbingsDao;

    public ContentRubbings getByContent(Content content) {
        return this.contentRubbingsDao.findByContent(content);
    }
}
