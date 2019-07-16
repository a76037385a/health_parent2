package com.app01.service;

import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.pojo.CheckItem;

public interface CheckItemService {

    //新增检查项
    void add(CheckItem checkItem);

    //检查项分页查询
    PageResult findPageByCondition(QueryPageBean queryPageBean);

    //删除检查项
    void deleteCheckItemById(int id);

    CheckItem findCheckItemById(int id);

    void editCheckItemById(CheckItem checkItem);


}
