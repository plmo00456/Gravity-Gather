package com.wooreal.gravitygather.controller;

import com.wooreal.gravitygather.dto.common.Alarm;
import com.wooreal.gravitygather.dto.community.Article;
import com.wooreal.gravitygather.dto.community.ArticleMaster;
import com.wooreal.gravitygather.service.CommonService;
import com.wooreal.gravitygather.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Common Controller", description = "커뮤니티 컨트롤러")
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/v1/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService){
        this.communityService = communityService;
    }

    @PostMapping("/article-master/get")
    @ApiOperation(value = "게시글 마스터 가져오는 api")
    public ResponseEntity<?> getArticleMaster() {
        List<ArticleMaster> articleMasters = communityService.getArticleMasters();
        return new ResponseEntity<>( articleMasters, HttpStatus.OK);
    }

    @PostMapping("/article/get")
    @ApiOperation(value = "게시글들 가져오는 api")
    public ResponseEntity<?> getArticles(@RequestBody Article article) {
        List<Article> articles = communityService.getArticles(article);
        return new ResponseEntity<>( articles, HttpStatus.OK);
    }

    @PostMapping("/article")
    @ApiOperation(value = "게시글 가져오는 api")
    public ResponseEntity<?> getArticle(@RequestBody Article article) {
        Article getArticle = communityService.getArticle(article);
        return new ResponseEntity<>( getArticle, HttpStatus.OK);
    }

}
