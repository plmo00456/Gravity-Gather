package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.room.RoomRequest;
import com.wooreal.gravitygather.dto.room.RoomResponse;
import com.wooreal.gravitygather.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Common Controller", description = "공동 컨트롤러")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/common")
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService){
        this.commonService = commonService;
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
            , @RequestParam(name="lastSeq", required = false, defaultValue = "0") int alarmSeq) {
        commonService.readAlarm(userId, alarmSeq);
        return new ResponseEntity<>( HttpStatus.OK);
    }

}
