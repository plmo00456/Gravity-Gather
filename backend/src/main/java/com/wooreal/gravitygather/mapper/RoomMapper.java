package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.room.ChatLog;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {
    List<Room> getRooms();

    List<Room> getRooms(RoomRequest roomRequest);

    Room getRoomBySeq(int seq);

    int createRoom(RoomRequest roomRequest);

    int enterRoom(RoomRequest roomRequest);

    int leaveRoom(RoomRequest roomRequest);

    int updateRoom(RoomRequest roomRequest);

    int deleteRoom(int roomId);

    void insChatLog(ChatLog cl);

    List<UserResponse> isInTheRooms(RoomRequest roomRequest);

    int inTheRoom(RoomRequest roomRequest);

    int leaveTheRoom(RoomRequest roomRequest);

    int outTheRoom(RoomRequest roomRequest);
}
