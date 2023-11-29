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
public class Comment {
    private Integer seq;
    private Integer article_seq;
    private Integer parent_comment_seq;
    private String content;
    private String user_seq;
    private int likes;
    private Boolean is_delete;
    private Timestamp created_at;
    private Timestamp updated_at;
}
