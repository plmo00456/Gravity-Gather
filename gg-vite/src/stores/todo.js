import {defineStore} from "pinia";
import http from "@/api/http";

export const useTodoStore = defineStore({
  id: 'todo',
  state: () => ({
    todoList: [],
    dataResponse: null,
  }),
  getters: {
    getDataResponse: (state) => {
      state.dataResponse;
    }
  },
  actions: {
    async getTodos(todo){
      return http.post(`/todo/${todo}/get`, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        if (response.status === 200) {
          this.dataResponse = response;
          this.todoList = response.data;
          return response.data;
        }
      })
      .catch(error => {
        this.dataResponse = error.response;
      });
    },
    async addTodo(todo){
      return http.post('/todo/add',todo, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        if (response.status === 200) {
          this.dataResponse = response;
          return true;
        }else{
          return false;
        }
      })
      .catch(error => {
        this.dataResponse = error.response;
        return false;
      });
    },
    async updateTodo(todo){
      return http.post('/todo/update',todo, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        if (response.status === 200) {
          this.dataResponse = response;
          return true;
        }else{
          return false;
        }
      })
      .catch(error => {
        this.dataResponse = error.response;
        return false;
      });
    },
    async completeTodo(todo){
      return http.post('/todo/complete',todo, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        if (response.status === 200) {
          this.dataResponse = response;
          return true;
        }else{
          return false;
        }
      })
      .catch(error => {
        this.dataResponse = error.response;
        return false;
      });
    },
    async deleteTodo(todo){
      return http.post('/todo/delete',todo, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        if (response.status === 200) {
          this.dataResponse = response;
          return true;
        }else{
          return false;
        }
      })
      .catch(error => {
        this.dataResponse = error.response;
        return false;
      });
    },
  }
})
