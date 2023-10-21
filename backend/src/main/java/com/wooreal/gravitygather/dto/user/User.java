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
    private String name;
    private String email;
    private String status;
    private int failLoginCnt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
