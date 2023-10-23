package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (user == null) {
            return new ResponseEntity<>("아이디와 비밀번호를 확인해 주세요.", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
