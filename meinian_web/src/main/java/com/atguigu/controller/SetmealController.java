package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.SetmealService;
import com.atguigu.service.TravelGroupService;
import com.atguigu.utli.AliyunOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;
    @Reference
    TravelGroupService travelGroupService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/editQuery")
    public Result editQuery(Integer id) {
        try {
            Setmeal setmeal = setmealService.getById(id);
            List<TravelGroup> travelGroups = travelGroupService.getAll();
            Integer[] travelGroupIds = travelGroupService.getTravelGroupIdsBySetmealId(id);
            // TravelGroup travelGroup = travelGroupService.getById(id);
            // List<TravelItem> travelItems = travelItemService.getAll();
            // Integer[] travelItemIds  = travelGroupService.getTravelItemIdsByTravelGroup(id);

            List<Object> list = new ArrayList();
            list.add(setmeal);
            list.add(travelGroups);
            list.add(travelGroupIds);
            return new Result(true, null, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, null);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,
                      Integer[] travelGroupIds) {
        try {
            setmealService.add(setmeal, travelGroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,
                         Integer[] travelGroupIds) {
        try {
            setmealService.update(setmeal,travelGroupIds);
            return new Result(true, MessageConstant.UPDATE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPDATE_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean);
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        try {
            // 1.获取原始文件名称
            String originalFilename = imgFile.getOriginalFilename();
            // 2.生成新的文件名称（防止上传同名文件被覆盖）
            int index = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(index);
            String imgName = UUID.randomUUID().toString().replace("-","").toUpperCase() + suffix;
            // 3.调用工具类，上传图片到七牛云
            AliyunOSSUtils.upload(imgFile.getBytes(), imgName);

            // 补充代码 用于解决七牛云垃圾图片问题
            // 将图片名称存入Redis, 基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, imgName);

            // 4.返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, new String[]{new Setmeal().getImgUrl(), imgName});
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
