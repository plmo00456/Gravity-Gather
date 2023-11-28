package com.wooreal.gravitygather.dto.room;

import com.wooreal.gravitygather.dto.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RoomRequest {

    private Integer seq;
    private String title;
    private String topic;
    private String password;
    private String passwordSalt;
    private Boolean isLocked;
    private Integer currentParticipant;
    private Integer maxParticipant;
    private Integer ownerSeq;
    private Integer userSeq;

}
