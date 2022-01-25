package com.atguigu.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

    @org.junit.Test
    public void test() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("2001"));
    }
}
