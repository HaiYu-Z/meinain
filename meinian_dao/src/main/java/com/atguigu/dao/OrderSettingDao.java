package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderSettingDao {

    int findOrderSettingByOrderDate(@Param("orderDate") String orderDate);

    void add(OrderSetting orderSetting);

    void updateForOrderDate(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(@Param("startDate") String startDate,
                                              @Param("endDate") String endDate);
}
