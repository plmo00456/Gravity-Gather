package com.wooreal.gravitygather.dto.user;

import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@ToString
@NoArgsConstructor
public class User implements UserDetails {

    private int seq;
    private String id;
    private String password;
    private String password_salt;
    private String photo;
    private String name;
    private String nickname;
    private String email;
    private int room_character;
    private int room_map;
    private String status;
    private int fail_login_cnt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User (UserRequest userRequest){
        this.seq = userRequest.getSeq();
        this.id = userRequest.getId();
        this.photo = userRequest.getPhoto();
        this.password = userRequest.getPassword();
        this.password_salt = userRequest.getPasswordSalt();
        this.name = userRequest.getName();
        this.nickname = userRequest.getNickname();
        this.room_character = userRequest.getRoomCharacter();
        this.room_map= userRequest.getRoomMap();
    }
}
