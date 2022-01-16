package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.utli.POIUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {
    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);

            List<OrderSetting> listData = new ArrayList<>();
            for (String[] strings : list) {
                String dataStr = strings[0];
                String numberStr = strings[1];

                listData.add(new OrderSetting(new Date(dataStr), Integer.parseInt(numberStr)));
            }

            orderSettingService.addBatch(listData);

            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }
}
