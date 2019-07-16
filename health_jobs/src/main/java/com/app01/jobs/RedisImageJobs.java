package com.app01.jobs;

import com.app01.Utils.ImageUtil.QiniuUtils;
import com.app01.redis.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class RedisImageJobs {

    @Autowired
    JedisPool jedisPool;

    public void cleanTempImage() {
        System.out.println("start cleanTempImageing......");

        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.imgtemp,RedisConstant.imgDb);
        int setSize = set.size();
        if(!set.isEmpty()){
            for (String pic : set) {
                QiniuUtils.deleteFileFromQiniu(pic);
                jedisPool.getResource().srem(RedisConstant.imgtemp,pic);
            }
            System.out.println("Cleaned " + setSize + "image Success..!");
        }else{
            System.out.println("Set isEmpty!");
        }

    }
}
