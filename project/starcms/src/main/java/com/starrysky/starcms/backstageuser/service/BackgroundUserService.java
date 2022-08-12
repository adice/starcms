package com.starrysky.starcms.backstageuser.service;

import com.starrysky.starcms.backstageuser.dao.BackgroundUserDao;
import com.starrysky.starcms.entity.BackgroundUser;
import com.starrysky.starcms.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BackgroundUserService
 * @Description
 * @Author adi
 * @Date 2022-07-26 17:27
 */
@Service
@Transactional(readOnly = true)
public class BackgroundUserService {
    @Resource
    private BackgroundUserDao backgroundUserDao;

//    /**
//     * 后台用户登录
//     *
//     * @param name
//     * @param password
//     * @return
//     */
//    @Transactional()
//    public BackgroundUser login(String name, String password) throws Exception {
//        BackgroundUser backgroundUser = this.backgroundUserDao.findByNameAndState(name, Constant.STATE_NORMAL);
//        if (backgroundUser == null) {
//            return null;
//        } else {
//            try {
////            if(backgroundUser.getPassword().equals(password)){
//                if (BCrypt.checkpw(password, backgroundUser.getPassword())) {
//                    // 修改最后一次登录时间及次数
//                    backgroundUser.setLastLoginTime(new Date());
//                    backgroundUser.setLoginCount(backgroundUser.getLoginCount() + 1);
//                    this.backgroundUserDao.save(backgroundUser);
//                    return backgroundUser;
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                return null;
//            }
//        }
//    }

    public boolean getByName(String name){
        return this.backgroundUserDao.findByName(name) != null;
    }

    /**
     * 检索后台用户
     *
     * @param name
     * @param realName
     * @param state
     * @return
     */
    public Page<BackgroundUser> list(String name, String realName, Integer state, int pageNum, int pageSize) throws Exception {
        return this.backgroundUserDao.findDynamic(name, realName, state, pageNum, pageSize);
    }

    @Transactional()
    public BackgroundUser add(BackgroundUser user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword("{bcrypt}" + bCryptPasswordEncoder.encode(user.getPassword()));
        return this.backgroundUserDao.save(user);
    }

    public BackgroundUser getById(int id) throws Exception {
        return this.backgroundUserDao.getOne(id);
    }

    @Transactional()
    public BackgroundUser edit(BackgroundUser user) throws Exception {
        BackgroundUser dbUser = this.backgroundUserDao.getOne(user.getId());
        dbUser.setRealName(user.getRealName());
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            dbUser.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        dbUser.setEmail(user.getEmail());
        dbUser.setRoles(user.getRoles());
        dbUser.setDataRange(user.getDataRange());
        dbUser.setState(user.getState());
        return this.backgroundUserDao.save(dbUser);
    }

    @Transactional()
    public void editPwd(int id, String password) throws Exception {
        BackgroundUser dbUser = this.backgroundUserDao.getOne(id);
        dbUser.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(password));
        this.backgroundUserDao.save(dbUser);
    }

    @Transactional()
    public void delete(int id) {
        this.backgroundUserDao.deleteById(id);
    }

    @Transactional()
    public void deletes(String ids){
        String[] allId = ids.split(",");
        List<BackgroundUser> list = new ArrayList<>();
        for(String id : allId){
            if(id!=null && !id.equals("")) {
                BackgroundUser backgroundUser = new BackgroundUser();
                backgroundUser.setId(Integer.parseInt(id));
                list.add(backgroundUser);
            }
        }
        this.backgroundUserDao.deleteInBatch(list);
    }
}
