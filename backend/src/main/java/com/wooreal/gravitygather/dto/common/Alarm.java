package com.wooreal.gravitygather.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Component
public class Alarm {
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

    Integer seq;
    Integer receive_seq;
    Integer sender_seq;
    String msg;
    Boolean is_check;
    String dte_cde_val;
    String dte_cde_nm;
    Integer cont_seq;
    Timestamp created_at;

    String sender_nickname;
    String sender_photo;

    public String getSender_photo(){
        if(this.sender_photo != null && staticFileUploadDir != null && staticFileMappingDir != null) {
            String normalizedPhoto = this.sender_photo.replace("\\", "/");
            return normalizedPhoto.replace(staticFileUploadDir, staticFileMappingDir);
        } else
            return null;
    }
}
