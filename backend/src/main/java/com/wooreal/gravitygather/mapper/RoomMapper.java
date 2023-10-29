package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.room.Room;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomMapper {
    List<Room> getRooms();
}
