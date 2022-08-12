package com.starrysky.starcms.security;

import com.starrysky.starcms.backstageuser.dao.BackgroundUserDao;
import com.starrysky.starcms.entity.BackgroundRole;
import com.starrysky.starcms.entity.BackgroundUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName SecurityUserDetailService
 * @Description
 * @Author adi
 * @Date 2022-08-11 15:00
 */
@Service
@Transactional(readOnly = true)
public class SecurityUserDetailsService implements UserDetailsService {
    @Resource
    private BackgroundUserDao backgroundUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BackgroundUser backgroundUser = backgroundUserDao.findByName(username);
        if(backgroundUser != null) {
            SecurityUser securityUser = new SecurityUser();
            securityUser.setId(backgroundUser.getId());
            securityUser.setUsername(username);
            securityUser.setPassword(backgroundUser.getPassword());
            securityUser.setRealName(backgroundUser.getRealName());
            securityUser.setEnabled(backgroundUser.getState() == 1);
            securityUser.setAccountNonExpired(true);
            securityUser.setAccountNonLocked(true);
            securityUser.setCredentialsNonExpired(true);
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            for(BackgroundRole backgroundRole : backgroundUser.getRoles()){
                authorities.add(new SimpleGrantedAuthority(backgroundRole.getTitle()));
            }
            securityUser.setAuthorities(authorities);
            return securityUser;
        }else {
            throw new UsernameNotFoundException("用户名不存在");
        }
    }

    /**
     * 自动升级密码加密规则，再实现 UserDetailsPasswordService及其方法
     */
//    @Override
//    @Transactional()
//    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
//        BackgroundUser backgroundUser = this.backgroundUserDao.findByName(userDetails.getUsername());
//        backgroundUser.setPassword(newPassword);
//        this.backgroundUserDao.save(backgroundUser);
//        return userDetails;
//    }

}
