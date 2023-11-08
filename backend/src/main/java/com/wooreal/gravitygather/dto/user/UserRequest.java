package com.wooreal.gravitygather.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UserRequest {

    private String id;
    private String password;
    private String passwordSalt;

    private String email;
    private String code;
}
