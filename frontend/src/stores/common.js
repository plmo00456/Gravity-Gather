import {defineStore} from "pinia";
import http from "@/api/http";

export const useCommonStore = defineStore({
    id: 'common',
    state: () => ({
        alarmList: [],
        dataResponse: null,
    }),
    actions: {
        async getAlarm(userId){
            return http.post(`/common/alarm/get/`+userId, {
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
        async readAlarm(userId, alarmSeq){
            const data = {
                'seq' : alarmSeq,
            }
            return http.post(`/common/alarm/read/`+userId, data, {
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
