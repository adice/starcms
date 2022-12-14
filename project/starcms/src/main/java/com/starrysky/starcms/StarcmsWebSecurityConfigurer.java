package com.starrysky.starcms;

import com.starrysky.starcms.security.LoginSuccessHandler;
import com.starrysky.starcms.security.SecurityUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import javax.annotation.Resource;

/**
 * @ClassName StarcmsWebSecurityConfigurer
 * @Description SpringSecurity配置类
 * @Author adi
 * @Date 2022-08-10 10:11
 */
@Configuration
public class StarcmsWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Resource
    private SecurityUserDetailsService securityUserDetailsService;
    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(securityUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 授权
                .mvcMatchers("/backstage/user/**", "/backstage/channel/**", "/backstage/lucene/**").hasRole("admin")
                .mvcMatchers("/backstage/content/**").hasAnyRole("admin", "entry", "audit")
                .mvcMatchers("/backstage/journal/**").hasAnyRole("admin", "entry")
                // 认证
                .antMatchers("/backstage/loginpage", "/backstage/unauthen", "/backstage/createvcode").permitAll()
                .antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/jquery-ui/**", "/fuelux/**", "/sweetalert/**").permitAll()
                .anyRequest().authenticated()
                // 登录
                .and()
                .formLogin()
                .loginPage("/backstage/loginpage")
                .loginProcessingUrl("/backstage/login")
                .usernameParameter("name")
                .passwordParameter("password")
                .defaultSuccessUrl("/backstage/index", true)
                .successHandler(loginSuccessHandler)
                .failureUrl("/backstage/loginpage")
                // 注销
                .and()
                .logout()
                .logoutUrl("/backstage/logout")
                .logoutSuccessUrl("/backstage/loginpage")
                .invalidateHttpSession(true);
//        http.rememberMe()   // 免密登录,以持久化的方式记录cookie信息
//                .tokenRepository(persistentTokenRepository())
//                .tokenValiditySeconds(7 * 24 * 60 * 60)
        // csrf 会话管理
        http.csrf().disable()   // 启用csrf，表单用th:action，则自动添加一个隐藏域
                // 限制1个账号只能登录1次
                .sessionManagement()
                .maximumSessions(1) // 需要重写user的hashcode和equals方法
                .expiredUrl("/backstage/user/loginpage")
//                .maxSessionsPreventsLogin(true) // 禁止后登录用户登录
                ;
        // 防止XSS
//        http.headers()
//                .xssProtection()
//                .and()
//                .contentSecurityPolicy("script-src 'self' 'sha256-+Hgiz/ECd4pXVDDa2NGHdKRPHC5B/yWLVVbppzTJTcg='");
        // 认证失败跳转
        http.exceptionHandling()
                .accessDeniedPage("/backstage/unauthen");

    }

    /**
     * DataSource和PersistenTokenRepository都是为了实现“记住我”的功能，
     * 并将相关信息存入数据库中
     */
//    @Resource
//    private DataSource dataSource;
//
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository(){
//        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(false);  // 第1此启动会创建表，下次要改为false
//        return jdbcTokenRepository;
//    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }

}
