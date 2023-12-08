package com.wooreal.gravitygather.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class UserResponse {

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

    private int seq;
    private String id;
    private String photo;
    private String name;
    private String nickname;
    private String email;
    private String status;
    private int roomCharacter;
    private int roomMap;

    public String getPhoto(){
        if(this.photo != null && staticFileUploadDir != null && staticFileMappingDir != null) {
            String normalizedPhoto = this.photo.replace("\\", "/");
            return normalizedPhoto.replace(staticFileUploadDir, staticFileMappingDir);
        } else
            return null;
    }

    public String replaceFileDir(String filePath){
        if(filePath != null && staticFileUploadDir != null && staticFileMappingDir != null) {
            String normalizedPhoto = filePath.replace("\\", "/");
            return normalizedPhoto.replace(staticFileUploadDir, staticFileMappingDir);
        } else
            return null;
    }

    public UserResponse(User user){
        if (user != null) {
            this.seq = user.getSeq() != 0 ? user.getSeq() : this.seq;
            this.id = user.getId() != null ? user.getId() : this.id;
            this.photo = user.getPhoto() != null ? replaceFileDir(user.getPhoto()) : this.photo;
            this.name = user.getName() != null ? user.getName() : this.name;
            this.nickname = user.getNickname() != null ? user.getNickname() : this.nickname;
            this.email = user.getEmail() != null ? user.getEmail() : this.email;
            this.status = user.getStatus() != null ? user.getStatus() : this.status;
            this.roomCharacter = user.getRoom_character() != 0 ? user.getRoom_character() : this.roomCharacter;
            this.roomMap = user.getRoom_map() != 0 ? user.getRoom_map() : this.roomMap;
        }
    }

}
