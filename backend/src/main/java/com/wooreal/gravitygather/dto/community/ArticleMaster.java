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
public class ArticleMaster {

    private Integer seq;
    private String title;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;
}
