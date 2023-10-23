package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User login(UserRequest userRequest){
        User user = userMapper.login(userRequest);
        if (user == null || !user.getPassword().equals(userRequest.getPassword())) {
            return null;
        }
        return userMapper.login(userRequest);
    }

}
