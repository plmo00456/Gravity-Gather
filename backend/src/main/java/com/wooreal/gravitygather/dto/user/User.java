package com.wooreal.gravitygather.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
public class User {

    private int seq;
    private String id;
    private String password;
    private String password_salt;
    private String photo;
    private String name;
    private String nickname;
    private String email;
    private int room_character;
    private int room_map;
    private String status;
    private int failLoginCnt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
