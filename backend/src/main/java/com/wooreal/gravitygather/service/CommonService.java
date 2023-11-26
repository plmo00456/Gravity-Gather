package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.config.WebSocketHandler;
import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.user.EmailVerificationResult;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import com.wooreal.gravitygather.dto.user.UserResponse;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.CommonMapper;
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
public class CommonService {

    private final CommonMapper commonMapper;

    public CommonService(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public List<Alarm> getAlarm(int userId){
        return commonMapper.getAlarm(userId);
    }

    public void readAlarm(int userId, int alarmSeq){
        commonMapper.readAlarm(userId, alarmSeq);
    }

//    public void sendAlarm()

}
