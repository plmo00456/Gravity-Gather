package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.user.Friend;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User login(UserRequest userRequest);
    User getUserByEmail(String email);

    User getUserById(String email);

    User getUserBySeq(Integer seq);

    List<User> getUserBySeqs(List<Integer> seqs);

    int setUserActive(String email);

    int userUpdate(User user);

    List<Friend> getFriends(Friend friend);

    int addFriend(Friend friend);

    int deleteFriend(Friend friend);

    int register(UserRequest user);
}
