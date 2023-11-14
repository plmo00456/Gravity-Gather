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
    private String password;
    private String passwordSalt;
    private String newPassword;

    private String email;
    private String code;
}
