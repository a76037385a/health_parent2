package com.app.dao;

import com.app01.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(int roleId);
}
