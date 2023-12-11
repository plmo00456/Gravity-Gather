package com.wooreal.gravitygather.dto.team;

import com.google.gson.JsonElement;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Todo {

    private int seq;
    private int user_seq;
    private Integer receive_seq;
    private String user_nickname;
    private String receive_nickname;
    private int room_seq;
    private String title;
    private String content;
    private Boolean is_complete;
    private Boolean is_delete;
    private Timestamp created_at;
    private Timestamp updated_at;

}
