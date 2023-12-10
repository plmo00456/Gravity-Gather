package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.community.ArticleMaster;
import com.wooreal.gravitygather.dto.community.Comment;
import com.wooreal.gravitygather.dto.community.Like;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommunityMapper {

    List<ArticleMaster> getArticleMasters();

    List<Article> getArticles(Article article);

    Article getArticlesAllCnt(Article article);

    Article getArticle(Article article);

    int articleWrite(Article article);

    void articleGoUpViewCount(Article article);

    List<Comment> getComments(Comment comment);

    int addComment(Comment comment);

    int updateArticle(Article article);

    int deleteArticle(Article article);

    void likeArticle(Like like);

    void likeComment(Like like);

    List<Like> getLikes(Like like);

    int addLike(Like like);

    int deleteLike(Like like);

    int deleteComment(Like like);

    int deleteScrap(Article article);

    int addScrap(Article article);
}
