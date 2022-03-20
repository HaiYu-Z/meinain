package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;

import java.util.List;

public interface TravelGroupService {

    void add(TravelGroup travelGroup, Integer[] travelItemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    TravelGroup getById(Integer id);

    Integer[] getTravelItemIdsByTravelGroup(Integer id);

    void delete(Integer id);

    void update(TravelGroup travelGroup, Integer[] travelItemIds);

    List<TravelGroup> getAll();

    Integer[] getTravelGroupIdsBySetmealId(Integer id);
}
