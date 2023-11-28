package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.user.*;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.exception.ExceptionCode;
import com.wooreal.gravitygather.mapper.UserMapper;
import com.wooreal.gravitygather.utils.SHA256Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final UserMapper userMapper;
    private final MailService mailService;
    private final RedisService redisService;

    private final WebSocketHandler webSocketHandler;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    public UserService(UserMapper userMapper, MailService mailService, RedisService redisService, WebSocketHandler webSocketHandler) {
        this.userMapper = userMapper;
        this.mailService = mailService;
        this.redisService = redisService;
        this.webSocketHandler = webSocketHandler;
    }

    public User login(UserRequest userRequest){
        User user = userMapper.login(userRequest);
        if (user == null || user.getStatus().equals("DELETED"))
            throw new BusinessLogicException(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호를 확인해 주세요.");

        if (user.getStatus().equals("LOCK"))
            throw new BusinessLogicException(HttpStatus.UNAUTHORIZED, "정지된 계정입니다. 관리자에게 문의해 주세요.");
        return userMapper.login(userRequest);
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "끌림 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String email) {
        User user = userMapper.getUserByEmail(email);
        if (user != null && !user.getStatus().equals("UNVERIFIED")) {
            System.out.println("userMapper.checkDuplicatedEmail exception occur email: " + email);
            throw new BusinessLogicException(HttpStatus.UNAUTHORIZED, "중복된 이메일 입니다.");
        }
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessLogicException(HttpStatus.valueOf(500));
        }
    }

    public EmailVerificationResult verifiedCode(UserRequest userRequest) {
        String email = userRequest.getEmail();
        String code = userRequest.getCode();
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(code);

        if(authResult)
            setUserActive(userRequest);
        return EmailVerificationResult.of(authResult);
    }

    public int setUserActive(UserRequest userRequest) {
        String email = userRequest.getEmail();
        return userMapper.setUserActive(email);
    }

    public User getUserBySeq(int seq){
        return userMapper.getUserBySeq(seq);
    }

    public UserResponse getPublicUserInfo(int seq) {
        User ur = getUserBySeq(seq);
        UserResponse result = new UserResponse();

        result.setSeq(ur.getSeq());
        result.setPhoto(ur.getPhoto());
        result.setNickname(ur.getNickname());
        result.setRoomCharacter(ur.getRoom_character());
        result.setRoomMap(ur.getRoom_map());

        return result;
    }

    public void userUpdate(UserRequest userRequest) throws IOException {
        User ur = getUserBySeq(userRequest.getSeq());

        if(userRequest.getPassword() != null && !userRequest.getPassword().equals("")){
            String oriPassword = ur.getPassword();
            String oriSalt = ur.getPassword_salt();

            String password = userRequest.getPassword();
            String valiPassword = SHA256Util.generateHashWithSalt(password, oriSalt);

            if(!oriPassword.equals(valiPassword)){
                throw new BusinessLogicException(HttpStatus.valueOf(500), "현재 비밀번호가 틀렸습니다.");
            }

            String newPasswordSalt = SHA256Util.generateSalt();
            String newPassword = SHA256Util.generateHashWithSalt(userRequest.getNewPassword(), newPasswordSalt);

            userRequest.setPassword(newPassword);
            userRequest.setPasswordSalt(newPasswordSalt);
        }

        int result = userMapper.userUpdate(new User(userRequest));
        if(result == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "내 정보 변경 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }

        webSocketHandler.updateUserMsg(userRequest.getSeq());
    }

    public List<Friend> getFriends(Friend friend){
        return userMapper.getFriends(friend);
    }

    public int addFriend(Friend friend){
        return userMapper.addFriend(friend);
    }

    public int deleteFriend(Friend friend){
        return userMapper.deleteFriend(friend);
    }

}
