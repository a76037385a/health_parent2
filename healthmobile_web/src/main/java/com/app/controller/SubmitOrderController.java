package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.app01.Utils.SMSUtils.SMSUtils;
import com.app01.Utils.validate.ValidateCodeUtils;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.Order;
import com.app01.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class SubmitOrderController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;


    @PostMapping("/submit")
    public Result submitOrder(@RequestBody Map map)  {

        System.out.println(map);

        Result result = null;

        String phone = (String) map.get("telephone");

        String phoneValidate = (String) map.get("validateCode");
        String redisValidate = jedisPool.getResource().get(phone);


        //验证码验证失败
        if(redisValidate == null && !phoneValidate.equals(redisValidate)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }


        //service判断
        try {
            //调用service
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            System.out.println("调用service");
            result = orderService.submitOrder(map);

        }catch (Exception e){
            return result;
        }


        //发送短信
        if(result.isFlag()) {
            try {
                //发送验证码
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, phone, "aaa123");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return result;

    }

}
