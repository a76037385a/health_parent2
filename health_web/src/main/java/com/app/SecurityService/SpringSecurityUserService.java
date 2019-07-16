package com.app.SecurityService;


import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.pojo.Permission;
import com.app01.pojo.Role;
import com.app01.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {


    @Reference
    UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.app01.pojo.User user = userService.findUserByUsername(username);

        if(user == null){
            return null;
        }

        List<GrantedAuthority> list = new ArrayList<>();

        Set<Role> roles = user.getRoles();

        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            if (permissions != null && permissions.size() >0){
                for (Permission permission : permissions) {
                    list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }
        }

        UserDetails userDetails = new User(username, user.getPassword(), list);

        return userDetails;
    }
}
