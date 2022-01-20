package com.atguigu.service;

import com.atguigu.entity.Result;

import java.util.Map;

public interface OrderService {

    Result saveOrder(Map<String, String> map);

    Map<String, Object> findById(Integer id) throws Exception;
}
