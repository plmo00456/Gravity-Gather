package com.wooreal.gravitygather.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.RoomMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomMapper roomMapper;
    private final WebSocketHandler webSocketHandler;

    public RoomService(RoomMapper roomMapper, WebSocketHandler webSocketHandler) {
        this.roomMapper = roomMapper;
        this.webSocketHandler = webSocketHandler;
    }

    public List<Room> getRooms(){
        return roomMapper.getRooms();
    }

    public Room getRoomBySeq(int seq){
        return roomMapper.getRoomBySeq(seq);
    }

    public void createRoom(RoomRequest roomRequest){
        roomMapper.createRoom(roomRequest);
        int seq = roomRequest.getSeq();
        if(seq == 0) {
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 방 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        RoomResponse currentRoom = new RoomResponse(getRoomBySeq(seq));

        Gson gson = new Gson();
        JsonElement roomInfoElement = gson.toJsonTree(currentRoom);
        JsonObject result = new JsonObject();
        result.addProperty("type1", "room");
        result.addProperty("type2", "create");
        result.add("roomInfo", roomInfoElement);

        try {
            System.out.println(result.toString());
            webSocketHandler.sendMessageToAll(result.toString());
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 방 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
    }

}
