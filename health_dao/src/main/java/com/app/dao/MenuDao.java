package com.app.dao;

import com.app01.pojo.Menu;
import java.util.List;

import java.util.LinkedHashSet;

public interface MenuDao {

    LinkedHashSet<Menu> findMenusByRoleId(int roleId);

    List<Menu> findMenusByMenuId(int menuid);
}
