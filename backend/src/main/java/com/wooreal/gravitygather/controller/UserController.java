package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.LoginRequired;
import com.wooreal.gravitygather.dto.file.FileVO;
import com.wooreal.gravitygather.dto.user.EmailVerificationResult;
import com.wooreal.gravitygather.dto.user.Friend;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.service.FileUploadService;
import com.wooreal.gravitygather.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@OpenAPIDefinition(info = @Info(title = "User Controller", version = "v1", description = "사용자 컴트롤러"))
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
    @Operation(summary = "로그인 액션 Api", description = "아이디, 비밀번호를 이용하여 로그인 합니다.")
    public ResponseEntity<?> loginAction(@Parameter(description = "로그인 정보가 담긴 Request Body", in = ParameterIn.PATH) @RequestBody UserRequest userRequest, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        User user = userService.login(userRequest, request, httpServletResponse);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @PostMapping("/emails/verification-requests")
    @Operation(summary = "메일 인증 요청")
    public ResponseEntity<?> sendMessage(@RequestBody UserRequest userRequest) {
        userService.sendCodeToEmail(userRequest.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/emails/verifications")
    @Operation(summary = "메일 인증 검증")
    public ResponseEntity<?> verificationEmail(@RequestBody UserRequest userRequest) {
        EmailVerificationResult response = userService.verifiedCode(userRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/info")
    @Operation(summary = "유저 정보")
    public ResponseEntity<?> getUserInfo() {
        UserResponse userInfo = new UserResponse(userService.getUserBySeq());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/{seq}/public-info")
    @Operation(summary = "공개 가능한 유저 정보")
    public ResponseEntity<?> getPublicUserInfo(@PathVariable("seq") int seq) {
        UserResponse userInfo = userService.getPublicUserInfo(seq);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/update")
    @Operation(summary = "유저 정보 변경 api")
    public ResponseEntity<?> userUpdate(UserRequest userRequest,
        @RequestParam(name = "profileImage", required = false) MultipartFile imageFile,
        @RequestParam(name = "isRemoveImage", required = false, defaultValue = "false") Boolean isRemoveImage
    ) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            FileVO file = fileUploadService.singleFileUpload(imageFile, "img/");
            userRequest.setPhoto(file.getUpload_path());
        }
        if (isRemoveImage) {
            userRequest.setPhoto("");
        }

        userService.userUpdate(userRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/friend/get")
    @Operation(summary = "친구 가져오는 api")
    public ResponseEntity<?> getFriends(@RequestBody(required = false) Friend friend) {
        List<Friend> friends = userService.getFriends(friend);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/friend/add")
    @Operation(summary = "친구 삭제하는 api")
    public ResponseEntity<?> addFriends(@RequestBody Friend friend) {
        userService.addFriend(friend);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/friend/delete")
    @Operation(summary = "친구 삭제하는 api")
    public ResponseEntity<?> deleteFriends(@RequestBody Friend friend) {
        userService.deleteFriend(friend);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refreshAccessToken")
    @Operation(summary = "액세스 토큰 재발급 api")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        userService.refreshAccessToken(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login/checkEmailDuplication")
    @Operation(summary = "이메일 중복 확인 api")
    public ResponseEntity<?> checkEmailDuplication(@RequestBody User user) {
        userService.checkEmailDuplication(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login/checkIdDuplication")
    @Operation(summary = "아이디 중복 확인 api")
    public ResponseEntity<?> checkIdDuplication(@RequestBody User user) {
        userService.checkIdDuplication(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login/register")
    @Operation(summary = "회원가입 api")
    public ResponseEntity<?> register(@RequestBody UserRequest user) {
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
