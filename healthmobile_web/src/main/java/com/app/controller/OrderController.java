package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.Order;
import com.app01.service.OrderService;
import org.springframework.messaging.handler.MessagingAdviceBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderService orderService;

    @GetMapping("/findORderById")
    public Result findORderById(int id) {
        Map order = null;
        try {
            order  = orderService.findOrderById(id);
        }catch (Exception e){
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,order);
    }
}
