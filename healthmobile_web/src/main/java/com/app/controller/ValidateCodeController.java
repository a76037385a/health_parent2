package com.app.controller;


import com.app01.SMS.SmsConstant;
import com.app01.Utils.SMSUtils.SMSUtils;
import com.app01.Utils.validate.ValidateCodeUtils;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validate")
public class ValidateCodeController {

   @Autowired
   JedisPool jedisPool;


    @GetMapping("/sendSMS")
    public Result sendSMS(String telephone) {
        System.out.println("telephone="+telephone);

        Integer code = ValidateCodeUtils.generateValidateCode(4);

        //发送4位验证码
        //try {
        //    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        //}catch (Exception e){
        //    return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        //}

        //缓存5*60秒
        jedisPool.getResource().setex(telephone,5*60,code.toString());


        System.out.println("code="+code);

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @GetMapping("/send4Login")
    public Result send4Login(String telephone) {

        Integer loginCode = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(telephone);
        System.out.println("loginCode="+loginCode);

        //发送6位验证码
        //try {
        //    SMSUtils.sendShortMessage(SmsConstant.VALIDATE_CODE,phoneNumber,loginCode.toString());
        //}catch (Exception e){
        //    return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        //}

        //缓存
        jedisPool.getResource().setex(telephone+SmsConstant.redisLoginMrak,5 *60,loginCode.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


}
