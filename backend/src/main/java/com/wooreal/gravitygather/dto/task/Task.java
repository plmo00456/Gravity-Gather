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
    private String category_code;
    private String category_nm;
    private String content;
    private Boolean is_all_day;
    private Boolean is_delete;
    private Boolean is_shared;
    private Integer user_seq;
    private String user_nm;

    //UNIX로 관리
    private Integer start_date;
    private Integer end_date;
    private Integer start_time;
    private Integer end_time;

    private Timestamp created_at;
    private Timestamp updated_at;
    private Timestamp share_date;
    private String caption;

    private Integer[] shared_user_seq;
}
