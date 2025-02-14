package com.lab.mapper;

import com.lab.pojo.User;

import java.util.List;

public interface UserMapper {


    public List<User> findAllUser();

    public User findOneUser(User user);

}
