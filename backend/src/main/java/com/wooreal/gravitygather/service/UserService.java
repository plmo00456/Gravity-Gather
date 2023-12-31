package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.user.EmailVerificationResult;
import com.wooreal.gravitygather.dto.user.Friend;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.UserMapper;
import com.wooreal.gravitygather.utils.JwtTokenUtil;
import com.wooreal.gravitygather.utils.SHA256Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final UserMapper userMapper;
    private final MailService mailService;
    private final RedisService redisService;

    private final WebSocketHandler webSocketHandler;

    private final JwtTokenUtil jwtTokenUtil;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    public UserService(UserMapper userMapper, MailService mailService, RedisService redisService, WebSocketHandler webSocketHandler, JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.mailService = mailService;
        this.redisService = redisService;
        this.webSocketHandler = webSocketHandler;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public User login(UserRequest userRequest, HttpServletRequest request, HttpServletResponse httpServletResponse){
        User tmp = userMapper.getUserById(userRequest.getId());
        if(tmp == null)
            throw new BusinessLogicException(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호를 확인해 주세요.");

        String salt = tmp.getPassword_salt();
        String password = SHA256Util.generateHashWithSalt(userRequest.getPassword(), salt);
        userRequest.setPassword(password);
        User user = userMapper.login(userRequest);

        if (user == null || user.getStatus().equals("DELETED"))
            throw new BusinessLogicException(HttpStatus.UNAUTHORIZED, "아이디와 비밀번호를 확인해 주세요.");

        if (user.getStatus().equals("LOCK"))
            throw new BusinessLogicException(HttpStatus.UNAUTHORIZED, "정지된 계정입니다. 관리자에게 문의해 주세요.");

        httpServletResponse.setHeader("authorization", jwtTokenUtil.generateToken(user.getSeq(), 0));
        httpServletResponse.setHeader("refresh", jwtTokenUtil.generateToken(user.getSeq(), 1));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            user.getSeq(), null, new ArrayList<>());
        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

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

    public User getUserBySeq(){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.getUserBySeq(seq);
    }

    public User getUserBySeq(int seq){
        return userMapper.getUserBySeq(seq);
    }

    public UserResponse getPublicUserInfo(int seq) {
        User ur = getUserBySeq(seq);
        if(ur == null || ur.getStatus().equals("DELETED")){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "존재하지 않는 사용자입니다.");
        }
        UserResponse result = new UserResponse();

        result.setSeq(ur.getSeq());
        result.setPhoto(ur.getPhoto());
        result.setNickname(ur.getNickname());
        result.setRoomCharacter(ur.getRoom_character());
        result.setRoomMap(ur.getRoom_map());

        return result;
    }

    public void userUpdate(UserRequest userRequest) throws IOException {
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRequest.setSeq(userSeq);

        User ur = getUserBySeq();

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

        webSocketHandler.updateUserMsg();
    }

    public List<Friend> getFriends(Friend friend){
        if(friend == null) friend = new Friend();
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friend.setUser_seq(seq);
        return userMapper.getFriends(friend);
    }

    public void addFriend(Friend friend){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friend.setUser_seq(seq);
        if(userMapper.addFriend(friend) == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "친구 추가 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
    }

    public void deleteFriend(Friend friend){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friend.setUser_seq(seq);
        if(userMapper.deleteFriend(friend) == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "친구 삭제 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
    }

    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        String refresh = request.getHeader("refresh");
        if(jwtTokenUtil.validateToken(refresh)){
            int seq = jwtTokenUtil.getUserSeqFromToken(refresh);
            response.setHeader("authorization", jwtTokenUtil.generateToken(seq, 0));
            response.setHeader("refresh", jwtTokenUtil.generateToken(seq, 1));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                seq, null, new ArrayList<>());
            usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }else{
            throw new BusinessLogicException(HttpStatus.valueOf(401), "로그인이 만료되었습니다.");
        }
    }

    public void checkEmailDuplication(User user){
        String email = user.getEmail();
        user = userMapper.getUserByEmail(email);
        if (user != null) {
            throw new BusinessLogicException(HttpStatus.OK,"true" ,"중복된 이메일 입니다.");
        }else{
            throw new BusinessLogicException(HttpStatus.OK,"false" , "");
        }
    }

    public boolean checkIdDuplication(User user){
        String id = user.getId();
        user = userMapper.getUserById(id);
        if (user != null) {
            throw new BusinessLogicException(HttpStatus.OK,"true" ,"중복된 아이디 입니다.");
        }else{
            throw new BusinessLogicException(HttpStatus.OK,"false" , "");
        }
    }

    public void register(UserRequest user){
        if(user.getPassword() != null && !user.getPassword().equals("")){
            String salt = SHA256Util.generateSalt();

            user.setPassword(SHA256Util.generateHashWithSalt(user.getPassword(), salt));
            user.setPasswordSalt(salt);
        }

        if(userMapper.register(user) == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "계정 생성 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
    }
}
