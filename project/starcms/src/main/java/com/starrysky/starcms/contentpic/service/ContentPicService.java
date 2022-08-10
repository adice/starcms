package com.starrysky.starcms.contentpic.service;

import com.starrysky.starcms.contentpic.dao.ContentPicDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentPic;
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
public class ContentPicService {

    @Resource
    private ContentPicDao contentPicDao;

    public ContentPic getByContent(Content content) {
        return this.contentPicDao.findByContent(content);
    }
}
