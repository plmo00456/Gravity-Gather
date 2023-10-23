package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User login(UserRequest userRequest);
}
