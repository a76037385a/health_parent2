package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.CheckGroup;
import com.app01.pojo.CheckItem;
import com.app01.service.CheckGroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    CheckGroupService checkGroupService;


    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.findPage(queryPageBean);
        return pageResult;
    }

    @GetMapping("/getAllCheckitem")
    public Result getAllCheckitem() {
        List<CheckItem> list = checkGroupService.getAllCheckitem();
        if(list.size()>0){
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }

    @PostMapping("/addCheckGroup")
    public Result addCheckGroup(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds)  {
        try {
            checkGroupService.addCheckGroup(checkGroup,checkitemIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //System.out.println(checkGroup.toString());
        //System.out.println(checkitemIds.toString());
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @GetMapping("/getCheckGroup")
    public Result getCheckGroupById( int id) {
        Map map = new HashMap();
        try {
            map = checkGroupService.getCheckGroupById(id);
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,map);
    }
    @PostMapping("/updateCheckGroup")
    public Result updateCheckGroup(@RequestBody CheckGroup checkGroup, Integer[] pickedCheckItems) {
        try {
            checkGroupService.updateCheckGroup(checkGroup,pickedCheckItems);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @GetMapping("/delCheckGroup")
    public Result delByCheckGroupId(int id) {
        try {
            checkGroupService.delByCheckGroupId(id);
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
