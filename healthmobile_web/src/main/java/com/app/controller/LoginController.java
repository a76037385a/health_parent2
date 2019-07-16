package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.http.HttpResponse;
import com.app01.SMS.SmsConstant;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import sun.plugin2.message.Message;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    LoginService loginService;

    @PostMapping("/check")
    public Result check(HttpServletResponse resp ,@RequestBody Map map) {

        //System.out.println(map.get("telephone").toString());
        //System.out.println(map.get("validateCode").toString());

        Result result = loginService.loginCheck(map);
        if (!result.isFlag()){
            //登录失败
            System.out.println("登录失败");
            return result;
        }else {
            //登录成功,设置cookie
            System.out.println("登录成功");
            Cookie cookie = new Cookie("login_member_telephone", (String) map.get("telephone"));
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);//30day
            resp.addCookie(cookie);
            return result;
        }

    }

}
