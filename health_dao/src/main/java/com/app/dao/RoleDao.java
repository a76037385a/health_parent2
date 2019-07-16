package com.app.dao;

import com.app01.pojo.Menu;
import com.app01.pojo.Role;

import java.util.LinkedHashSet;
import java.util.Set;

public interface RoleDao {

    Set<Role> findRolesByUserId(int userId);



}
