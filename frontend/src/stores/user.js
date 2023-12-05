import {defineStore} from "pinia";
import http from "@/api/http";
import {router} from "@/router/index.js"

export const useUserStore = defineStore({
    id: 'user',
    state: () => ({
        isLoggedIn: false,
        userInfo: null,
        dataResponse: null,
        friendList: [],
        token: {
            access: null,
            refresh: null,
        },
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
                    if(response.headers['accessToken']){
                        localStorage.setItem('accessToken', response.headers['accessToken']);
                        localStorage.setItem('refreshToken', response.headers['refreshToken']);
                        if(response.data.status !== 'UNVERIFIED')
                            this.isLoggedIn = true;

                        this.userInfo = response.data;
                    }
                    this.dataResponse = response;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async logout() {
            try {
                localStorage.setItem('accessToken', null);
                localStorage.setItem('refreshToken', null);
                this.isLoggedIn = false;
                this.userInfo = null;
                if (router.currentRoute.value.path === '/') {
                    await router.go();
                } else {
                    await router.push({ path: '/' });
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
        },

        async userInfoStateUpdate(seq) {
            return http.get(`/user/${seq}/info`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;

                    let chkPhoto = false;
                    for (let key of Object.keys(response.data)) {
                        if (response.data[key] !== null && response.data[key] !== undefined) {
                            if(key === 'photo') chkPhoto = true;
                            this.userInfo[key] = response.data[key];
                        }
                    }
                    if(!chkPhoto)
                        this.userInfo['photo'] = null;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async userInfoUpdate(userInfo){
            return http.post(`/user/update`, userInfo, {
                headers: {
                    'Content-Type': 'multipart/form-data'
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

        async getFriends(friend){
            return http.post(`/user/friend/get`, friend, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.friendList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async addFriend(friend){
            return http.post(`/user/friend/add`, friend, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.friendList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        },

        async deleteFriend(friend){
            return http.post(`/user/friend/delete`, friend, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.status === 200) {
                    this.dataResponse = response;
                    this.friendList = response.data;
                }
            })
            .catch(error => {
                this.dataResponse = error.response;
            });
        }
    }
})
