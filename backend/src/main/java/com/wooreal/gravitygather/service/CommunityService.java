package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.community.ArticleMaster;
import com.wooreal.gravitygather.dto.community.Comment;
import com.wooreal.gravitygather.dto.community.Like;
import com.wooreal.gravitygather.dto.user.User;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.CommunityMapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {

    private final CommunityMapper communityMapper;
    private final CommonService commonService;
    private final UserService userService;

    public CommunityService(CommunityMapper communityMapper, CommonService commonService, UserService userService) {
        this.communityMapper = communityMapper;
        this.commonService = commonService;
        this.userService = userService;
    }

    public List<ArticleMaster> getArticleMasters(){
        return communityMapper.getArticleMasters();
    }

    public List<Article> getArticles(Article article){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setUser_seq(seq+"");
        return communityMapper.getArticles(article);
    }

    public Article getArticlesAllCnt(Article article){
        return communityMapper.getArticlesAllCnt(article);
    }

    public Article getArticle(int seq){
        Article article = new Article();
        article.setSeq(seq);

        communityMapper.articleGoUpViewCount(article);

        List<Article> articles = getArticles(article);
        if(articles == null || articles.size() == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "존재하지 않는 게시물 입니다.");

        return articles.get(0);
    }

    public void articleWrite(Article article){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setUser_seq(seq+"");
        if(communityMapper.articleWrite(article) == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "게시물 등록 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
    }

    public List<Comment> getComments(Comment comment){
        return communityMapper.getComments(comment);
    }

    public void addComment(Comment comment){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setUser_seq(seq+"");
        if(communityMapper.addComment(comment) == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "댓글 등록 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
    }

    public void updateArticle(Article article){
        Article articleInfo = getArticle(article.getSeq());
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(articleInfo == null || !articleInfo.getUser_seq().equals(seq+"")){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "권한이 없습니다.");
        }

        if(communityMapper.updateArticle(article) == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "게시글 수정 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
    }

    public void deleteArticle(Article article){
        Article articleInfo = getArticle(article.getSeq());
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(articleInfo == null || !articleInfo.getUser_seq().equals(seq+"")){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "권한이 없습니다.");
        }
        if(communityMapper.deleteArticle(article) == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "게시글 삭제 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
    }

    public Comment getComment(int seq){
        Comment comment = new Comment();
        comment.setSeq(seq);

        List<Comment> comments = getComments(comment);
        if(comments == null || comments.size() == 0)
            throw new BusinessLogicException(HttpStatus.valueOf(500), "존재하지 않는 댓글 입니다.");

        return comments.get(0);
    }

    public List<Like> getLikes(Like like){
        return communityMapper.getLikes(like);
    }

    public Like getLike(Like like){
        List<Like> likes = communityMapper.getLikes(like);
        if(likes.size() > 0) return likes.get(0);
        else return null;
    }

    public void addLike(Like like){
        User userInfo = userService.getUserBySeq();

        if(like.getMode().equals("article")){
            Article article = getArticle(like.getContent_seq());
            if(article.getUser_seq().equals(like.getUser_seq()+"")) return;
            commonService.sendAlarm(Integer.parseInt(article.getUser_seq()), like.getUser_seq(), userInfo.getNickname() + "님께서 \"" + article.getTitle() + "\"게시글에 좋아요를 눌렀습니다.", "01");
        }else if(like.getMode().equals("comment")){
            Comment comment = getComment(like.getContent_seq());
            if(comment.getUser_seq().equals(like.getUser_seq()+"")) return ;
            commonService.sendAlarm(Integer.parseInt(comment.getUser_seq()), like.getUser_seq(), userInfo.getNickname() + "님께서 댓글에 좋아요를 눌렀습니다.", "02");
        }

        communityMapper.addLike(like);
    }

    public Like likeArticle(Like like){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        like.setUser_seq(seq);

        like.setMode("article");
        Like tmp = getLike(like);
        if(tmp != null){
            deleteLike(like);
            if(tmp.getIs_up() != like.getIs_up()){
                addLike(like);
            }
        }else{
            addLike(like);
        }

        return getLike(like);
    }

    public Like likeComment(Like like){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        like.setUser_seq(seq);

        like.setMode("comment");
        Like tmp = getLike(like);
        if(tmp != null){
            deleteLike(like);
            if(tmp.getIs_up() != like.getIs_up()){
                addLike(like);
            }
        }else{
            addLike(like);
        }

        return getLike(like);
    }

    public void deleteLike(Like like){
        communityMapper.deleteLike(like);
    }

    public boolean scrap(Article article){
        Integer seq = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setUser_seq(seq+"");
        Article scrap = commonService.getScrap(article);
        if(scrap != null){
            communityMapper.deleteScrap(article);
            return false;
        }else{
            communityMapper.addScrap(article);
            Article oriArticle = getArticle(article.getSeq());
            User userInfo = userService.getUserBySeq();
            commonService.sendAlarm(Integer.parseInt(oriArticle.getUser_seq()), seq, userInfo.getNickname() + "님께서 \"" + oriArticle.getTitle() + "\"글을 스크랩 했습니다.", "04");
            return true;
        }
    }
}
