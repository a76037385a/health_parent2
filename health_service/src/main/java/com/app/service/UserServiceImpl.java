package com.app.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.app.dao.MenuDao;
import com.app.dao.PermissionDao;
import com.app.dao.RoleDao;
import com.app.dao.UserDao;
import com.app01.pojo.Menu;
import com.app01.pojo.Permission;
import com.app01.pojo.Role;
import com.app01.pojo.User;
import com.app01.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PermissionDao permissionDao;

    @Autowired
    MenuDao menuDao;

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        if (user == null){
            return null;
        }

        //查找 用户组
        Set<Role> roles = roleDao.findRolesByUserId(user.getId());

        if(roles != null && roles.size() > 0){
            for (Role role : roles) {
                int roleId = role.getId();

                //查找操作权限
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if(permissions != null && permissions.size()>0){
                    role.setPermissions(permissions);
                }
                //查找菜单权限
                LinkedHashSet<Menu> menus = menuDao.findMenusByRoleId(roleId);
                if(menus != null && menus.size()>0){
                    for (Menu menu : menus) {
                        List<Menu> cmenu = menuDao.findMenusByMenuId(menu.getId());
                        menu.setChildren(cmenu);
                    }
                    role.setMenus(menus);
                }

            }
            user.setRoles(roles);
        }



        return user;
    }

    public Boolean isNullOrEmpty(Set set) {

        if(set == null){
            return false;
        }

        if(set.size() == 0){
            return false;
        }

        return null;
    }
}
