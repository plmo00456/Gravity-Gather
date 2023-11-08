package com.wooreal.gravitygather.dto.room;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
public class RoomSession {
    private WebSocketSession session;
    private String sender;
    private Integer senderSeq;
    private String roomId;

    public RoomSession(WebSocketSession session, String sender,Integer senderSeq, String roomId) {
        this.session = session;
        this.sender = sender;
        this.senderSeq = senderSeq;
        this.roomId = roomId;
    }
}