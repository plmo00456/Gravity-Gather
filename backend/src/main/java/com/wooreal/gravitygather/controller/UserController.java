package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.user.EmailVerificationResult;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.service.MailService;
import com.wooreal.gravitygather.service.RedisService;
import com.wooreal.gravitygather.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@Api(tags = "Test Controller", description = "테스트 API")
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 액션 Api", notes = "아이디, 비밀번호를 이용하여 로그인 합니다.")
    public ResponseEntity<?> loginAction(@RequestBody UserRequest userRequest) {
        User user = userService.login(userRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/emails/verification-requests")
    @ApiOperation(value = "메일 인증 요청")
    public ResponseEntity<?> sendMessage(@RequestParam("email") @Valid @Email String email) {
        userService.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/emails/verifications")
    @ApiOperation(value = "메일 인증 검증")
    public ResponseEntity<?> verificationEmail(@RequestParam("email") @Valid @Email String email,
                                            @RequestParam("code") String authCode) {
        EmailVerificationResult response = userService.verifiedCode(email, authCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
