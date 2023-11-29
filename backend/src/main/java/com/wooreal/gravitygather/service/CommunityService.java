package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.community.ArticleMaster;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.CommonMapper;
import com.wooreal.gravitygather.mapper.CommunityMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {

    private final CommunityMapper communityMapper;

    public CommunityService(CommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    public List<ArticleMaster> getArticleMasters(){
        return communityMapper.getArticleMasters();
    }

    public List<Article> getArticles(Article article){
        return communityMapper.getArticles(article);
    }

    public Article getArticle(Article article){
        List<Article> articles = getArticles(article);
        if(articles == null || articles.size() == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "존재하지 않는 게시물 입니다.");

        return articles.get(0);
    }


}
