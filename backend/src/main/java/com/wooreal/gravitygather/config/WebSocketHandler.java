package com.wooreal.gravitygather.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.dto.room.RoomSession;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.service.RedisService;
import com.wooreal.gravitygather.service.RoomService;
import com.wooreal.gravitygather.service.UserService;
import com.wooreal.gravitygather.utils.comUtil;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Getter
public class WebSocketHandler extends TextWebSocketHandler {

    private final int InviteSec = 11000;

    private final RedisService redisService;
    private final RoomService roomService;
    private final UserService userService;

    public WebSocketHandler(@Lazy RoomService roomService, @Lazy UserService userService, @Lazy RedisService redisService) {
        this.roomService = roomService;
        this.userService = userService;
        this.redisService = redisService;
    }

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();
    private Map<String, Set<RoomSession>> meetrooms = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENTS.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CLIENTS.remove(session.getId(), session);

        Optional<RoomSession> roomSessionOpt = meetrooms.values().stream()
                .flatMap(Set::stream)
                .filter(roomSession -> roomSession.getSession().getId().equals(session.getId()))
                .findFirst();

        roomSessionOpt.ifPresent(roomSession -> {
            JsonObject jo = new JsonObject();
            jo.addProperty("type1", "room");
            jo.addProperty("type2", "leave");
            jo.addProperty("roomId", roomSession.getRoomId());
            jo.addProperty("sender", roomSession.getSender());
            jo.addProperty("senderSeq", roomSession.getSenderSeq());

            try {
                handleTextMessage(session, new TextMessage(jo.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
        String type1 = comUtil.hasIsNullChk(jsonObject, "type1", null);
        String type2 = comUtil.hasIsNullChk(jsonObject, "type2", null);
        String roomId = comUtil.hasIsNullChk(jsonObject, "roomId", null);
        String sender = comUtil.hasIsNullChk(jsonObject, "sender", null);
        String senderPhoto = comUtil.hasIsNullChk(jsonObject, "senderPhoto", null);
        String senderSeq = comUtil.hasIsNullChk(jsonObject, "senderSeq", null);
        String receiveSeq = comUtil.hasIsNullChk(jsonObject, "receiveSeq", null);
        String content = comUtil.hasIsNullChk(jsonObject, "content", null);

        if (type1.equals("room")) {
            JsonObject jo = new JsonObject();
            switch(type2){
                case "enter":
                    if(!comUtil.isNullChk(roomId, "").equals("") && !comUtil.isNullChk(senderSeq, "").equals("")){
                        RoomRequest rr = new RoomRequest();
                        rr.setSeq(Integer.parseInt(roomId));
                        rr.setOwnerSeq(Integer.parseInt(senderSeq));
                        rr.setUserSeq(Integer.parseInt(senderSeq));
                        roomService.inTheRoom(rr);
                        updateRoomMsg(rr.getSeq());
                    }

                    enterRoom(roomId, session, sender, Integer.parseInt(comUtil.isNullChk(senderSeq, "0")));
                    content = sender + "님이 입장하셨습니다.";
                    break;
                case "chat":
                    break;
                case "leave":
                    //잠시 자리 비움
                    if(!comUtil.isNullChk(roomId, "").equals("") && !comUtil.isNullChk(senderSeq, "").equals("")){
                        RoomRequest rr = new RoomRequest();
                        rr.setSeq(Integer.parseInt(roomId));
                        rr.setOwnerSeq(Integer.parseInt(senderSeq));
                        rr.setUserSeq(Integer.parseInt(senderSeq));
                        roomService.leaveTheRoom(rr);
                    }
                    break;
                case "out":
                    // 방을 나감
                    if(!comUtil.isNullChk(roomId, "").equals("") && !comUtil.isNullChk(senderSeq, "").equals("")){
                        RoomRequest rr = new RoomRequest();
                        rr.setSeq(Integer.parseInt(roomId));
                        rr.setOwnerSeq(Integer.parseInt(senderSeq));
                        rr.setUserSeq(Integer.parseInt(senderSeq));
                        roomService.outTheRoom(rr);
                        updateRoomMsg(rr.getSeq());
                    }

                    outRoom(roomId, session);
                    content = sender + "님이 방을 떠났습니다.";
                    break;
                case "invite":
                    inviteRoom(roomId, receiveSeq);
                    jo.addProperty("type1", type1);
                    jo.addProperty("type2", type2);
                    jo.addProperty("content", content);
                    jo.addProperty("datetime", new Date().getTime());
                    jo.addProperty("roomId", roomId);
                    jo.addProperty("receiveSeq", receiveSeq);
                    jo.addProperty("timer", InviteSec - 1000);
                    roomService.insChatLog(type2, Integer.parseInt(roomId), content, Integer.parseInt(senderSeq));
                    sendMessageToAll(jo.toString());
                    return;
            }
            jo.addProperty("type1", type1);
            jo.addProperty("type2", type2);
            jo.addProperty("content", content);
            jo.addProperty("sender", sender);
            jo.addProperty("senderSeq", senderSeq);
            jo.addProperty("senderPhoto", senderPhoto);
            jo.addProperty("datetime", new Date().getTime());
            roomService.insChatLog(type2, Integer.parseInt(roomId), content, Integer.parseInt(senderSeq));
            for (RoomSession s : meetrooms.get(roomId)) {
                if(s.getSession().isOpen()){
                    s.getSession().sendMessage(new TextMessage(jo.toString()));
                }
            }
        }

    }

    private void enterRoom(String roomId, WebSocketSession session, String sender, int senderSeq) {
        Set<RoomSession> room = meetrooms.getOrDefault(roomId, new HashSet<>());
        RoomSession userSession = new RoomSession(session, sender, senderSeq, roomId);
        room.add(userSession);
        meetrooms.put(roomId, room);

        Set<RoomSession> roomSession = meetrooms.get(roomId);
    }

    private void outRoom(String roomId, WebSocketSession session) {
        Set<RoomSession> room = meetrooms.getOrDefault(roomId, new HashSet<>());
        room.stream()
                .filter(userSession -> userSession.getSession().equals(session))
                .findFirst()
                .ifPresent(room::remove);
        meetrooms.put(roomId, room);
    }

    private void inviteRoom(String roomId, String receiveSeq) {
        redisService.setValues("InviteCode " + receiveSeq + " " + roomId, "true", Duration.ofMillis(InviteSec));
    }

    public void sendMessageToAll(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : CLIENTS.values()) {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
            }
        }
    }

    public void updateUserMsg() throws IOException {
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Gson gson = new Gson();
        UserResponse user = new UserResponse(userService.getUserBySeq());
        JsonObject jo = new JsonObject();
        jo.addProperty("type1", "room");
        jo.addProperty("type2", "updateUserMsg");
        jo.addProperty("seq", seq);
        jo.add("userInfo", gson.toJsonTree(user));
        jo.addProperty("datetime", new Date().getTime());
//        sendMessageToAll(jo.toString());
        sendMessageToUserSeqInRoom(seq, jo);
    }

    public void sendMessageToUserSeqInRoom(Integer userSeq, JsonObject jo){
        Optional<RoomSession> roomSessionOpt = meetrooms.values().stream()
                .flatMap(Set::stream)
                .filter(roomSession -> roomSession.getSenderSeq().equals(userSeq))
                .findFirst();

        roomSessionOpt.ifPresent(roomSession -> {
            Set<RoomSession> roomSessions = meetrooms.get(roomSession.getRoomId());
            if (roomSessions != null) {
                for (RoomSession rs : roomSessions) {
                    if(rs.getSession().isOpen()){
                        try {
                            rs.getSession().sendMessage(new TextMessage(jo.toString()));
                        } catch (IOException e) {
                        }
                    }
                }
            }
        });
    }

    public void sendMessageToRoom(Integer roomId, JsonObject jo) throws IOException {
        System.out.println("roomId : " + roomId);
        System.out.println(meetrooms);
        for (RoomSession s : meetrooms.get(roomId+"")) {
            if(s.getSession().isOpen()){
                s.getSession().sendMessage(new TextMessage(jo.toString()));
            }
        }
    }

    public void createRoomMsg(int roomId) throws IOException {
        Gson gson = new Gson();
        RoomResponse createRoom = new RoomResponse(roomService.getRoomBySeq(roomId));
        JsonObject jo = new JsonObject();
        jo.addProperty("type1", "room");
        jo.addProperty("type2", "createMsg");
        jo.addProperty("seq", roomId);
        jo.add("roomInfo", gson.toJsonTree(createRoom));
        jo.addProperty("datetime", new Date().getTime());
        sendMessageToAll(jo.toString());
    }

    public void updateRoomMsg(int roomId) throws IOException {
        Gson gson = new Gson();
        RoomResponse updatedRoom = new RoomResponse(roomService.getRoomBySeq(roomId));
        JsonObject jo = new JsonObject();
        jo.addProperty("type1", "room");
        jo.addProperty("type2", "updateMsg");
        jo.addProperty("seq", roomId);
        jo.add("roomInfo", gson.toJsonTree(updatedRoom));
        jo.addProperty("datetime", new Date().getTime());
        sendMessageToAll(jo.toString());
    }

}
