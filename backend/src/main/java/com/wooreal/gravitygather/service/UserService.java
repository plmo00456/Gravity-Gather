package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.user.EmailVerificationResult;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.exception.ExceptionCode;
import com.wooreal.gravitygather.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Service
public class UserService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final UserMapper userMapper;
    private final MailService mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    public UserService(UserMapper userMapper, MailService mailService, RedisService redisService) {
        this.userMapper = userMapper;
        this.mailService = mailService;
        this.redisService = redisService;
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
            System.out.println("UserService.createCode() exception occur");
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

}
