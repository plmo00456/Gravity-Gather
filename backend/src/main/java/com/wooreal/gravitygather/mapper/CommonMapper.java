package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

    List<Alarm> getAlarm(int userId);

    void readAlarm(int userId, int alarmSeq);

    int sendAlarm(int receive_seq, int sender_seq, String msg, String code);

    List<Article> getScraps(Article article);

    Article getScrapsAllCnt(Article article);
}
