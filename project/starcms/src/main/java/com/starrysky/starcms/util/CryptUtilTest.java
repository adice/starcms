package com.starrysky.starcms.util;

/**
 * @ClassName CryptUtil
 * @Description
 * @Author adi
 * @Date 2022-07-29 11:02
 */
public class CryptUtilTest {
    public static void main(String[] args) {
        String pwd = BCrypt.hashpw("123123", BCrypt.gensalt());
        System.out.println(pwd);
    }
}
