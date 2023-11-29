package com.wooreal.gravitygather.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
public class Friend {

    private Integer seq;
    private Integer user_seq;
    private String user_nm;
    private Integer friend_seq;
    private Timestamp created_at;
}
