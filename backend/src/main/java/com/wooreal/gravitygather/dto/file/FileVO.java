package com.wooreal.gravitygather.dto.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileVO {
    private Integer seq;
    private String original_name;
    private String new_name;
    private String upload_path;
    private String extension;
    private Timestamp create_at;
}
