package com.wooreal.gravitygather.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wooreal.gravitygather.dto.room.RoomSession;
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

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();
    private Map<String, Set<RoomSession>> meetrooms = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENTS.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CLIENTS.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

        String type1 = jsonObject.get("type1").getAsString();
        String type2 = jsonObject.get("type2").getAsString();
        String roomId = jsonObject.get("roomId").getAsString();
        String sender = jsonObject.get("sender").getAsString();
        String senderSeq = jsonObject.get("senderSeq").getAsString();
        String content = jsonObject.get("content").getAsString();

        if (type1.equals("room")) {
            JsonObject jo = new JsonObject();
            switch(type2){
                case "enter":
                    enterRoom(roomId, session, sender);
                    content = sender + "님이 입장하셨습니다.";
                    break;
                case "chat":
                    break;
                case "leave":
                    leaveRoom(roomId, session);
                    content = sender + "님이 방을 떠났습니다.";
                    break;
            }
            jo.addProperty("content", content);
            jo.addProperty("sender", sender);
            jo.addProperty("senderSeq", senderSeq);
            jo.addProperty("datetime", new Date().toString());

            for (RoomSession s : meetrooms.get(roomId)) {
                s.getSession().sendMessage(new TextMessage(jo.toString()));
            }
        }


//        String id = session.getId();  //메시지를 보낸 아이디
//        CLIENTS.entrySet().forEach( arg->{
//            if(!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
//                try {
//                    arg.getValue().sendMessage(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void enterRoom(String roomId, WebSocketSession session, String sender) {
        Set<RoomSession> room = meetrooms.getOrDefault(roomId, new HashSet<>());
        RoomSession userSession = new RoomSession(session, sender);
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
            System.out.println("===============================");
            System.out.println(session);
            System.out.println(session.isOpen());
            System.out.println("===============================");
            if (session.isOpen()) {
                session.sendMessage(textMessage);
            }
        }
    }
}
