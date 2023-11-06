package com.wooreal.gravitygather.dto.room;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
public class RoomSession {
    private WebSocketSession session;
    private String sender;

    public RoomSession(WebSocketSession session, String sender) {
        this.session = session;
        this.sender = sender;
    }
}