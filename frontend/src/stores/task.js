import {defineStore} from "pinia";
import http from "@/api/http";

export const useTaskStore = defineStore({
    id: 'task',
    state: () => ({
        taskList: [],
        dataResponse: null,
    }),
    actions: {
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
