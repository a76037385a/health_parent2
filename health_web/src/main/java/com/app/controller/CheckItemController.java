package com.app.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.pojo.CheckItem;
import com.app01.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    CheckItemService checkItemService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result addCheckItem(@RequestBody CheckItem checkItem) {
        try {
            //do service
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findPageByCondition(queryPageBean);
        return pageResult;
    }

    @GetMapping("/deleteRow")
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    public Result deleteRow(int id) {
        try {
            checkItemService.deleteCheckItemById(id);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @GetMapping("/findById")
    public Result findById(int id) {
        CheckItem checkItem = checkItemService.findCheckItemById(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    @PostMapping("/editCheckItemById")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result editCheckItemById(@RequestBody CheckItem checkItem) {
        System.out.println(checkItem);
        System.out.println();
        try {
            checkItemService.editCheckItemById(checkItem);

        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

}
