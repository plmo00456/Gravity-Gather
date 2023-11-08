package com.wooreal.gravitygather.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wooreal.gravitygather.dto.room.Room;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.dto.room.RoomSession;
import com.wooreal.gravitygather.service.RoomService;
import com.wooreal.gravitygather.utils.comUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final RoomService roomService;

    public WebSocketHandler(@Lazy RoomService roomService) {
        this.roomService = roomService;
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
        String type1 = Optional.ofNullable(jsonObject.get("type1")).map(com.google.gson.JsonElement::getAsString).orElse(null);
        String type2 = Optional.ofNullable(jsonObject.get("type2")).map(com.google.gson.JsonElement::getAsString).orElse(null);
        String roomId = Optional.ofNullable(jsonObject.get("roomId")).map(com.google.gson.JsonElement::getAsString).orElse(null);
        String sender = Optional.ofNullable(jsonObject.get("sender")).map(com.google.gson.JsonElement::getAsString).orElse(null);
        String senderSeq = Optional.ofNullable(jsonObject.get("senderSeq")).map(com.google.gson.JsonElement::getAsString).orElse(null);
        String content = Optional.ofNullable(jsonObject.get("content")).map(com.google.gson.JsonElement::getAsString).orElse(null);

        System.out.println(type1 + " " + type2);
        if (type1.equals("room")) {
            JsonObject jo = new JsonObject();
            switch(type2){
                case "enter":
                    if(!comUtil.isNullChk(roomId, "").equals("") && !comUtil.isNullChk(senderSeq, "").equals("")){
                        RoomRequest rr = new RoomRequest();
                        rr.setSeq(Integer.parseInt(roomId));
                        rr.setOwnerSeq(Integer.parseInt(senderSeq));
                        roomService.enterRoom(rr);
                        updateRoomMsg(rr.getSeq());
                    }

                    enterRoom(roomId, session, sender, Integer.parseInt(comUtil.isNullChk(senderSeq, "0")));
                    content = sender + "님이 입장하셨습니다.";
                    break;
                case "chat":
                    break;
                case "leave":
                    if(!comUtil.isNullChk(roomId, "").equals("") && !comUtil.isNullChk(senderSeq, "").equals("")){
                        RoomRequest rr = new RoomRequest();
                        rr.setSeq(Integer.parseInt(roomId));
                        rr.setOwnerSeq(Integer.parseInt(senderSeq));
                        roomService.leaveRoom(rr);
                        updateRoomMsg(rr.getSeq());
                    }

                    leaveRoom(roomId, session);
                    content = sender + "님이 방을 떠났습니다.";
                    break;
            }
            jo.addProperty("type1", type1);
            jo.addProperty("type2", type2);
            jo.addProperty("content", content);
            jo.addProperty("sender", sender);
            jo.addProperty("senderSeq", senderSeq);
            jo.addProperty("datetime", new Date().getTime());

            for (RoomSession s : meetrooms.get(roomId)) {
                if(s.getSession().isOpen()){
                    System.out.println(s.getSender() + " " + s.getSession());
                    System.out.println(jo.toString());
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
    }

    private void leaveRoom(String roomId, WebSocketSession session) {
        Set<RoomSession> room = meetrooms.getOrDefault(roomId, new HashSet<>());
        room.stream()
                .filter(userSession -> userSession.getSession().equals(session))
                .findFirst()
                .ifPresent(room::remove);
        meetrooms.put(roomId, room);
    }

    public void sendMessageToAll(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : CLIENTS.values()) {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
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
