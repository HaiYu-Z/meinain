package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] travelGroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);
}
