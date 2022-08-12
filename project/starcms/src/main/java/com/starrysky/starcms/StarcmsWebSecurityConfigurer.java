package com.starrysky.starcms;

import com.starrysky.starcms.security.LoginSuccessHandler;
import com.starrysky.starcms.security.SecurityUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                .antMatchers("/backstage/user/loginpage", "/css/**", "/js/**", "/img/**", "/fonts/**", "/jquery-ui/**", "/fuelux/**", "/sweetalert/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/backstage/user/loginpage")
                .loginProcessingUrl("/backstage/user/login")
                .usernameParameter("name")
                .passwordParameter("password")
                .defaultSuccessUrl("/backstage/index", true)
                .successHandler(loginSuccessHandler)
                .failureUrl("/backstage/user/loginpage")
                .and()
                .logout()
                .logoutUrl("/backstage/user/logout")
                .logoutSuccessUrl("/backstage/user/loginpage")
                .invalidateHttpSession(true)

                .and()
                .csrf().disable();
    }
}
