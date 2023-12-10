package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.config.LoginRequired;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.community.ArticleMaster;
import com.wooreal.gravitygather.dto.community.Comment;
import com.wooreal.gravitygather.dto.community.Like;
import com.wooreal.gravitygather.service.CommunityService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(info = @Info(title = "Community Controller", version = "v1", description = "커뮤니티 컴트롤러"))
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping("/article-master/get")
    @Operation(summary = "게시글 마스터 가져오는 api")
    public ResponseEntity<?> getArticleMaster() {
        List<ArticleMaster> articleMasters = communityService.getArticleMasters();
        return new ResponseEntity<>(articleMasters, HttpStatus.OK);
    }

    @PostMapping("/article/get")
    @Operation(summary = "게시글들 가져오는 api")
    public ResponseEntity<?> getArticles(@RequestBody Article article) {
        List<Article> articles = communityService.getArticles(article);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @PostMapping("/article/get/count")
    @Operation(summary = "게시글들 개수 가져오는 api")
    public ResponseEntity<?> getArticlesCnt(@RequestBody Article article) {
        Article cnt = communityService.getArticlesAllCnt(article);
        return new ResponseEntity<>(cnt, HttpStatus.OK);
    }

    @PostMapping("/article/{seq}")
    @Operation(summary = "게시글 가져오는 api")
    public ResponseEntity<?> getArticle(@PathVariable("seq") int seq) {
        Article getArticle = communityService.getArticle(seq);
        return new ResponseEntity<>(getArticle, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/article/write")
    @Operation(summary = "게시글 작성 api")
    public ResponseEntity<?> articleWrite(@RequestBody Article article) {
        communityService.articleWrite(article);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/article/comment/get")
    @Operation(summary = "게시글 댓글 가져오는 api")
    public ResponseEntity<?> getComments(@RequestBody Comment comment) {
        List<Comment> comments = communityService.getComments(comment);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }


    @LoginRequired
    @PostMapping("/article/comment/add")
    @Operation(summary = "게시글 댓글 추가 api")
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        communityService.addComment(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/article/update")
    @Operation(summary = "게시글 수정 api")
    public ResponseEntity<?> updateArticle(@RequestBody Article article) {
        communityService.updateArticle(article);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/article/delete")
    @Operation(summary = "게시글 삭제 api")
    public ResponseEntity<?> deleteArticle(@RequestBody Article article) {
        communityService.deleteArticle(article);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/article/like/get")
    @Operation(summary = "좋아요 단일 가져오는 api")
    public ResponseEntity<?> getLike(@RequestBody Like like) {
        Like data = communityService.getLike(like);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/article/likes/get")
    @Operation(summary = "좋아요 가져오는 api")
    public ResponseEntity<?> getLikes(@RequestBody Like like) {
        List<Like> likes = communityService.getLikes(like);
        return new ResponseEntity<>(likes,  HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/article/like")
    @Operation(summary = "게시글 좋아요 api")
    public ResponseEntity<?> likeArticle(@RequestBody Like like) {
        Like data = communityService.likeArticle(like);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/article/comment/like")
    @Operation(summary = "게시글 댓글 좋아요 api")
    public ResponseEntity<?> likeComment(@RequestBody Like like) {
        Like data = communityService.likeComment(like);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @LoginRequired
    @PostMapping("/article/scrap")
    @Operation(summary = "게시글 스크랩 처리 api")
    public ResponseEntity<?> scrap(@RequestBody Article article) {
        Boolean scrap = communityService.scrap(article);
        return new ResponseEntity<>(scrap, HttpStatus.OK);
    }

}
