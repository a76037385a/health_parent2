package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.Package;
import com.app01.service.SetmealService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @GetMapping("/getSetmeal")
    public Result getSetmeal() {
        System.out.println("get");
        try {
            List<Package> list = setmealService.findAllPackage();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @GetMapping("/findPackgeInfosById")
    public Package findPackgeInfosById(int id) {
        System.out.println(id);
        return setmealService.findPackgeInfosById(id);
    }

    @GetMapping("/findPackgeByid")
    public Result findPackgeByid(int id) {
        System.out.println(id);
        try {
            Package pac = setmealService.findPackgeByid(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pac);
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
