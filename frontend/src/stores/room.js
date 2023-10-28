import {defineStore} from "pinia";
import axios from "axios";
import http from "@/api/http";

export const useUserStore = defineStore({
    id: 'user',
    state: () => ({
        isLoggedIn: false,
        userInfo: null,
        dataResponse: null,
    }),
    getters: {
        username() {
            return this.userInfo ? this.userInfo.username : '';
        },
        getDataResponse: (state) => {
            state.dataResponse;
        }
    },
    actions: {
        async login(credentials) {
            return http.post('/user/login', credentials, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.status === 200) {
                        if(response.data.status !== 'UNVERIFIED')
                            this.isLoggedIn = true;

                        this.dataResponse = response;
                        this.userInfo = response.data;
                    }
                })
                .catch(error => {
                    this.dataResponse = error.response;
                });
        },

        async logout() {
            try {
                const response = await axios.post('/api/v1/logout')

                if (response.status === 200) {
                    this.isLoggedIn = false;
                    this.info = null;
                } else {
                    throw new Error('Logout failed')
                }

            } catch (error) {
                console.error(error)
                throw error
            }

        },

        async sendMail(email) {
            return http.post('/user/emails/verification-requests', email, {
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

        async emailVerify(emailAndCode) {
            return http.post('/user/emails/verifications', emailAndCode, {
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
