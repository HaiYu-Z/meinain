package com.atguigu.dao;

import com.atguigu.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    void add(Order order);

    List<Order> findOrderByCondition(Order order);

    Map<String, Object> findById(Integer id);
}
