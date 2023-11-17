package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.room.ChatLog;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
}
