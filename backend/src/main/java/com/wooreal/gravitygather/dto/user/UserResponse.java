package com.wooreal.gravitygather.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserResponse {
    private int seq;
    private String id;
    private String photo;
    private String name;
    private String nickname;
    private String email;
    private String status;

    public UserResponse(User user){
        if (user != null) {
            this.seq = user.getSeq() != 0 ? user.getSeq() : this.seq;
            this.id = user.getId() != null ? user.getId() : this.id;
            this.photo = user.getPhoto() != null ? user.getPhoto() : this.photo;
            this.name = user.getName() != null ? user.getName() : this.name;
            this.nickname = user.getNickname() != null ? user.getNickname() : this.nickname;
            this.email = user.getEmail() != null ? user.getEmail() : this.email;
            this.status = user.getStatus() != null ? user.getStatus() : this.status;
        }
    }

}
