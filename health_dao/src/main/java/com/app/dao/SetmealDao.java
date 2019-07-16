package com.app.dao;

import com.app01.pojo.CheckGroup;
import com.app01.pojo.Package;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDao {

    Page<Package> findAllPage(String queryString);

    List<CheckGroup> getAllCheckGroup();

    void addPackgeAndCheckGroupByPackgeId(@Param("pid") int pid, @Param("gid") int gid);

    void addPackge(Package packge);

    List<Package> findAllPackage();

    Package findPackgeInfosById(int id);

    Package findPackgeByid(int id);
}
