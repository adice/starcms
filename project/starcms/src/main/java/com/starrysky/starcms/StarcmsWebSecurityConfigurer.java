//package com.starrysky.starcms;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
///**
// * @ClassName StarcmsWebSecurityConfigurer
// * @Description
// * @Author adi
// * @Date 2022-08-10 10:11
// */
//public class StarcmsWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
//    @Bean
//    public PasswordEncoder getPassWordENcoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/backstage/user/login", "/css", "/js", "/img", "/fonts", "/jquery-ui", "/fuelux", "/sweetalert").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/backstage/login.html")
//                .loginProcessingUrl("/backstage/user/login")
//                .usernameParameter("name")
//                .passwordParameter("password")
//                .successForwardUrl("/backstage/index")
//
//                .and()
//                .csrf().disable();
//    }
//}
