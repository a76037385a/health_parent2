package com.app.dao;

import com.app01.pojo.User;

public interface UserDao {

    User findUserByUsername(String username);

}
