package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomMapper roomMapper;

    public RoomService(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    public List<Room> getRooms(){
        return roomMapper.getRooms();
    }


}
