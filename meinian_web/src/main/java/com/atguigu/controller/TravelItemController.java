package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/TravelItem")
public class TravelItemController {
    @Reference
    TravelItemService travelItemService;

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
    public Result add(@RequestBody TravelItem travelItem) {
        try {
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE123')")//权限校验
    public Result delete(Integer id) {
        try {
            travelItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result update(@RequestBody TravelItem travelItem) {
        try {
            travelItemService.update(travelItem);
            return new Result(true, MessageConstant.UPDATE_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPDATE_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return travelItemService.findPage(queryPageBean);
    }

    @RequestMapping("/getById")
    public Result getOne(Integer id) {
        try {
            TravelItem travelItem = travelItemService.getById(id);
            return new Result(true, null, travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, null);
        }
    }

    @RequestMapping("/getAll")
    public Result getAll() {
        try {
            List<TravelItem> list = travelItemService.getAll();
            return new Result(true, null, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, null);
        }
    }

}
