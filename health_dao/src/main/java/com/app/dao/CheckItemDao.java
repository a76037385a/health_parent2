package com.app.dao;

import com.app01.pojo.CheckItem;
import com.github.pagehelper.Page;

public interface CheckItemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> findPageByCondition(String queryString);

    void deleteById(int id);

    Long findCountByCheckItemId(int id);

    CheckItem findCheckItemById(int id);

    void updateCheckItem(CheckItem checkItem);

}
