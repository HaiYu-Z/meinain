package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface OrderSettingDao {

    int findOrderSettingByOrderDate(@Param("orderDate") Date orderDate);

    void add(OrderSetting orderSetting);

    void updateForOrderDate(OrderSetting orderSetting);
}
