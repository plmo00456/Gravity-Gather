package com.wooreal.gravitygather.dto.room;

import com.wooreal.gravitygather.dto.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
public class Room {

    private int seq;
    private String title;
    private String topic;
    private Boolean is_locked;
    private Boolean is_delete;
    private String password;
    private int max_participant;
    private int current_participant;
    private int owner_seq;
    private Boolean is_full;
    private Timestamp created_at;
    private User user;

}
