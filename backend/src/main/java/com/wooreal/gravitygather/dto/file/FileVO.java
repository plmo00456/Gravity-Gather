package com.wooreal.gravitygather.dto.file;

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
public class FileVO {

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
    private String original_name;
    private String new_name;
    private String upload_path;
    private String extension;
    private Timestamp created_at;
    private String image_path;

    public String getImage_path() {
        if(this.upload_path != null && staticFileUploadDir != null && staticFileMappingDir != null) {
            String normalizedPhoto = this.upload_path.replace("\\", "/");
            return normalizedPhoto.replace(staticFileUploadDir, staticFileMappingDir);
        } else
            return null;
    }
}
