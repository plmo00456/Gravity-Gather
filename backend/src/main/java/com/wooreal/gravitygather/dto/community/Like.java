package com.wooreal.gravitygather.dto.community;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Like {

    private int content_seq;
    private String mode;
    private int user_seq;
    private boolean is_up;
    private Timestamp created_at;

    public boolean getIs_up() {
        return this.is_up;
    }

}
