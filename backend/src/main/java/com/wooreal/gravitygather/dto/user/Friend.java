package com.wooreal.gravitygather.dto.user;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Friend {

    private Integer seq;
    private Integer user_seq;
    private String user_nm;
    private Integer friend_seq;
    private Timestamp created_at;
}
