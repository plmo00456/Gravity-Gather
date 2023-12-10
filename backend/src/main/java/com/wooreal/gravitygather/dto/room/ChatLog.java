package com.wooreal.gravitygather.dto.room;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatLog {

    private Integer seq;
    private String type;
    private Integer room_seq;
    private String msg;
    private Integer sender_seq;
    private Timestamp created_at;

    public ChatLog (String type, Integer room_seq, String msg, Integer sender_seq){
        this.type = type;
        this.room_seq = room_seq;
        this.msg = msg;
        this.sender_seq = sender_seq;
    }
}
