package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.utli.SMSUtils;
import com.atguigu.utli.ValidateCodeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    Logger logger = Logger.getLogger(ValidateCodeController.class);

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/send")
    public Result send(String telephone, String state) {
        try {
            // 1.生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 2.发送验证码
            SMSUtils.sendShortMessage(telephone, code);
            // 手机号加上后缀，防止在redis中被覆盖
            switch (state) {
                case "order":
                    state = RedisMessageConstant.SENDTYPE_ORDER;
                    break;
                case "login":
                    state = RedisMessageConstant.SENDTYPE_LOGIN;
                    break;
                case "getpwd":
                    state = RedisMessageConstant.SENDTYPE_GETPWD;
            }
            // 3.将将验证码存储到redis中，便于后期验证
            jedisPool.getResource().setex(telephone + state, 5 * 60, code);

            logger.info("telephone = " + telephone + " code = " + code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
