package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.file.FileVO;
import com.wooreal.gravitygather.dto.user.EmailVerificationResult;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.service.FileUploadService;
import com.wooreal.gravitygather.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "User Controller", description = "사용자 관련 API")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    private final FileUploadService fileUploadService;

    public UserController(UserService userService, FileUploadService fileUploadService) {
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 액션 Api", notes = "아이디, 비밀번호를 이용하여 로그인 합니다.")
    public ResponseEntity<?> loginAction(@RequestBody UserRequest userRequest) {
        User user = userService.login(userRequest);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @PostMapping("/emails/verification-requests")
    @ApiOperation(value = "메일 인증 요청")
    public ResponseEntity<?> sendMessage(@RequestBody UserRequest userRequest) {
        userService.sendCodeToEmail(userRequest.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/emails/verifications")
    @ApiOperation(value = "메일 인증 검증")
    public ResponseEntity<?> verificationEmail(@RequestBody UserRequest userRequest) {
        EmailVerificationResult response = userService.verifiedCode(userRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //인증
    @GetMapping("/{seq}/info")
    @ApiOperation(value = "공개 가능한 유저 정보")
    public ResponseEntity<?> getUserInfo(@PathVariable("seq") int seq) {
        UserResponse userInfo = new UserResponse(userService.getUserBySeq(seq));
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/{seq}/public-info")
    @ApiOperation(value = "공개 가능한 유저 정보")
    public ResponseEntity<?> getPublicUserInfo(@PathVariable("seq") int seq) {
        UserResponse userInfo = userService.getPublicUserInfo(seq);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    //인증
    @PostMapping("/update")
    @ApiOperation(value = "유저 정보 변경 api")
    public ResponseEntity<?> userUpdate(UserRequest userRequest,
                                        @RequestParam(name = "profileImage", required=false) MultipartFile imageFile) throws IOException {
        if(imageFile != null && !imageFile.isEmpty()){
            FileVO file = fileUploadService.singleFileUpload(imageFile, "img/");
            userRequest.setPhoto(file.getUpload_path());
        }

        userService.userUpdate(userRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
