package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.mapper.CommonMapper;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    private final CommonMapper commonMapper;

    public CommonService(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public List<Alarm> getAlarm(){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return commonMapper.getAlarm(seq);
    }

    public void readAlarm(){
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commonMapper.readAlarm(userId);
    }

    public void sendAlarm(int receive_seq, int sender_seq, String msg, String code){
        try{
            if(receive_seq != sender_seq)
                commonMapper.sendAlarm(receive_seq, sender_seq, msg, code);
        }catch (Exception e){}
    }

    public List<Article> getScraps(Article article){
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setUser_seq(userSeq+"");
        return commonMapper.getScraps(article);
    }

    public Article getScrap(Article article){
        List<Article> scraps = commonMapper.getScraps(article);
        if(scraps == null || scraps.size() == 0) return null;
        else return scraps.get(0);
    }

    public Article getScrapsAllCnt(Article article){
        Integer userSeq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setUser_seq(userSeq+"");
        return commonMapper.getScrapsAllCnt(article);
    }

}
