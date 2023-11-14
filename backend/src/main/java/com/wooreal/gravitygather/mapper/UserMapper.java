package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User login(UserRequest userRequest);
    User getUserByEmail(String email);

    User getUserBySeq(Integer seq);

    List<User> getUserBySeqs(List<Integer> seqs);

    int setUserActive(String email);

    int userUpdate(User user);
}
