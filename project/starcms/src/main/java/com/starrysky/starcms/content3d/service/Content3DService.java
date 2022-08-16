package com.starrysky.starcms.content3d.service;

import com.starrysky.starcms.content3d.dao.Content3DDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.Content3D;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName Content3DService
 * @Description
 * @Author adi
 * @Date 2022-08-16 11:29
 */
@Service
@Transactional(readOnly = true)
public class Content3DService {
    @Resource
    private Content3DDao content3DDao;

    public Content3D getByContent(Content content) {
        return this.content3DDao.findByContent(content);
    }
}
