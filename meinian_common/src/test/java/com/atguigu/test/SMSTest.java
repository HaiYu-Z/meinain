package com.atguigu.test;

import com.atguigu.utli.SMSUtils;

public class SMSTest {
    public static void main(String[] args) {
        try {
            SMSUtils.sendShortMessage("19965344706","3453");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
