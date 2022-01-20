package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.utli.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public Result saveOrder(Map<String, String> map) {
        Integer setmealId = Integer.valueOf(map.get("setmealId"));

        // 1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
        String orderDate = map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(orderDate);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);

        // 2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
        } else if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 3. 判断是否是会员(根据手机号码查询t_member)
        String telephone = map.get("telephone");
        Member member = memberDao.findMemberByTelephone(telephone);
        if (member != null) {
            //     - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
            Order orderParam = new Order();
            orderParam.setMemberId(member.getId());
            orderParam.setSetmealId(setmealId);
            orderParam.setOrderDate(orderDate);

            List<Order> orderList = orderDao.findOrderByCondition(orderParam);
            if (orderList != null && orderList.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //     - 如果不是会员(不能够查询出来) ,自动注册为会员(直接向t_member插入一条记录)
            member = new Member(map.get("name"), map.get("sex"), map.get("idCard"), telephone, new Date());
            memberDao.add(member);
        }

        // 4.进行预约
        //     - 向t_order表插入一条记录
        Order order = new Order(member.getId(), orderDate, Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, setmealId);
        orderDao.add(order);
        //     - t_ordersetting表里面预约的人数reservations+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.updateReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    @Override
    public Map<String, Object> findById(Integer id) throws Exception {
        Map<String, Object> map = orderDao.findById(id);
        Date orderDate = (Date) map.get("orderDate");
        map.put("orderDate", DateUtils.parseDate2String(orderDate));
        return map;
    }
}
