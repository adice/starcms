package com.starrysky.starcms.contentaudio.service;

import com.starrysky.starcms.contentaudio.dao.ContentAudioDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentAudio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName ContentAudioService
 * @Description
 * @Author adi
 * @Date 2022-08-15 20:57
 */
@Service
@Transactional(readOnly = true)
public class ContentAudioService {
    @Resource
    private ContentAudioDao contentAudioDao;

    public ContentAudio getByContent(Content content) {
        return this.contentAudioDao.findByContent(content);
    }
}
