package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.QiNiuCloud.CloudKeys;
import com.app01.Utils.ImageUtil.QiniuUtils;
import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.CheckGroup;
import com.app01.pojo.Package;
import com.app01.redis.RedisConstant;
import com.app01.service.SetmealService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @PostMapping("/findAllPage")
    public Result findAllPage(@RequestBody  QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findAllPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }

    @PostMapping("/upload")
    public Result imgeupload(@RequestParam("imgFile") MultipartFile imgFile) throws IOException {
        //获得原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        //获得文件后缀名
        String mini = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        //拼接文件名UUID名
        String newFileName = UUID.randomUUID() + "." + mini;
        String imageUrlPath = CloudKeys.domain;
        try {
            //上传到云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),newFileName);
            //Sadd到redis
            jedisPool.getResource().sadd(RedisConstant.imgtemp,newFileName);
            //生成url返回
            imageUrlPath += newFileName;
        }catch (Exception e){
            //上传失败
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,imageUrlPath);
    }

    @GetMapping("/getAllCheckGroup")
    public Result getAllCheckGroup() {
        List<CheckGroup> list = new ArrayList<>();
        try {
            list = setmealService.getAllCheckGroup();
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }

    @PostMapping("/addPackge")
    public Result addPackge(@RequestBody Package packge, Integer[] pickCheckGroup) {
        System.out.println(packge.getImg());
        try {
            setmealService.addPackage(packge,pickCheckGroup);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }
}
