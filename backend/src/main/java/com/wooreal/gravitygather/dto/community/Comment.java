package com.wooreal.gravitygather.dto.community;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Component
public class Comment {

    @Value("${file.upload.dir}")
    @JsonIgnore
    private String fileUploadDir;

    @Value("${file.mapping.dir}")
    @JsonIgnore
    private String fileMappingDir;

    private static String staticFileUploadDir;
    private static String staticFileMappingDir;

    @PostConstruct
    public void init() {
        staticFileUploadDir = fileUploadDir;
        staticFileMappingDir = fileMappingDir;
    }

    private Integer seq;
    private Integer article_seq;
    private Integer parent_comment_seq;
    private String content;
    private String user_seq;
    private int likes;
    private Boolean is_up;
    private Boolean is_delete;
    private Timestamp created_at;
    private Timestamp updated_at;

    private String nickname;
    private String photo;

    public String getPhoto(){
        if(this.photo != null && staticFileUploadDir != null && staticFileMappingDir != null) {
            String normalizedPhoto = this.photo.replace("\\", "/");
            return normalizedPhoto.replace(staticFileUploadDir, staticFileMappingDir);
        } else
            return null;
    }

    public Boolean getIs_up() {
        if(this.is_up == null) return null;
        else return this.is_up;
    }
}
