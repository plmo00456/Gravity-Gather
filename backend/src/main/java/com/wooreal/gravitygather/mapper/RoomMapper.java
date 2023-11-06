package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomMapper {
    List<Room> getRooms();

    Room getRoomBySeq(int seq);

    int createRoom(RoomRequest roomRequest);
}
