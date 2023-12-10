package com.wooreal.gravitygather.dto.room;

import com.wooreal.gravitygather.dto.user.UserResponse;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class RoomResponse {

    private Integer seq;
    private String title;
    private String topic;
    private Boolean isLocked;
    private Boolean isDelete;
    private Integer maxParticipant;
    private Integer currentParticipant;
    private Integer ownerSeq;
    private Boolean isFull;
    private Timestamp createdAt;
    private UserResponse user;

    public RoomResponse(Room room){
        if(room != null){
            this.seq = room.getSeq();
            this.title = room.getTitle();
            this.topic = room.getTopic();
            this.isLocked = room.getIs_locked();
            this.isDelete = room.getIs_delete();
            this.maxParticipant = room.getMax_participant();
            this.currentParticipant = room.getCurrent_participant();
            this.ownerSeq = room.getOwner_seq();
            this.isFull = room.getIs_full();
            this.createdAt = room.getCreated_at();
            this.user = new UserResponse(room.getUser());
        }
    }
}
