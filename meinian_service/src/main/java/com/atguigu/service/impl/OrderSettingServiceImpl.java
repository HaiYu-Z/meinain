package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addBatch(List<OrderSetting> listData) {
        for (OrderSetting orderSetting : listData) {
            // 如果日期在数据库存在则修改
            int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
            if (count > 0) {
                orderSettingDao.updateForOrderDate(orderSetting);
            } else {
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public void updateAndAdd(OrderSetting orderSetting) {
        // 如果日期在数据库存在则修改
        int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            orderSettingDao.updateForOrderDate(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public List<OrderSetting> getOrderSettingByMonth(String date) {
        String startDate = date + "-1"; // 2022-1-1
        String endDate = date + "-31"; // 2022-1-31
        return orderSettingDao.getOrderSettingByMonth(startDate, endDate);
    }
}