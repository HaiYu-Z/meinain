package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelGroupService;
import com.atguigu.service.TravelItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/TravelGroup")
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;
    @Reference
    TravelItemService travelItemService;

    @RequestMapping("/add")
    public Result add(@RequestBody TravelGroup travelGroup,
                      Integer[] travelItemIds) {
        try {
            travelGroupService.add(travelGroup,travelItemIds);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TravelGroup travelGroup,
                      Integer[] travelItemIds) {
        try {
            travelGroupService.update(travelGroup,travelItemIds);
            return new Result(true, MessageConstant.UPDATE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPDATE_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            travelGroupService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return travelGroupService.findPage(queryPageBean);
    }

    @RequestMapping("/getAll")
    public Result getAll() {
        try {
            List<TravelGroup> list = travelGroupService.getAll();
            return new Result(true, null, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/editQuery")
    public Result editQuery(Integer id) {
        try {
            TravelGroup travelGroup = travelGroupService.getById(id);
            List<TravelItem> travelItems = travelItemService.getAll();
            Integer[] travelItemIds  = travelGroupService.getTravelItemIdsByTravelGroup(id);

            List<Object> list = new ArrayList();
            list.add(travelGroup);
            list.add(travelItems);
            list.add(travelItemIds);
            return new Result(true, null, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, null);
        }
    }
}
