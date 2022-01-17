package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TravelItemDao {

    void add(TravelItem travelItem);

    Page findPage(@Param("queryString") String queryString);

    void delete(@Param("id") Integer id);

    TravelItem getById(Integer id);

    void update(TravelItem travelItem);

    List<TravelItem> getAll();

    void deleteTravelItemIdsFromTravelGroup(Integer id);

    /**
     * 封装TravelGroup对象的travelItems属性
     * @param id
     * @return
     */
    List<TravelItem> getTravelItemById(Integer id);
}
