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
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getArticle(article) {
            return http.post(`/community/article/${article}`,{
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
        }
    }
})