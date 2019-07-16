package com.app.dao;

import com.app01.entity.QueryPageBean;
import com.app01.pojo.CheckGroup;
import com.app01.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    Page<CheckGroup> findPage(String queryString);

    List<CheckItem> getAllCheckitem();

    CheckGroup getCheckGroupById(int id);

    List<Integer> getCheckItemsByCheckGroupID(int id);

    void addCheckGroup(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map map);

    void delCheckgroupAndcheckitemByCheckGroupId(int grpId);

    void updateCheckGroup(CheckGroup checkGroup);

    void delCheckGroupById(int id);

}
