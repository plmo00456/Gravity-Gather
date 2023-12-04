package com.wooreal.gravitygather.dto.community;

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
public class Article {
    private final int viewPageCnt = 15;

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
    private Integer master_seq;
    private String title;
    private String content;
    private String user_seq;
    private int is_delete;
    private int view_count;
    private int comment_count;
    private int likes;
    private Timestamp created_at;
    private Timestamp updated_at;

    private String nickname;
    private String photo;

    private String order;
    private String search;

    private int currentPage;
    private int lastPage;
    private int offset;
    private int allCnt;

    public String getPhoto(){
        if(this.photo != null && staticFileUploadDir != null && staticFileMappingDir != null) {
            String normalizedPhoto = this.photo.replace("\\", "/");
            return normalizedPhoto.replace(staticFileUploadDir, staticFileMappingDir);
        } else
            return null;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        this.offset = (currentPage - 1) * viewPageCnt;
    }
}
