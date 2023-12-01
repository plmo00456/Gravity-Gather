package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.file.FileVO;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.service.CommonService;
import com.wooreal.gravitygather.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "Common Controller", description = "공동 컨트롤러")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

    private final CommonService commonService;
    private final FileUploadService fileUploadService;

    public CommonController(CommonService commonService, FileUploadService fileUploadService) {
        this.commonService = commonService;
        this.fileUploadService = fileUploadService;
    }

    // 인증
    @PostMapping("/alarm/get/{userId}")
    @ApiOperation(value = "알람 가져오는 api")
    public ResponseEntity<?> getAlarm(@PathVariable("userId") int userId) {
        List<Alarm> alarms = commonService.getAlarm(userId);
        return new ResponseEntity<>(alarms, HttpStatus.OK);
    }

    // 인증
    @PostMapping("/alarm/read/{userId}")
    @ApiOperation(value = "알람 읽음처리 api")
    public ResponseEntity<?> readAlarm(@PathVariable("userId") int userId
        , @RequestBody(required = false) Alarm alarm) {
        commonService.readAlarm(userId, alarm == null ? 0 : alarm.getSeq());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/image/upload")
    @ApiOperation(value = "이미지 업로드 및 url 호출 api")
    public ResponseEntity<?> imageUpload(
        @RequestParam(name = "imageFile") MultipartFile imageFile)
        throws IOException {
        System.out.println(imageFile);
        System.out.println(imageFile == null);
        FileVO file = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            file = fileUploadService.singleFileUpload(imageFile, "img/");
        }
        System.out.println(file.getImage_path());
        return new ResponseEntity<>(file.getImage_path(), HttpStatus.OK);
    }

}
