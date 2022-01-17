package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void setTravelGroupAndTravelItem(@Param("paramData") List<Map<String, Integer>> paramData);

    Page<TravelGroup> findPage(@Param("queryString") String queryString);

    TravelGroup getById(Integer id);

    Integer[] getTravelItemIdsByTravelGroup(Integer id);

    void delete(Integer id);

    void deleteTravelItemIdsByTravelGroup(Integer id);

    void update(TravelGroup travelGroup);

    List<TravelGroup> getAll();

    /**
     * 封装Setmeal对象的travelGroups属性
     * @param id
     * @return
     */
    List<TravelGroup> getTravelGroupById(Integer id);
}
