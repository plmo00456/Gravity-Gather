package com.wooreal.gravitygather.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
public class FailedLoginAttempt {

    int id;
    int userId;
    String status;
    Timestamp timestamp;
}
