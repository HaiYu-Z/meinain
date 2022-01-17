package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.utli.Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal getById(Integer id) {
        return setmealDao.getById(id);
    }

    @Override
    public List<Setmeal> getAll() {
        return setmealDao.getAll();
    }

    @Override
    public void delete(Integer id) {
        setmealDao.deleteTravelGroupIdBySetmealId(id);
        setmealDao.delete(id);
    }

    @Override
    public void add(Setmeal setmeal, Integer[] travelGroupIds) {
        // 保存套餐数据
        setmealDao.add(setmeal);
        Integer id = setmeal.getId();

        // 补充代码 用于解决七牛云垃圾图片问题
        // 将图片名称存入Redis, 基于Redis的Set集合存储
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImgName());

        // 保存关联数据
        if (travelGroupIds.length > 0) {
            List<Map<String, Integer>> paramData = Utils.ergodicForIdsArray(id, travelGroupIds);
            setmealDao.setSetmealIdAndTravelGroupId(paramData);
        }
    }
}
