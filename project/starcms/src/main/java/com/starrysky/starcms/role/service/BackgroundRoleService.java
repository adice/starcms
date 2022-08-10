package com.starrysky.starcms.role.service;

import com.starrysky.starcms.entity.BackgroundRole;
import com.starrysky.starcms.role.dao.BackgroundRoleDao;
import com.starrysky.starcms.util.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName BackgroundRoleService
 * @Description
 * @Author adi
 * @Date 2022-07-28 14:11
 */
@Service
@Transactional(readOnly = true)
public class BackgroundRoleService {
    @Resource
    private BackgroundRoleDao backgroundRoleDao;

    public List<BackgroundRole> listNormal(){
        return this.backgroundRoleDao.findByState(Constant.STATE_NORMAL);
    }
}
