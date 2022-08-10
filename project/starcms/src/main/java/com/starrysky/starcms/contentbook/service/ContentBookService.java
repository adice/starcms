package com.starrysky.starcms.contentbook.service;

import com.starrysky.starcms.contentbook.dao.ContentBookDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentBook;
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
public class ContentBookService {

    @Resource
    private ContentBookDao contentBookDao;

    public ContentBook getByContent(Content content) {
        return this.contentBookDao.findByContent(content);
    }
}
