package com.starrysky.starcms.security;

import com.starrysky.starcms.backstageuser.dao.BackgroundUserDao;
import com.starrysky.starcms.entity.BackgroundUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @ClassName LoginSuccessHandler
 * @Description 登录成功后的后续处理，此处记录了最后一次登录时间和登录次数加1
 * @Author adi
 * @Date 2022-08-12 10:37
 */
@Service
@Transactional
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Resource
    private BackgroundUserDao backgroundUserDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 更新登录时间和次数
        BackgroundUser backgroundUser = this.backgroundUserDao.findByName(authentication.getName());
        backgroundUser.setLastLoginTime(new Date());
        backgroundUser.setLoginCount(backgroundUser.getLoginCount() + 1);
        backgroundUserDao.save(backgroundUser);

        super.setDefaultTargetUrl("/backstage/index");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
