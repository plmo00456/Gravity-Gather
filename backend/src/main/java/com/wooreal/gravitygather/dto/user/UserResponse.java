package com.wooreal.gravitygather.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
public class UserResponse {
    private int seq;
    private String id;
    private String name;
    private String email;
    private String status;

    public UserResponse(User user){
        this.seq = user.getSeq();
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.status = user.getStatus();
    }
}
