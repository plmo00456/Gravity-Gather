import {defineStore} from "pinia";
import http from "@/api/http";

export const useCommunityStore = defineStore({
    id: 'community',
    state: () => ({
        articleMasterList: [],
        articleList: [],
        article: null,
        dataResponse: null,
    }),
    actions: {
        async getArticleMaster() {
            return http.post(`/community/article-master/get`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.articleMasterList = response.data;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getArticles(article) {
            return http.post(`/community/article/get`, article,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.articleList = response.data;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getArticlesAllCnt(article) {
            return http.post(`/community/article/get/count`, article,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getArticle(seq) {
            return http.post(`/community/article/${seq}`,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async writeArticle(article){
            article.content = article.content.replaceAll("cursor: nwse-resize;", '');
            return http.post(`/community/article/write`, article,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getComments(article) {
            return http.post(`/community/article/comment/get`,article ,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async addComment(comment){
            comment.content = comment.content.replaceAll("cursor: nwse-resize;", '');
            if(comment.imageUrl != null){
                comment.content += `<img src="${comment.imageUrl}" alt="대댓글 이미지">`;
            }
            return http.post(`/community/article/comment/add`, comment ,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return true;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async updateArticle(article){
            article.content = article.content.replaceAll("cursor: nwse-resize;", '');
            return http.post(`/community/article/update`, article,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async deleteArticle(article){
            return http.post(`/community/article/delete`, article,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getLike(like){
            return http.post(`/community/article/like/get`, like,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async likeHandle(like){
            let modeText = '';
            if(like.mode === 'comment') modeText = '/comment';

            return http.post(`/community/article${modeText}/like`, like,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async scrap(article){
            return http.post(`/community/article/scrap`, article,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
    }
})
