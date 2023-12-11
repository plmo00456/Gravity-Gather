import {defineStore} from "pinia";
import http from "@/api/http";

export const useRoomStore = defineStore({
    id: 'room',
    state: () => ({
        rooms: [],
        dataResponse: null,
        currentRoom: [],
        currentRoomParticipants: [],
    }),
    getters: {
        getDataResponse: (state) => {
            state.dataResponse;
        }
    },
    actions: {
        async getRoom() {
            return http.post('/room/get', {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.rooms = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async getSingleRoom(roomId) {
            return http.post(`/room/${roomId}/get`, {
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
        async getRoomParticipants(roomId) {
            return http.post('/room/'+roomId+'/participants', {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.currentRoomParticipants = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
        async createRoom(roomInfo) {
            return http.post('/room/create', roomInfo, {
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
        async enterRoom(roomInfo) {
            return http.post('/room/enter', roomInfo, {
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
                console.log(error);
                this.dataResponse = error.response;
            });
        },
        async deleteRoom(roomId) {
            return http.post('/room/'+roomId+'/delete', {
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
                    console.log(error);
                    this.dataResponse = error.response;
                });
        },
        async isInTheRoom() {
            return http.post('/room/isInTheRoom', {
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
                console.log(error);
                this.dataResponse = error.response;
            });
        },
        async kick(user){
            return http.post('/room/kick', user,{
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
        async outRoom(){
            return http.post('/room/out', {
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
