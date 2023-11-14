package com.wooreal.gravitygather.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserRequest {

    private Integer seq;
    private String id;
    private String name;
    private String nickname;
    private String password;
    private String passwordSalt;
    private String newPassword;
    private Integer roomCharacter;
    private Integer roomMap;
    private String photo;

    private String email;
    private String code;
}
