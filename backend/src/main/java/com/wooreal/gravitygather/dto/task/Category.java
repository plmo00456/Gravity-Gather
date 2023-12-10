package com.wooreal.gravitygather.dto.task;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Component
public class Category {
    private Integer seq;
    private String category_nm;
    private Integer user_seq;
    private Boolean is_delete;
    private int task_order;
    private Timestamp created_at;
    private Timestamp updated_at;

    private Integer[] seqs;
}
