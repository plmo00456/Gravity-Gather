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
                        if(task.bg_color) task.backgroundColor = task.bg_color;
                        if(task.text_color) task.textColor = task.text_color;
                        task.oriContent = task.content;
                        task.oriTitle = task.title;
                        let content = '';
                        if(task.content != null && task.content !== '' && task.content !== '<p></p>'){
                            content += '<div class="font-bold">' + task.title + '</div>'
                            content += '<div>' + task.content + '</div>'
                        }
                        if(task.user_seq !== data.user_seq){
                            task.is_share = true;
                            const tmp = task.title;
                            task.title = '<div class="flex"><img class="mx-0.5 rounded-full bg-white w-[1rem] h-[1rem]" src="' + require('@/assets/image/share-icon.png') + '" alt="공유 아이콘">' + tmp + "</div>";
                            if(task.content){
                                content += '<div class="my-2 border-b border-gray-300"></div>'
                            }

                            content += '<div class="text-gray-500 text-xs font-bold">[공유자 : ' + task.user_nickname + ']</div>';
                            if(task.caption)
                                content += '<div class="text-gray-500 text-xs">' + task.caption + '</div>'
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
    }
})
