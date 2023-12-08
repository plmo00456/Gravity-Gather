import {defineStore} from "pinia";
import http from "@/api/http";

export const useCommonStore = defineStore({
    id: 'common',
    state: () => ({
        alarmList: [],
        dataResponse: null,
        scrapList: [],
    }),
    actions: {
        async getAlarm(){
            return http.post(`/common/alarm/get`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.alarmList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async readAlarm(alarmSeq){
            const data = {
                'seq' : alarmSeq,
            }
            return http.post(`/common/alarm/read`, data, {
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
        async imageUpload(userInfo){
            return http.post(`/common/image/upload`, userInfo, {
                headers: {
                    // 'Content-Type': 'multipart/form-data'
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
        async getScraps(article) {
            return http.post(`/common/scrap/get`, article,{
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.scrapList = response.data;
                    return response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getScrapsAllCnt(article) {
            return http.post(`/common/scrap/get/count`, article,{
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
