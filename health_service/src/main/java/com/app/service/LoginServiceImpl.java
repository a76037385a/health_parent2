package com.app.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.app.dao.MemberDao;
import com.app01.SMS.SmsConstant;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.Member;
import com.app01.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    JedisPool jedisPool;


    @Override
    public Result loginCheck(Map map) {

        String phoneNumber = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String redisValidateCode = jedisPool.getResource().get(phoneNumber+ SmsConstant.redisLoginMrak);


        System.out.println(phoneNumber);
        System.out.println(validateCode);
        System.out.println(redisValidateCode);

        //redis没有验证码,没有发送验证码,手机号码为空 false
        if (redisValidateCode == null || validateCode == null || phoneNumber == null){
            return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }
        //验证码不正确 false
        if(!validateCode.equals(redisValidateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        Member member = findMember(phoneNumber);
        if (member != null){
            //有该用户,登录成功
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else{
            //增加用户
            addMember(map);
            //登录成功
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }



    }


    //查找用户
    public Member findMember(String phoneNumber) {
        return memberDao.findMemberByPhoneNumber(phoneNumber);
    }

    //新增用户
    public void addMember(Map map) {
        Member member = new Member();
        member.setPhoneNumber((String) map.get("telephone"));
        member.setRegTime(new Date());
        memberDao.addMember(member);
    }


}
