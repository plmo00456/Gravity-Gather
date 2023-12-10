package com.wooreal.gravitygather.dto.user;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class FailedLoginAttempt {

    int id;
    int userId;
    String status;
    Timestamp timestamp;
}
