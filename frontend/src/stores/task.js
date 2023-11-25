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

function formatDate(date, format) {
    let hours = parseInt(unixToFormat(date, "%H"));
    let minutes = parseInt(unixToFormat(date, "%M"));

    if ((hours === 0 && minutes === 0) || (hours === 23 && minutes === 59)) {
        return unixToFormat(date, "%Y-%m-%d");
    } else {
        return unixToFormat(date, format);
    }
}

export const useTaskStore = defineStore({
    id: 'task',
    state: () => ({
        taskList: [],
        taskCategoryList: [],
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
                        if(task.bg_color){
                            task.backgroundColor = task.bg_color;
                            task.borderColor = task.bg_color;
                        }
                        if(task.text_color) task.textColor = task.text_color;
                        task.oriContent = task.content;
                        task.oriTitle = task.title;
                        let content = '';
                        let categotyNM = task.category_nm ? ' [' + task.category_nm + ']' : '';
                        if(task.content != null && task.content !== '' && task.content !== '<p></p>'){
                            content += '<div class="font-bold">' + task.title + categotyNM + '</div>'
                            content += '<div>' + task.content + '</div>'
                        }
                        const tmp = task.title;
                        if(task.user_seq !== data.user_seq){
                            task.is_share = true;
                            task.title = '<div class="flex items-center px-0.5"><div class="flex justify-center items-center rounded-full mr-0.5 bg-white w-[1rem] h-[1rem]"><img class="w-[0.6rem] h-[0.6rem]" src="' + require('@/assets/image/share-icon.png') + '" alt="공유 아이콘"></div>' + tmp + "</div>";
                            if(task.content){
                                content += '<div class="my-2 border-b border-gray-300"></div>'
                            }

                            content += '<div class="text-gray-500 text-xs font-bold">[공유자 : ' + task.user_nickname + ']</div>';
                            if(task.caption)
                                content += '<div class="text-gray-500 text-xs">' + task.caption + '</div>'
                        }else{
                            task.title = '<div class="flex items-center px-0.5">' + tmp + '</div>';
                        }
                        task.content = content;
                       if(task.is_all_day){
                           task.date = unixToFormat(task.start_date_time, "%Y-%m-%d");
                       }else{
                           task.start = formatDate(task.start_date_time, "%Y-%m-%d %H:%M:%S");
                           task.end = formatDate(task.end_date_time, "%Y-%m-%d %H:%M:%S");
                       }
                    });
                    this.taskList = tasks;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getCategory(task){
            return http.post(`/task/category/get/`, task, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.taskCategoryList = response.data;
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
        async updateTask(task){
            return http.post(`/task/update/`, task, {
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
        async addCategory(task){
            return http.post(`/task/category/add`, task, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.taskCategoryList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async updateCategory(categoty){
            return http.post(`/task/category/update`, categoty, {
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
        async updateCategoryOrder(task) {
            return http.post(`/task/category/update/order`, task, {
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
