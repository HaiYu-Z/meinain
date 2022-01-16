package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {

    void add(TravelItem travelItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);

    TravelItem getById(Integer id);

    void update(TravelItem travelItem);

    List<TravelItem> getAll();
}
