import {defineStore} from "pinia";
import axios from "axios";
import http from "@/api/http";

export const useUserStore = defineStore({
    id: 'user',
    state: () => ({
        isLoggedIn: false,
        info: null,
    }),
    getters: {
        username() {
            return this.info ? this.info.username : '';
        }
    },
    actions: {
        async login(credentials) {
            try {
                const response = await http.post('/user/login', credentials)
                if (response.status === 200) {
                    this.isLoggedIn = true;
                    this.info = response.data.user;
                } else {
                    throw new Error('Login failed')
                }
            } catch (error) {
                console.error(error)
                throw error
            }
        },

        async logout() {
            try{
                const response = await axios.post('/api/v1/logout')

                if(response.status === 200){
                    this.isLoggedIn = false;
                    this.info = null;
                } else{
                    throw new Error('Logout failed')
                }

            } catch(error){
                console.error(error)
                throw error
            }

        }
    }
})
