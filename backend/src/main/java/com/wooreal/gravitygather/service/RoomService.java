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
import com.wooreal.gravitygather.utils.SHA256Util;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
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

    public List<Room> getRooms(RoomRequest roomRequest){
        return roomMapper.getRooms(roomRequest);
    }

    public Room getRoomBySeq(int seq){
        return roomMapper.getRoomBySeq(seq);
    }

    public void createRoom(RoomRequest roomRequest){
        List<Room> rooms = getRooms(roomRequest);
        if(rooms.size() >= 3){
            throw new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR, "방 생성 개수는 최대 3개 입니다.");
        }

        if(roomRequest.getIsLocked()){
            String oriPassword = roomRequest.getPassword();
            String salt = SHA256Util.generateSalt();
            String encPassword = SHA256Util.generateHashWithSalt(oriPassword, salt);
            roomRequest.setPassword(encPassword);
            roomRequest.setPasswordSalt(salt);
        }
        roomMapper.createRoom(roomRequest);
        int seq = roomRequest.getSeq();
        if(seq == 0) {
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 방 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }

        try {
            webSocketHandler.createRoomMsg(seq);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 방 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
    }

    public int enterRoom(RoomRequest roomRequest){
        return roomMapper.enterRoom(roomRequest);
    }

    public int leaveRoom(RoomRequest roomRequest){
        return roomMapper.leaveRoom(roomRequest);
    }

    public int updateRooms(List<RoomRequest> list){
        int successCnt = 0;
        for (RoomRequest roomRequest : list) {
            int res = roomMapper.updateRoom(roomRequest);
            successCnt += res > 0 ? 1 : 0;
        }
        return successCnt;
    }


}
