package com.wooreal.gravitygather.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Component
public class Task {
    private Integer seq;
    private String title;
    private Integer category_seq;
    private String category_nm;
    private String content;
    private Boolean is_all_day;
    private Boolean is_delete;
    private Boolean is_shared;
    private Boolean is_important;
    private Integer user_seq;
    private String user_nm;
    private String user_nickname;
    private String bg_color;
    private String text_color;

    private Timestamp start_date;
    private Timestamp end_date;
    private Timestamp start_time;
    private Timestamp end_time;

    //UNIX로 관리
    private Integer start_date_time;
    private Integer end_date_time;

    private Timestamp created_at;
    private Timestamp updated_at;
    private Timestamp share_date;
    private String caption;

    private Integer[] shared_user_seq;
}
