package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.LoginRequired;
import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.file.FileVO;
import com.wooreal.gravitygather.service.CommonService;
import com.wooreal.gravitygather.service.FileUploadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@OpenAPIDefinition(info = @Info(title = "Common Controller", version = "v1", description = "공동 컴트롤러"))
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

    @LoginRequired
    @PostMapping("/alarm/get")
    @Operation(summary = "알람 가져오는 api")
    public ResponseEntity<?> getAlarm() {
        List<Alarm> alarms = commonService.getAlarm();
        return new ResponseEntity<>(alarms, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/alarm/read")
    @Operation(summary = "알람 읽음처리 api")
    public ResponseEntity<?> readAlarm() {
        commonService.readAlarm();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/image/upload")
    @Operation(summary = "이미지 업로드 및 url 호출 api")
    public ResponseEntity<?> imageUpload(
        @RequestParam(name = "imageFile") MultipartFile imageFile)
        throws IOException {
        FileVO file = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            file = fileUploadService.singleFileUpload(imageFile, "img/");
        }
        return new ResponseEntity<>(file.getImage_path(), HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/scrap/get")
    @Operation(summary = "스크랩들 가져오는 api")
    public ResponseEntity<?> getScraps(@RequestBody Article article) {
        List<Article> scraps = commonService.getScraps(article);
        return new ResponseEntity<>(scraps, HttpStatus.OK);
    }

    @PostMapping("/scrap/get/count")
    @Operation(summary = "스크랩들 개수 가져오는 api")
    public ResponseEntity<?> getScrapCnt(@RequestBody Article article) {
        Article cnt = commonService.getScrapsAllCnt(article);
        return new ResponseEntity<>(cnt, HttpStatus.OK);
    }

}
