package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.Utils.POI.POIUtils;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.OrderSetting;
import com.app01.service.OrderSettingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingsList = new ArrayList<>();

            if(list != null && list.size() > 0){
                for (String[] strings : list) {
                    orderSettingsList.add(
                            new OrderSetting
                                    (new Date(strings[0]),Integer.parseInt(strings[1])));
                }
            }
            orderSettingService.addOrderSetting(orderSettingsList);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        System.out.println(date);
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @PostMapping("/setNumberByOrderDate")
    public Result setOrderByNumber(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.setOrderByNumber(orderSetting);
        }catch (Exception e){
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }

        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
