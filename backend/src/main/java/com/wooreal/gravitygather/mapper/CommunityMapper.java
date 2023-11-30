package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.community.ArticleMaster;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {

    List<ArticleMaster> getArticleMasters();

    List<Article> getArticles(Article article);

    Article getArticle(Article article);

    int articleWrite(Article article);

}
