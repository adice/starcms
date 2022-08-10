package com.starrysky.starcms.role.dao;

import com.starrysky.starcms.entity.BackgroundRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName BackgroundRoleDao
 * @Description
 * @Author adi
 * @Date 2022-07-28 14:10
 */
public interface BackgroundRoleDao extends JpaRepository<BackgroundRole, Integer> {
    public List<BackgroundRole> findByState(int state);
}
