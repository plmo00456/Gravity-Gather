package com.wooreal.gravitygather.service;

import com.google.gson.JsonObject;
import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.room.ChatLog;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.RoomMapper;
import com.wooreal.gravitygather.mapper.UserMapper;
import com.wooreal.gravitygather.utils.SHA256Util;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RedisService redisService;
    private final RoomMapper roomMapper;
    private final UserService userService;

    private final WebSocketHandler webSocketHandler;

    public RoomService(RoomMapper roomMapper, UserService userService, WebSocketHandler webSocketHandler, RedisService redisService) {
        this.roomMapper = roomMapper;
        this.userService = userService;
        this.webSocketHandler = webSocketHandler;
        this.redisService = redisService;
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

    public RoomResponse createRoom(RoomRequest roomRequest){
        Integer ownerSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roomRequest.setOwnerSeq(ownerSeq);
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
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 룸 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        roomRequest.setUserSeq(ownerSeq);
        inTheRoom(roomRequest);
        insChatLog("create", seq, null, roomRequest.getOwnerSeq());

        try {
            webSocketHandler.createRoomMsg(seq);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 룸 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        return new RoomResponse(getRoomBySeq(seq));
    }

    public int enterRoom(RoomRequest roomRequest)
    {
        return roomMapper.enterRoom(roomRequest);
    }

    public int leaveRoom(RoomRequest roomRequest){
        return roomMapper.leaveRoom(roomRequest);
    }

    public void deleteRoom(int roomId){
        Room roomInfo = getRoomBySeq(roomId);
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(roomInfo == null || !roomInfo.getOwner_seq().equals(seq)){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "권한이 없습니다.");
        }
        int result = roomMapper.deleteRoom(roomId);
        if(result == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 룸 삭제 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setSeq(roomId);
        outTheRoom(roomRequest);
    }

    public List<UserResponse> getRoomParticipants(int roomId){
        RoomRequest room = new RoomRequest();
        room.setSeq(roomId);
        return roomMapper.isInTheRooms(room);
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
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 룸 입장 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        Room currentRoom = getRoomBySeq(roomRequest.getSeq());
        if(currentRoom == null || currentRoom.getSeq() == null || currentRoom.getSeq() == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "미팅 룸 입장 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }

        if(currentRoom.getCurrent_participant().equals(currentRoom.getMax_participant())){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "방 인원이 가득 찼습니다.");
        }

        String redisAuthCode = redisService.getValues("InviteCode " + roomRequest.getUserSeq() + " " + roomRequest.getSeq());
        boolean authResult = redisService.checkExistsValue(redisAuthCode);
        if(authResult) return currentRoom;

        if(currentRoom.getIs_locked()){
            String passwordSalt = currentRoom.getPassword_salt();
            String password = SHA256Util.generateHashWithSalt(roomRequest.getPassword(), passwordSalt);
            if(!password.equals(currentRoom.getPassword())){
                throw new BusinessLogicException(HttpStatus.valueOf(401), "비밀번호가 틀렸습니다.");
            }
        }
        return currentRoom;
    }

    public List<UserResponse> isInTheRooms(){
        RoomRequest roomRequest = new RoomRequest();
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roomRequest.setUserSeq(userSeq);
        return roomMapper.isInTheRooms(roomRequest);
    }

    public UserResponse isInTheRoom(){
        RoomRequest roomRequest = new RoomRequest();
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        roomRequest.setUserSeq(userSeq);
        List<UserResponse> rooms = roomMapper.isInTheRooms(roomRequest);
        return rooms == null || rooms.size() == 0 ? null : rooms.get(0);
    }

    public void inTheRoom(RoomRequest roomRequest){
        roomMapper.inTheRoom(roomRequest);
    }

    public void leaveTheRoom(RoomRequest roomRequest){
        roomMapper.leaveTheRoom(roomRequest);
    }

    public void outTheRoom(RoomRequest roomRequest){
        roomMapper.outTheRoom(roomRequest);
    }
    public void outTheRoom(){
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setUserSeq(userSeq);
        roomMapper.outTheRoom(roomRequest);
    }

    public void kick(RoomRequest roomRequest){
        Integer roomSeq = roomRequest.getSeq();
        // 방장인지 확인
        Room room = getRoomBySeq(roomSeq);
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(room.getOwner_seq().equals(userSeq)){
            User kickUser = userService.getUserBySeq(roomRequest.getUserSeq());
            if(kickUser == null){
                throw new BusinessLogicException(HttpStatus.valueOf(500), "추방 할 상대가 없습니다.");
            }
            outTheRoom(roomRequest);
            JsonObject jo = new JsonObject();
            String type1 = "room";
            String type2 = "kick";
            String roomId = roomSeq+"";
            String receiveSeq = roomRequest.getUserSeq()+"";
            jo.addProperty("type1", type1);
            jo.addProperty("type2", type2);
            jo.addProperty("roomId",roomId);
            jo.addProperty("receiveSeq",receiveSeq);
            jo.addProperty("receiveNickname",kickUser != null ? kickUser.getNickname() : "[알수없음]");
            try{
                webSocketHandler.sendMessageToRoom(roomRequest.getSeq(), jo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            throw new BusinessLogicException(HttpStatus.valueOf(500), "권한이 없습니다.");
        }
    }

}
