package com.wooreal.gravitygather.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

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
