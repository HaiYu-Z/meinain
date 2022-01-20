package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void setSetmealIdAndTravelGroupId(@Param("paramData") List<Map<String, Integer>> paramData);

    Page<Setmeal> findPage(@Param("queryString") String queryString);

    void delete(Integer id);

    void deleteTravelGroupIdBySetmealId(Integer id);

    List<Setmeal> getAll();

    Setmeal getById(Integer id);

    Setmeal getSetmealById(Integer id);
}
