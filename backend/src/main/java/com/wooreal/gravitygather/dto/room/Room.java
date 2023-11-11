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

    private Integer seq;
    private String title;
    private String topic;
    private Boolean is_locked;
    private Boolean is_delete;
    private String password;
    private String password_salt;
    private Integer max_participant;
    private Integer current_participant;
    private Integer owner_seq;
    private Boolean is_full;
    private Timestamp created_at;
    private User user;
}
