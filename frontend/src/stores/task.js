import {defineStore} from "pinia";
import http from "@/api/http";

function unixToFormat(unix_timestamp, format) {
    var date = new Date(unix_timestamp * 1000); // UNIX 타임스탬프를 밀리초로 변환

    var map = {
        '%Y': date.getFullYear(),
        '%m': ('0' + (date.getMonth() + 1)).slice(-2),
        '%d': ('0' + date.getDate()).slice(-2),
        '%H': ('0' + date.getHours()).slice(-2),
        '%M': ('0' + date.getMinutes()).slice(-2),
        '%S': ('0' + date.getSeconds()).slice(-2)
    };

    return format.replace(/%[YmdHMS]/g, function(m) { return map[m]; });
}

export const useTaskStore = defineStore({
    id: 'task',
    state: () => ({
        taskList: [],
        dataResponse: null,
    }),
    actions: {
        async getTasks(data){
            return http.post(`/task/get/`, data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                this.dataResponse = response;
                if (response.status === 200) {
                    const tasks = this.dataResponse.data;
                    tasks.forEach(task => {
                       if(task.is_all_day){
                           task.date = unixToFormat(task.start_date_time, "%Y-%m-%d");
                       }else{
                           task.start = task.start_time ? unixToFormat(task.start_date_time, "%Y-%m-%d %H:%M:%S") : unixToFormat(task.start_date_time, "%Y-%m-%d");
                           task.end = task.end_time ? unixToFormat(task.end_date_time, "%Y-%m-%d %H:%M:%S") : unixToFormat(task.end_date_time, "%Y-%m-%d");
                       }
                    });
                    this.taskList = tasks;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async addTask(task){
            return http.post(`/task/add/`, task, {
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
    }
})
