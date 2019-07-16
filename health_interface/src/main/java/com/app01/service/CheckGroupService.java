package com.app01.service;

import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.pojo.CheckGroup;
import com.app01.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckGroupService {

    PageResult findPage(QueryPageBean queryPageBean);

    List<CheckItem> getAllCheckitem();

    void addCheckGroup(CheckGroup checkGroup, Integer[] integers);

    void setCheckGroupAndCheckItem(int checkGroupId, Integer[] integers);

    Map getCheckGroupById(int id);

    void updateCheckGroup(CheckGroup checkGroup,Integer[] pickedCheckItems);

    void delByCheckGroupId(int id);


}
