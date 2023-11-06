package com.wooreal.gravitygather.dto.room;

import com.wooreal.gravitygather.dto.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
public class RoomRequest {

    private int seq;
    private String title;
    private String topic;
    private String password;
    private Boolean isLocked;
    private int maxParticipant;
    private int ownerSeq;

}
