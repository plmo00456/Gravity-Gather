package com.wooreal.gravitygather.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.room.*;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.RoomMapper;
import com.wooreal.gravitygather.mapper.UserMapper;
import com.wooreal.gravitygather.utils.SHA256Util;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomMapper roomMapper;
    private final UserMapper userMapper;

    private final WebSocketHandler webSocketHandler;

    public RoomService(RoomMapper roomMapper, UserMapper userMapper, WebSocketHandler webSocketHandler) {
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
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
            System.out.println("salt : " + salt);
            System.out.println("encPassword : " + encPassword);
            System.out.println("oriPassword : " + oriPassword);

            roomRequest.setPassword(encPassword);
            roomRequest.setPasswordSalt(salt);
        }
        roomMapper.createRoom(roomRequest);
        int seq = roomRequest.getSeq();
        if(seq == 0) {
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 방 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        insChatLog("create", seq, null, roomRequest.getOwnerSeq());

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

    public List<UserResponse> getRoomParticipants(int roomId){
        Map<String, Set<RoomSession>> rooms = webSocketHandler.getMeetrooms();
        Set<RoomSession> roomSession = rooms.get(roomId+"");
        List<Integer> seqs = new ArrayList<>();
        for (RoomSession session : roomSession) {
            seqs.add(session.getSenderSeq());
        }

        List<User> participant = userMapper.getUserBySeqs(seqs);
        List<UserResponse> participants = participant.stream()
                .map(UserResponse::new)
                .map(user -> {
                    UserResponse ur = new UserResponse();
                    ur.setSeq(user.getSeq());
                    ur.setNickname(user.getNickname());
                    ur.setPhoto(user.getPhoto());
                    ur.setRoomMap(user.getRoomMap());
                    ur.setRoomCharacter(user.getRoomCharacter());
                    return ur;
                })
                .collect(Collectors.toList());
        return participants;
    }

    public int updateRooms(List<RoomRequest> list){
        int successCnt = 0;
        for (RoomRequest roomRequest : list) {
            int res = roomMapper.updateRoom(roomRequest);
            successCnt += res > 0 ? 1 : 0;
        }
        return successCnt;
    }

    public void insChatLog(String type, Integer room_seq, String msg, Integer sender_seq){
        ChatLog cl = new ChatLog(type, room_seq, msg, sender_seq);
        roomMapper.insChatLog(cl);
    }

    public Room canEnterRoom(RoomRequest roomRequest){
        if(roomRequest == null || roomRequest.getSeq() == null || roomRequest.getSeq() == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 방 입장 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        Room currentRoom = getRoomBySeq(roomRequest.getSeq());
        if(currentRoom == null || currentRoom.getSeq() == null || currentRoom.getSeq() == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 방 입장 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        if(currentRoom.getIs_locked()){
            String passwordSalt = currentRoom.getPassword_salt();
            String password = SHA256Util.generateHashWithSalt(roomRequest.getPassword(), passwordSalt);
            System.out.println("===================================");
            System.out.println(currentRoom);
            System.out.println(roomRequest.getPassword());
            System.out.println(passwordSalt);
            System.out.println(password);
            System.out.println(currentRoom.getPassword());
            System.out.println("===================================");
            if(!password.equals(currentRoom.getPassword())){
                throw new BusinessLogicException(HttpStatus.valueOf(401), "비밀번호가 틀렸습니다.");
            }
        }
        return currentRoom;
    }


}
