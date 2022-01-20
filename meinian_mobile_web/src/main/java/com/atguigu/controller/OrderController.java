package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map<String, String> map) {
        try {
            String telephone = map.get("telephone");
            String validateCode = map.get("validateCode");
            String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            // 判断验证码是否正确
            if (redisCode == null || !redisCode.equals(validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            return orderService.saveOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map<String, Object> map = orderService.findById(id);
            return new Result(true, MessageConstant.ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
