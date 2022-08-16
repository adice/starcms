package com.starrysky.starcms.contentaudio.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.ContentAudio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ContentAudioDao
 * @Description
 * @Author adi
 * @Date 2022-08-15 20:55
 */
public interface ContentAudioDao extends JpaRepository<ContentAudio, Integer> {
    public ContentAudio findByContent(Content content);

    public void deleteByContent(Content content);
}
