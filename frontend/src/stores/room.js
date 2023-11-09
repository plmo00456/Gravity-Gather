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
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },
    }
})
