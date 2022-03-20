package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] travelGroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);

    List<Setmeal> getAll();

    Setmeal getById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map> getSetmealReport();

    void update(Setmeal setmeal, Integer[] travelGroupIds);
}
