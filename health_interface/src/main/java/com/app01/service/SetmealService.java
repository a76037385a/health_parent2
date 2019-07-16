package com.app01.service;

import com.app01.entity.PageResult;
import com.app01.entity.QueryPageBean;
import com.app01.pojo.CheckGroup;
import com.app01.pojo.Package;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.util.List;

public interface SetmealService {

    PageResult findAllPage(QueryPageBean queryPageBean);

    List<CheckGroup> getAllCheckGroup();

    void addPackage(Package packge, Integer[] pickCheckGroups);

    List<Package> findAllPackage();

    Package findPackgeInfosById(int id);

    Package findPackgeByid(int id);



}
