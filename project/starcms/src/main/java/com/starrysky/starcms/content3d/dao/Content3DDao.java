package com.starrysky.starcms.content3d.dao;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.entity.Content3D;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName Content3DDao
 * @Description
 * @Author adi
 * @Date 2022-08-16 11:28
 */
public interface Content3DDao extends JpaRepository<Content3D, Integer> {
    public Content3D findByContent(Content content);

    public void deleteByContent(Content content);
}
