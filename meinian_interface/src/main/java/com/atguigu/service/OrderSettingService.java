package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.List;

public interface OrderSettingService {

    void addBatch(List<OrderSetting> listData);

    List<OrderSetting> getOrderSettingByMonth(String date);

    void updateAndAdd(OrderSetting orderSetting);
}
