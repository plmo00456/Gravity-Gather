package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.dto.user.UserRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {

    List<Alarm> getAlarm(int userId);

    void readAlarm(int userId, int alarmSeq);

    int sendAlarm(int receive_seq, int sender_seq, String msg);
}
