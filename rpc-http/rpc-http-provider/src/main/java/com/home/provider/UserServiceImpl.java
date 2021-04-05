package com.home.provider;

import com.home.api.User;
import com.home.api.UserService;

import java.time.LocalDateTime;

/**
 * 用户接口的实现类
 */
public class UserServiceImpl implements UserService {
    @Override
    public User findById(int id) {
        return User.builder()
                .id(id)
                .name("rpc time: "+ LocalDateTime.now())
                .build();
    }
}
