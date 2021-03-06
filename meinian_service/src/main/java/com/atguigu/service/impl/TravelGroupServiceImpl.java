package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.atguigu.utli.Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        travelGroupDao.add(travelGroup);
        Integer id = travelGroup.getId();
        if (travelItemIds.length > 0) {
            List<Map<String, Integer>> paramData = Utils.ergodicForIdsArray(id, travelItemIds);
            travelGroupDao.setTravelGroupAndTravelItem(paramData);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<TravelGroup> page = travelGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public TravelGroup getById(Integer id) {
        return travelGroupDao.getById(id);
    }

    @Override
    public Integer[] getTravelItemIdsByTravelGroup(Integer id) {
        return travelGroupDao.getTravelItemIdsByTravelGroup(id);
    }

    @Override
    public void delete(Integer id) {
        travelGroupDao.deleteTravelItemIdsByTravelGroup(id);
        travelGroupDao.delete(id);
    }

    @Override
    public void update(TravelGroup travelGroup, Integer[] travelItemIds) {
        travelGroupDao.update(travelGroup);
        Integer id = travelGroup.getId();

        // ?????????????????????????????????
        travelGroupDao.deleteTravelItemIdsByTravelGroup(id);
        // ???????????????????????????
        if (travelItemIds.length > 0) {
            List<Map<String, Integer>> paramData = Utils.ergodicForIdsArray(id, travelItemIds);
            travelGroupDao.setTravelGroupAndTravelItem(paramData);
        }
    }

    @Override
    public Integer[] getTravelGroupIdsBySetmealId(Integer id) {
        return travelGroupDao.getTravelGroupIdsBySetmealId(id);
    }

    @Override
    public List<TravelGroup> getAll() {
        return travelGroupDao.getAll();
    }
}
